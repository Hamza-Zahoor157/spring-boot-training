package com.redmath.lecture02.ai.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "GEMINI_API_KEY", matches = ".+")
class IndexControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void indexKnowledgeBase_returnsOk() throws Exception {
    mockMvc.perform(post("/api/v1/ai/index")
            .with(jwt().jwt(jwt -> jwt.subject("Hamza"))))
        .andExpect(status().isOk());
  }
}