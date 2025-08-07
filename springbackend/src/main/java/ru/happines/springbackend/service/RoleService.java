package ru.happines.springbackend.service;

import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.enums.RoleType;

import java.util.List;

public interface RoleService {
    @Deprecated
    Role findById(long id);
    @Deprecated
    List<RoleType> findAllNames();
    // возвожно стоит сделать назначение роли по юзернейму.
    void setRole(String username, RoleType roleType);
}
