package com.redmath.lecture02.welcome;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class WelcomeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void welcome_returnsWelcomeMessage() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/welcome")
            .with(jwt().jwt(jwt -> jwt.subject("Hamza"))))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Welcome to RedMath"));
  }
}
