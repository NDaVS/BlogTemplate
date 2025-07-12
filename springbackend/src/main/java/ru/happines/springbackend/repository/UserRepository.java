package ru.happines.springbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.happines.springbackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
