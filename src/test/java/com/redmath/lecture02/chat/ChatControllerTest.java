package com.redmath.lecture02.chat;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "GEMINI_API_KEY", matches = ".+")
class ChatControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void chat_withMessage_returnsOk() throws Exception {
    mockMvc.perform(get("/api/v1/chat")
            .with(jwt().jwt(jwt -> jwt.subject("Hamza")))
            .param("message", "Hello"))
        .andExpect(status().isOk());
  }

  @Test
  void chat_withDefaultValues_returnsOk() throws Exception {
    mockMvc.perform(get("/api/v1/chat")
            .with(jwt().jwt(jwt -> jwt.subject("Hamza"))))
        .andExpect(status().isOk());
  }

  @Test
  void chat_withCustomConversationId_returnsOk() throws Exception {
    mockMvc.perform(get("/api/v1/chat")
            .with(jwt().jwt(jwt -> jwt.subject("Hamza")))
            .param("conversationId", "test-conv")
            .param("message", "Hi"))
        .andExpect(status().isOk());
  }
}