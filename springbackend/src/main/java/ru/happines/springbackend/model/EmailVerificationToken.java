package ru.happines.springbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
public class EmailVerificationToken extends AbstractToken {
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public EmailVerificationToken(String token, User user,  long expiryTimeToLiveInSec) {
        super(token, expiryTimeToLiveInSec);
        this.user = user;
    }

    public EmailVerificationToken() {
        super("", 1000);
    }
}
