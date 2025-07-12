package ru.happines.springbackend.service;

import org.springframework.stereotype.Service;
import ru.happines.springbackend.model.User;

import java.util.List;

public interface UserService {
    User findById(long id);
    List<String> findAllNames();
}
