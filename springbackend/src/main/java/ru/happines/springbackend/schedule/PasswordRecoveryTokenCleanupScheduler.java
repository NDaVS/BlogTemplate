package ru.happines.springbackend.schedule;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.happines.springbackend.repository.PasswordRecoveryTokenRepository;

import java.util.Date;

@Component
@AllArgsConstructor
public class PasswordRecoveryTokenCleanupScheduler {
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;

    @Scheduled(fixedRate = 3_600_000)
    public void cleanup() {
        passwordRecoveryTokenRepository.deleteByExpiryDateBefore(new Date());
    }
}
