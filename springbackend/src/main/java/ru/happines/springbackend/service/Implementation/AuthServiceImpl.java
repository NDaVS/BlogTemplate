package ru.happines.springbackend.service.Implementation;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.happines.springbackend.dto.auth.ResetPasswordDTO;
import ru.happines.springbackend.exception.ErrorCode;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.repository.UserRepository;
import ru.happines.springbackend.security.jwt.JwtTokenProvider;
import ru.happines.springbackend.security.login.LoginAuthenticationFilter;
import ru.happines.springbackend.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


    public AuthServiceImpl(JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public void resetPassword(HttpServletRequest request, ResetPasswordDTO resetPasswordDTO) throws ServiceException {
        String username = tokenProvider.getUserNameFromJwtToken(tokenProvider.getTokenFromRequest(request));
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ServiceException(
                                ErrorCode.BAD_REQUEST_PARAMS,
                                "No user with username: " + username
                        )
                );
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmNewPassword())) {
            throw new ServiceException(ErrorCode.BAD_REQUEST_PARAMS, "New  and confirmation passwords are not equal");
        }

        user.setHashedPassword(encoder.encode(resetPasswordDTO.getNewPassword()));
        userRepository.save(user);

        logger.debug("Reset password for user {}", username);
    }
}
