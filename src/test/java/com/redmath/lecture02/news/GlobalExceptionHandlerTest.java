package com.redmath.lecture02.news;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void handleNewsNotFound_returns404() {
    HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getRequestURI()).thenReturn("/api/v1/news/1");

    var response = handler.handleNewsNotFound(new NewsNotFoundException(1L), req);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("News not found with id: 1", response.getBody().message());
  }

  @Test
  void handleBadRequest_returns400() {
    HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getRequestURI()).thenReturn("/api/v1/news");

    var response = handler.handleBadRequest(
        new InvalidNewsRequestException("Title cannot be empty."), req);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Title cannot be empty.", response.getBody().message());
  }

  @Test
  void handleUnexpectedError_returns500() {
    HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getRequestURI()).thenReturn("/api/v1/news");

    var response = handler.handleUnexpectedError(new RuntimeException("Unexpected"), req);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Unexpected", response.getBody().message());
  }

  @Test
  void handleAuthenticationException_returns401() {
    HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getRequestURI()).thenReturn("/api/v1/news");

    var response = handler.handleAuthenticationException(
        new AuthenticationCredentialsNotFoundException("Missing auth"), req);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("/api/v1/news", response.getBody().path());
  }

  @Test
  void handleAuthorizationDenied_returns403() {
    HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getRequestURI()).thenReturn("/api/v1/news");

    var response = handler.handleAuthorizationDenied(new AccessDeniedException("Forbidden"), req);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void handleAuthorizationDenied_withAuthorizationDeniedException_returns403() {
    HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getRequestURI()).thenReturn("/api/v1/news");

    var response = handler.handleAuthorizationDenied(
        new AuthorizationDeniedException("Denied"), req);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void buildErrorResponse_containsAllFields() {
    HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getRequestURI()).thenReturn("/api/v1/news");

    var response = handler.handleUnexpectedError(new RuntimeException("Test error"), req);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    var body = response.getBody();
    assertNotNull(body);
    assertEquals(500, body.status());
    assertEquals("Internal Server Error", body.error());
    assertEquals("Test error", body.message());
    assertEquals("/api/v1/news", body.path());
    assertNotNull(body.timestamp());
  }
}
