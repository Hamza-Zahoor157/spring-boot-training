package com.redmath.Lecture02.security;

import com.redmath.Lecture02.user.ApiUser;
import com.redmath.Lecture02.user.ApiUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuthAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final OAuthUserResolver resolver;
    private final ApiUserService userService;
    private final JwtService jwtService;

    public OAuthAuthenticationSuccessHandler(
            OAuthUserResolver resolver,
            ApiUserService userService,
            JwtService jwtService) {

        this.resolver = resolver;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken oauth =
                (OAuth2AuthenticationToken) authentication;

        OAuthUser oauthUser =
                resolver.resolve(oauth);

        ApiUser apiUser =
                userService.getOrCreateUser(
                        oauthUser.principal());

        String jwt =
                jwtService.generateToken(apiUser);

        response.sendRedirect(
                "/?token=" +
                        URLEncoder.encode(
                                jwt,
                                StandardCharsets.UTF_8));
    }
}