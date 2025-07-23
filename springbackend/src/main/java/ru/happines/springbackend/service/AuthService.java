package ru.happines.springbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.happines.springbackend.dto.auth.ResetPasswordDTO;
import ru.happines.springbackend.exception.ServiceException;

public interface AuthService {
    void resetPassword(HttpServletRequest request, ResetPasswordDTO resetPasswordDTO) throws ServiceException;

}
