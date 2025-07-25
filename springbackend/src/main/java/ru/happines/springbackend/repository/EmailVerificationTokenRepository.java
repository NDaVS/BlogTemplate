package ru.happines.springbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.happines.springbackend.model.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
}
