package com.redmath.Lecture02.security;

import com.redmath.Lecture02.user.ApiUser;
import com.redmath.Lecture02.user.ApiUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Component
public class FormLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ApiSecurityService apiSecurityService;
    private final ApiUserService userService;

    public FormLoginSuccessHandler(ApiSecurityService apiSecurityService, ApiUserService userService) {
        this.apiSecurityService = apiSecurityService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull Authentication authentication)
            throws IOException{

        String username = authentication.getName();
        ApiUser apiUser = userService.getOrCreateUser(username);
        String token = apiSecurityService.generateToken(apiUser);

        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        writer.write("{\"token\":\"" + token + "\"}");
        writer.flush();
    }
}
