package com.redmath.lecture02.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiChatConfiguration {

  @Bean
  ChatMemory chatMemory() {
    return MessageWindowChatMemory.builder()
        .maxMessages(20)
        .build();
  }

  @Bean
  ChatClient chatClient(ChatModel chatModel, ChatMemory chatMemory) {

    return ChatClient.builder(chatModel)
        .defaultAdvisors(
            MessageChatMemoryAdvisor.builder(chatMemory).build()
        )
        .build();
  }
}