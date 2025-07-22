package ru.happines.springbackend.security.jwt;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String rawAccessToken = (String) authentication.getCredentials();
        UserDetails securityUser = authenticate(rawAccessToken);
        return new JwtAuthenticationToken(securityUser);
    }

    public UserDetails authenticate(String accessToken) throws AuthenticationException {
        if (StringUtils.isEmpty(accessToken)) {
            throw new BadCredentialsException("Token is empty");
        }

        return jwtTokenProvider.parseJwtToken(accessToken);
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
