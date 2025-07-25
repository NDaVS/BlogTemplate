package ru.happines.springbackend.service;

import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.User;

import java.util.List;

public interface UserService {
    User findById(long id);

    User findByUsername(String username) throws ServiceException;

    User findByEmail(String email) throws ServiceException;

    boolean existsByEmail(String email);

    List<String> findAllNames();

    User create(CreateUserDTO userDTO);
}
