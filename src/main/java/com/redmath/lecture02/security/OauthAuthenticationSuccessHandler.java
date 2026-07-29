package com.redmath.lecture02.security;

import com.redmath.lecture02.user.ApiUser;
import com.redmath.lecture02.user.ApiUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OauthAuthenticationSuccessHandler
    implements AuthenticationSuccessHandler {

  private final OauthUserResolver resolver;
  private final ApiUserService userService;
  private final ApiSecurityService jwtService;

  public OauthAuthenticationSuccessHandler(
      OauthUserResolver resolver,
      ApiUserService userService,
      ApiSecurityService jwtService) {

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

    OauthUser oauthUser =
        resolver.resolve(oauth);

    ApiUser apiUser =
        userService.getOrCreateUser(
            oauthUser.principal());

    String jwt =
        jwtService.generateToken(apiUser);

    // Secure httpOnly cookie prevents XSS-based token theft.
    // The token is also passed via a safe, relative redirect URL
    // so the frontend can capture it into localStorage for Bearer auth.
    Cookie tokenCookie = new Cookie("token", jwt);
    tokenCookie.setHttpOnly(true);
    tokenCookie.setSecure(true);
    tokenCookie.setPath("/");
    response.addCookie(tokenCookie);

    String encodedToken = URLEncoder.encode(jwt, StandardCharsets.UTF_8);
    response.sendRedirect("/?token=" + encodedToken);
  }
}
