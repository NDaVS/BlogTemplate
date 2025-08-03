package ru.happines.springbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.dto.request.auth.ResetPasswordDTO;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.User;

import java.util.List;

public interface UserService {
    User findById(long id);

    void resetPassword(HttpServletRequest request, ResetPasswordDTO resetPasswordDTO) throws ServiceException;

    User findByUsername(String username) throws ServiceException;

    User findByEmail(String email) throws ServiceException;

    boolean existsByEmail(String email);

    @Deprecated
    List<String> findAllNames();

    User create(CreateUserDTO userDTO);
}
