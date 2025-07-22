package ru.happines.springbackend.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class SkipPathRequestMatcher implements RequestMatcher {

    private final OrRequestMatcher matchers;

    public SkipPathRequestMatcher(final List<String> pathsToSkip) {
        Assert.notNull(pathsToSkip, "List of paths to skip is required.");

        List<RequestMatcher> m = pathsToSkip.stream()
                .map(path -> (RequestMatcher) request -> {
                    String uri = request.getRequestURI();
                    return uri.equals(path) || uri.startsWith(path + "/");
                })
                .collect(Collectors.toList());

        matchers = new OrRequestMatcher(m);
    }

    @Override
    public boolean matches(final HttpServletRequest request) {
        return !matchers.matches(request);
    }
}
