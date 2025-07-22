package ru.happines.springbackend.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import ru.happines.springbackend.security.jwt.RefreshTokenAuthenticationProvider;
import ru.happines.springbackend.security.jwt.TokenAuthenticationProvider;
import ru.happines.springbackend.security.login.LoginAuthenticationProvider;

@Configuration
public class AuthenticationManagerConfiguration {

    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    private final LoginAuthenticationProvider loginAuthenticationProvider;

    private final RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider;

    public AuthenticationManagerConfiguration(final TokenAuthenticationProvider tokenAuthenticationProvider,
                                              final LoginAuthenticationProvider loginAuthenticationProvider,
                                              final RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider) {
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
        this.loginAuthenticationProvider = loginAuthenticationProvider;
        this.refreshTokenAuthenticationProvider = refreshTokenAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(final ObjectPostProcessor<Object> objectPostProcessor) throws Exception {
        var auth = new AuthenticationManagerBuilder(objectPostProcessor);
        auth.authenticationProvider(loginAuthenticationProvider);
        auth.authenticationProvider(tokenAuthenticationProvider);
        auth.authenticationProvider(refreshTokenAuthenticationProvider);
        return auth.build();
    }
}