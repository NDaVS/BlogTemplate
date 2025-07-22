package ru.happines.springbackend.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtPair {
    private final String token;
    private final String refreshToken;
}
