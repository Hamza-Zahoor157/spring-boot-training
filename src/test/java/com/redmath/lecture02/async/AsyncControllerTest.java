package com.redmath.lecture02.async;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AsyncControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void sendEmail_returnsOk() throws Exception {
    mockMvc.perform(get("/api/v1/send-email")
            .with(jwt().jwt(jwt -> jwt.subject("Hamza"))))
        .andExpect(status().isOk());
  }
}