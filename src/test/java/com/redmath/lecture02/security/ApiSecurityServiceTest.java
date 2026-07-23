package com.redmath.lecture02.security;

import com.redmath.lecture02.user.ApiUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiSecurityServiceTest {

  @Test
  void generateToken_returnsValidJwt() throws Exception {
    ApiSecurityService service = new ApiSecurityService();
    service.afterPropertiesSet();

    ApiUser user = new ApiUser();
    user.setUserName("testuser");
    user.setRoles("EDITOR");

    String token = service.generateToken(user);

    assertNotNull(token);
    assertEquals(3, token.split("\\.").length);

    var jwt = service.jwtDecoder().decode(token);
    assertEquals("testuser", jwt.getSubject());
  }

  @Test
  void jwtDecoder_returnsDecoderAfterInit() throws Exception {
    ApiSecurityService service = new ApiSecurityService();
    service.afterPropertiesSet();

    assertNotNull(service.jwtDecoder());
  }
}
