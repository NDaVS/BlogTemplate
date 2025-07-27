package ru.happines.springbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class AbstractToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String token;

    private Date expiryDate;



    public AbstractToken(String token, long expiryTimeToLiveInSec) {
        this.token = token;
        this.expiryDate = new Date(System.currentTimeMillis() + expiryTimeToLiveInSec * 1000);
    }
}
