package ru.happines.springbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Entity
@Table(name = "password_recovery_tokens")
@Data
@NoArgsConstructor
public class PasswordRecoveryToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    @Value("${security.recovery.user.tokenExpirationTime}")
    private Long expiryTimeToLiveInSec;

    public PasswordRecoveryToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = new Date(System.currentTimeMillis() + expiryTimeToLiveInSec * 1000);
    }
}
