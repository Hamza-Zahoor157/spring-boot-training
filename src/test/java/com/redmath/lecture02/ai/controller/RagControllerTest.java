package com.redmath.lecture02.ai.controller;

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
class RagControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void ask_withQuestion_returnsOk() throws Exception {
    mockMvc.perform(get("/api/v1/ai/rag")
            .with(jwt().jwt(jwt -> jwt.subject("Hamza")))
            .param("question", "What is the company policy?"))
        .andExpect(status().isOk());
  }

  @Test
  void ask_withDefaultQuestion_returnsOk() throws Exception {
    mockMvc.perform(get("/api/v1/ai/rag")
            .with(jwt().jwt(jwt -> jwt.subject("Hamza")))
            .param("question", "default"))
        .andExpect(status().isOk());
  }
}