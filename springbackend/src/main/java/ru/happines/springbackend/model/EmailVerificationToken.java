package ru.happines.springbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
public class EmailVerificationToken extends AbstractToken {
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;


    public EmailVerificationToken(String token, User user) {
        super(token);
        this.user = user;
    }

    public EmailVerificationToken() {
        super("");
    }
}
