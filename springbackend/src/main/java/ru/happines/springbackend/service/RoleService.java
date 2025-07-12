package ru.happines.springbackend.service;

import org.springframework.stereotype.Service;
import ru.happines.springbackend.model.Role;

import java.util.List;

public interface RoleService {
    Role findById(long id);
    List<String> findAllNames();
}
