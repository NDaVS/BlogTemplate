package ru.happines.springbackend.service.Implementation;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.happines.springbackend.dto.auth.RecoveryPasswordDTO;
import ru.happines.springbackend.dto.auth.ResetPasswordDTO;
import ru.happines.springbackend.exception.ErrorCode;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.PasswordRecoveryToken;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.repository.PasswordRecoveryTokenRepository;
import ru.happines.springbackend.repository.UserRepository;
import ru.happines.springbackend.security.jwt.JwtTokenProvider;
import ru.happines.springbackend.service.AuthService;

import java.util.Calendar;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    private final PasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


    public AuthServiceImpl(JwtTokenProvider tokenProvider,
                           UserRepository userRepository,
                           PasswordRecoveryTokenRepository passwordRecoveryTokenRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordRecoveryTokenRepository = passwordRecoveryTokenRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public void resetPassword(HttpServletRequest request, ResetPasswordDTO resetPasswordDTO) throws ServiceException {
        String username = tokenProvider.getUserNameFromJwtToken(tokenProvider.getTokenFromRequest(request));
        User user = findUserByUsername(username);
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmNewPassword())) {
            throw new ServiceException(ErrorCode.BAD_REQUEST_PARAMS, "New  and confirmation passwords are not equal");
        }

        user.setHashedPassword(encoder.encode(resetPasswordDTO.getNewPassword()));
        userRepository.save(user);

        logger.debug("Reset password for user {}", username);
    }

    @Override
    public void sendPasswordRecoveryToken(String username) throws ServiceException {
        User user = findUserByUsername(username);
        String rawToken = generateRecoveryToken();
        PasswordRecoveryToken token = new PasswordRecoveryToken(rawToken, user);
        passwordRecoveryTokenRepository.save(token);

        System.out.println(rawToken); // дописать функционал работы с почтой
    }

    @Override
    public void validateRecoveryToken(String rawToken) throws ServiceException {
        validateRawRecoveryToken(rawToken);
    }

    private User findUserByUsername(String username) throws ServiceException {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ServiceException(
                                ErrorCode.BAD_REQUEST_PARAMS,
                                "No user with username " + username)
                );
    }

    private String generateRecoveryToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void recoveryPassword(RecoveryPasswordDTO recoveryPasswordDTO) throws ServiceException {
        PasswordRecoveryToken token = validateRawRecoveryToken(recoveryPasswordDTO.getRecoveryToken());

        User user = token.getUser();

        if (!recoveryPasswordDTO.getNewPassword().equals(recoveryPasswordDTO.getConfirmNewPassword())) {
            throw new ServiceException(ErrorCode.BAD_REQUEST_PARAMS, "New passwords are not equal");
        }

        user.setHashedPassword(encoder.encode(recoveryPasswordDTO.getNewPassword()));

        userRepository.save(user);
        passwordRecoveryTokenRepository.delete(token);
    }

    private PasswordRecoveryToken getPasswordToken(String recoveryToken) throws ServiceException {
        return passwordRecoveryTokenRepository
                .findByToken(recoveryToken)
                .orElseThrow(() -> new ServiceException(ErrorCode.BAD_REQUEST_PARAMS, "Invalid recovery token"));
    }

    private boolean isRecoveryTokenExpired(PasswordRecoveryToken token) {
        final Calendar cal = Calendar.getInstance();

        return token.getExpiryDate().before(cal.getTime());
    }

    private PasswordRecoveryToken validateRawRecoveryToken(String rawToken) throws ServiceException {
        PasswordRecoveryToken token = getPasswordToken(rawToken);

        if (isRecoveryTokenExpired(token)) {
            throw new ServiceException(ErrorCode.RECOVERY_TOKEN_EXPIRED, "Token is expired");
        }

        return token;
    }
}
