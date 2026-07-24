package com.redmath.lecture02.chat;

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
class SimpleChatControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldReturnChatResponse() throws Exception {

    mockMvc.perform(get("/api/v1/chat-model")
            .with(jwt().jwt(jwt -> jwt.subject("Hamza")))
            .param("message", "Hello"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturnChatResponseWithDefaultMessage() throws Exception {

    mockMvc.perform(get("/api/v1/chat-model")
            .with(jwt().jwt(jwt -> jwt.subject("Hamza"))))
        .andExpect(status().isOk());
  }
}
