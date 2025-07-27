package ru.happines.springbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "password_recovery_tokens")
@Setter
@Getter
public class PasswordRecoveryToken extends AbstractToken {
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public PasswordRecoveryToken(String token, User user, long expiryTimeToLiveInSec) {
        super(token,  expiryTimeToLiveInSec);
        this.user = user;
    }

    public PasswordRecoveryToken() {
        super("", 100);
    }
}
