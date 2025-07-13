package ru.happines.springbackend.service;

import ru.happines.springbackend.dto.CreateUserDTO;
import ru.happines.springbackend.model.User;

import java.util.List;

public interface UserService {
    User findById(long id);

    List<String> findAllNames();

    User create(CreateUserDTO userDTO);
}
