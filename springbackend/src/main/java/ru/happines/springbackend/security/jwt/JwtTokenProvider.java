package ru.happines.springbackend.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.happines.springbackend.security.exception.ExpiredTokenException;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    public static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    public static final String HEADER = "Authorization";
    public static final String JWT_TOKEN_HEADER_PARAM = HEADER;
    public static final String HEADER_PREFIX = "Bearer ";
    private final UserDetailsService userDetailsService;

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.tokenExpirationTime}")
    private int tokenExpirationTimeInSec;

    @Value("${security.jwt.refreshTokenExpirationTime}")
    private int refreshTokenExpirationTimeInSec;

    public JwtTokenProvider(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public JwtPair generateTokenPair(final UserDetails user) {
        String token = createToken(user);
        String refreshToken = createRefreshToken(user);
        return new JwtPair(token, refreshToken);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    private String createRefreshToken(final UserDetails user) {
        Claims claims = Jwts.claims()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(getExpiryDate(refreshTokenExpirationTimeInSec))
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    private String createToken(final UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(getExpiryDate(tokenExpirationTimeInSec))
                .signWith(getSigningKey())
                .compact();
    }

    private Date getExpiryDate(final int tokenExpirationTimeInSec) {
        Date now = new Date();
        return new Date(now.getTime() + tokenExpirationTimeInSec * 1000L);
    }

    public UserDetails parseJwtToken(final String accessToken) {
        UserDetails userDetails = null;
        if (StringUtils.hasText(accessToken) && validateToken(accessToken)) {
            String username = getUserNameFromJwtToken(accessToken);
            userDetails = userDetailsService.loadUserByUsername(username);
        }

        return userDetails;
    }

    public String getUserNameFromJwtToken(final String token) {
        logger.debug(Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token).getPayload().getSubject()
        );
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(final String authToken) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException ex) {
            logger.debug("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (SignatureException | ExpiredJwtException expiredEx) {
            logger.debug("JWT Token is expired", expiredEx);
            throw new ExpiredTokenException(authToken, "JWT Token expired", expiredEx);
        }
    }


    public String getTokenFromRequest(final HttpServletRequest request) {
        String header = request.getHeader(JWT_TOKEN_HEADER_PARAM);
        if (org.apache.commons.lang3.StringUtils.isBlank(header)) {
            throw new BadCredentialsException("Missing JWT Token");
        }
        if (header.length() <= HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size");
        }
        return header.substring(HEADER_PREFIX.length());
    }
}