package ru.happines.springbackend.service;

import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.enums.RoleType;

import java.util.List;

public interface RoleService {
    Role findById(long id);
    List<RoleType> findAllNames();
    Role setRole(long userId, RoleType roleType);
}
