package ru.happines.springbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import ru.happines.springbackend.dto.CreateUserDTO;
import ru.happines.springbackend.dto.auth.RecoveryPasswordDTO;
import ru.happines.springbackend.dto.auth.ResetPasswordDTO;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.User;

public interface AuthService {
    User signup(@RequestBody CreateUserDTO userDTO) ;

    void resetPassword(HttpServletRequest request, ResetPasswordDTO resetPasswordDTO) throws ServiceException;

    void sendPasswordRecoveryToken(String username) throws ServiceException;

    void validateRecoveryToken(String token) throws ServiceException;

    void validateEmail(String token) throws ServiceException;

    void recoveryPassword(RecoveryPasswordDTO recoveryPasswordDTO) throws ServiceException;
}
