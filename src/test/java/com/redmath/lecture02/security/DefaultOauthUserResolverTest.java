package com.redmath.lecture02.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

class DefaultOauthUserResolverTest {

  private final DefaultOauthUserResolver resolver = new DefaultOauthUserResolver();

  @Test
  void resolveGoogle_returnsOauthUser() {
    OAuth2User user = mock(OAuth2User.class);
    when(user.getAttribute("email")).thenReturn("hamza@example.com");
    when(user.getAttribute("name")).thenReturn("Hamza");

    OAuth2AuthenticationToken token = mock(OAuth2AuthenticationToken.class);
    when(token.getAuthorizedClientRegistrationId()).thenReturn("google");
    when(token.getPrincipal()).thenReturn(user);

    OauthUser result = resolver.resolve(token);

    assertNotNull(result);
    assertEquals("google", result.provider());
    assertEquals("hamza@example.com", result.email());
    assertEquals("hamza@example.com", result.principal());
    assertEquals("Hamza", result.displayName());
  }

  @Test
  void resolveGithub_withEmail_returnsOauthUser() {
    OAuth2User user = mock(OAuth2User.class);
    when(user.getAttribute("login")).thenReturn("hamza");
    when(user.getAttribute("email")).thenReturn("hamza@example.com");
    when(user.getAttribute("name")).thenReturn("Hamza");

    OAuth2AuthenticationToken token = mock(OAuth2AuthenticationToken.class);
    when(token.getAuthorizedClientRegistrationId()).thenReturn("github");
    when(token.getPrincipal()).thenReturn(user);

    OauthUser result = resolver.resolve(token);

    assertNotNull(result);
    assertEquals("github", result.provider());
    assertEquals("hamza", result.principal());
    assertEquals("hamza@example.com", result.email());
    assertEquals("Hamza", result.displayName());
  }

  @Test
  void resolveGithub_withNullEmail_usesLoginAsPrincipal() {
    OAuth2User user = mock(OAuth2User.class);
    when(user.getAttribute("login")).thenReturn("hamza");
    when(user.getAttribute("email")).thenReturn(null);
    when(user.getAttribute("name")).thenReturn("Hamza");

    OAuth2AuthenticationToken token = mock(OAuth2AuthenticationToken.class);
    when(token.getAuthorizedClientRegistrationId()).thenReturn("github");
    when(token.getPrincipal()).thenReturn(user);

    OauthUser result = resolver.resolve(token);

    assertNotNull(result);
    assertEquals("github", result.provider());
    assertEquals("hamza", result.principal());
    assertEquals("hamza", result.email());
  }

  @Test
  void resolve_unsupportedProvider_throwsException() {
    OAuth2AuthenticationToken token = mock(OAuth2AuthenticationToken.class);
    when(token.getAuthorizedClientRegistrationId()).thenReturn("unknown");

    assertThrows(IllegalArgumentException.class, () -> resolver.resolve(token));
  }
}
