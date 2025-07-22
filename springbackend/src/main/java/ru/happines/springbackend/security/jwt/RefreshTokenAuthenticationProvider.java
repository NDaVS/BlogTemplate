package ru.happines.springbackend.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.service.Implementation.UserDetailsImpl;
import ru.happines.springbackend.service.UserService;

@Component
@AllArgsConstructor
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        String username = tokenProvider.getUserNameFromJwtToken(token);
        UserDetailsImpl userDetails = getUserDetails(username);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    private UserDetailsImpl getUserDetails(String username) {
        try {
            return UserDetailsImpl.build(userService.findByUsername(username));
        } catch (ServiceException e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return (RefreshJwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
