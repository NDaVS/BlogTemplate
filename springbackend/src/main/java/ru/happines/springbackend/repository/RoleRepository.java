package ru.happines.springbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.happines.springbackend.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
