package ru.happines.springbackend.service;

import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.dto.request.auth.RecoveryPasswordDTO;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.User;

public interface AuthService {
    User signup(CreateUserDTO userDTO);

    String sendPasswordRecoveryToken(String username) throws ServiceException;

    void validateRecoveryToken(String token) throws ServiceException;

    void validateEmail(String token) throws ServiceException;

    void recoveryPassword(RecoveryPasswordDTO recoveryPasswordDTO) throws ServiceException;
}
