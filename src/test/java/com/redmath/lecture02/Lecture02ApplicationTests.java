package com.redmath.lecture02;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@SpringBootTest
class Lecture02ApplicationTests {


  @Autowired
  private NimbusJwtDecoder jwtDecoder;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void contextLoads() {
    assertNotNull(jwtDecoder);
    assertNotNull(passwordEncoder);
  }

}
