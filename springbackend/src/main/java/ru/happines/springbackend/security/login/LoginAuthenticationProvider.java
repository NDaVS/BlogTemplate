package ru.happines.springbackend.security.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.service.Implementation.UserDetailsImpl;
import ru.happines.springbackend.service.UserService;

@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Autowired
    public LoginAuthenticationProvider(UserService userService) {
        this.userService = userService;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");
        String username = (String) authentication.getPrincipal();
        String password = authentication.getCredentials().toString();
        UserDetails securityUser = authenticateByUsernameAndPassword(username, password);
        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    private UserDetails authenticateByUsernameAndPassword(String username, String password) {
        User user = getUser(username);
        if (!encoder.matches(password, user.getHashedPassword())) {
            throw new BadCredentialsException("exception.badCredentials2");
        }
        return UserDetailsImpl.build(user);
    }

    private User getUser(String username) {
        try {
            return userService.findByUsername(username);
        } catch (ServiceException e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
