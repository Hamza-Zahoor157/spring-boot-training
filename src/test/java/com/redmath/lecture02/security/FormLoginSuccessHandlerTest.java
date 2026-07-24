package com.redmath.lecture02.security;

import com.redmath.lecture02.user.ApiUser;
import com.redmath.lecture02.user.ApiUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormLoginSuccessHandlerTest {

  @Test
  void onAuthenticationSuccess_returnsJsonWithToken() throws Exception {
    ApiSecurityService apiSecurityService = mock(ApiSecurityService.class);
    ApiUserService userService = mock(ApiUserService.class);
    FormLoginSuccessHandler handler = new FormLoginSuccessHandler(apiSecurityService, userService);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("testuser");

    ApiUser apiUser = new ApiUser();
    apiUser.setUserName("testuser");
    when(userService.getOrCreateUser("testuser")).thenReturn(apiUser);
    when(apiSecurityService.generateToken(apiUser)).thenReturn("test-token");

    MockHttpServletResponse response = new MockHttpServletResponse();
    HttpServletRequest request = mock(HttpServletRequest.class);

    handler.onAuthenticationSuccess(request, response, authentication);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("application/json;charset=UTF-8", response.getContentType());
    assertTrue(response.getContentAsString().contains("test-token"));
    assertTrue(response.getContentAsString().contains("\"token\""));
  }
}
