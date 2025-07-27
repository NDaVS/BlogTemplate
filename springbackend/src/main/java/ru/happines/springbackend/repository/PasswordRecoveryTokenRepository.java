package ru.happines.springbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.happines.springbackend.model.PasswordRecoveryToken;

import java.util.Date;
import java.util.Optional;

public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long> {
    Optional<PasswordRecoveryToken> findByToken(String token);

    void deleteByExpiryDateBefore(Date now);
}
