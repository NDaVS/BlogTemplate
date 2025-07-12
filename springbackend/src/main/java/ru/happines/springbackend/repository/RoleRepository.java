package ru.happines.springbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.enums.RoleType;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
