package ru.happines.springbackend.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.exception.ErrorCode;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.security.jwt.JwtPair;
import ru.happines.springbackend.security.jwt.JwtTokenProvider;
import ru.happines.springbackend.service.Implementation.UserDetailsImpl;
import ru.happines.springbackend.service.UserService;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@Component(value = "oauth2AuthenticationSuccessHandler")
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String LOGIN_URL = "http://localhost:8080/login";
    public static final String EMAIL_ATTRIBUTE = "email";
    public static final String GIVEN_NAME_ATTRIBUTE = "given_name";
    private final JwtTokenProvider tokenProvider;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    private final UserService userService;

    public Oauth2AuthenticationSuccessHandler(final JwtTokenProvider tokenProvider,
                                              final OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                                              final UserService userService) {
        this.tokenProvider = tokenProvider;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String email = token.getPrincipal().getAttribute(EMAIL_ATTRIBUTE);
        User user = getUser(token, email);
        JwtPair jwtPair = tokenProvider.generateTokenPair(UserDetailsImpl.build(user));
        getRedirectStrategy().sendRedirect(request, response, getRedirectUrl(LOGIN_URL, jwtPair));
    }

    private User getUser(OAuth2AuthenticationToken token, String email) throws AuthenticationException {
        boolean existsByEmail = userService.existsByEmail(email);
        try {
            if (existsByEmail) {
                return userService.findByEmail(email);
            }
            return userService.create(new CreateUserDTO(email, email, email, token.getPrincipal().getAttribute(GIVEN_NAME_ATTRIBUTE), token.getPrincipal().getAttribute("family_name"), email));
        } catch (ServiceException e) {
            throw new AuthenticationException(ErrorCode.GENERAL.name(), new ServiceException(ErrorCode.GENERAL, "User with email " + email + " already exists"));
        }
    }

    String getRedirectUrl(final String baseUrl, final JwtPair tokenPair) {
        String url = baseUrl;
        if (baseUrl.indexOf("?") > 0) {
            url += "&";
        } else {
            url += "/?";
        }
        return url + "accessToken=" + tokenPair.getToken() + "&refreshToken=" + tokenPair.getRefreshToken();
    }
}
