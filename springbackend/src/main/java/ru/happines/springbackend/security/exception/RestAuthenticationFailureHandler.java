package ru.happines.springbackend.security.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ru.happines.springbackend.exception.ErrorResponseHandler;

@Component
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ErrorResponseHandler errorResponseHandler;

    @Autowired
    public RestAuthenticationFailureHandler(final ErrorResponseHandler errorResponseHandler) {
        this.errorResponseHandler = errorResponseHandler;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) {
        errorResponseHandler.handle(exception, response);
    }
}
