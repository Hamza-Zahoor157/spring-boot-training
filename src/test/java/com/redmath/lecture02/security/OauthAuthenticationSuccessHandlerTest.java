package com.redmath.lecture02.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redmath.lecture02.user.ApiUser;
import com.redmath.lecture02.user.ApiUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

class OauthAuthenticationSuccessHandlerTest {

  @Test
  void onAuthenticationSuccess_writesHtmlWithToken() throws Exception {
    ApiSecurityService jwtService = mock(ApiSecurityService.class);
    ApiUserService userService = mock(ApiUserService.class);
    OauthUserResolver resolver = mock(OauthUserResolver.class);
    OauthAuthenticationSuccessHandler handler = new OauthAuthenticationSuccessHandler(
        resolver, userService, jwtService);

    OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
    when(authentication.getName()).thenReturn("hamza");
    org.springframework.security.oauth2.core.user.OAuth2User principal = mock(org.springframework.security.oauth2.core.user.OAuth2User.class);
    when(authentication.getPrincipal()).thenReturn(principal);
    when(authentication.getAuthorizedClientRegistrationId()).thenReturn("google");
    when(resolver.resolve(authentication)).thenReturn(new OauthUser("google", "hamza@example.com", "hamza@example.com", "Hamza"));

    ApiUser apiUser = new ApiUser();
    apiUser.setUserName("hamza");
    when(userService.getOrCreateUser("hamza@example.com")).thenReturn(apiUser);
    when(jwtService.generateToken(apiUser)).thenReturn("test-jwt-token");

    HttpServletRequest request = mock(HttpServletRequest.class);
    MockHttpServletResponse response = new MockHttpServletResponse();

    handler.onAuthenticationSuccess(request, response, authentication);

    assertEquals(HttpStatus.FOUND.value(), response.getStatus());
    assertEquals("/?token=test-jwt-token", response.getHeader("Location"));
  }
}
