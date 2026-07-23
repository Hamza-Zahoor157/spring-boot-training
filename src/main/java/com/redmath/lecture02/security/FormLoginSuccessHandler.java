package com.redmath.lecture02.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redmath.lecture02.user.ApiUser;
import com.redmath.lecture02.user.ApiUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class FormLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ApiSecurityService apiSecurityService;
  private final ApiUserService userService;

  public FormLoginSuccessHandler(ApiSecurityService apiSecurityService,
      ApiUserService userService) {
    this.apiSecurityService = apiSecurityService;
    this.userService = userService;
  }

  @Override
  public void onAuthenticationSuccess(
      @NonNull HttpServletRequest request,
      HttpServletResponse response,
      @NonNull Authentication authentication)
      throws IOException {

    String username = authentication.getName();
    ApiUser apiUser = userService.getOrCreateUser(username);
    String token = apiSecurityService.generateToken(apiUser);

    response.setContentType("application/json");
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    ObjectMapper objectMapper = new ObjectMapper();
    byte[] jsonBytes = objectMapper.writeValueAsString(
        Map.of("token", token)).getBytes(StandardCharsets.UTF_8);
    response.getOutputStream().write(jsonBytes);
  }
}
