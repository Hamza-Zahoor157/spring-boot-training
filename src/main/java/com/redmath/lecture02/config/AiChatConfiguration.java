package com.redmath.lecture02.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
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
  TextSplitter textSplitter() {
    return TokenTextSplitter.builder()
        .withChunkSize(500)
        .withMinChunkSizeChars(350)
        .withMinChunkLengthToEmbed(10)
        .withMaxNumChunks(10000)
        .withKeepSeparator(true)
        .build();
  }

  @Bean
  ChatClient chatClient(ChatModel chatModel, ChatMemory chatMemory, VectorStore vectorStore) {
    return ChatClient.builder(chatModel)
        .defaultAdvisors(
            MessageChatMemoryAdvisor.builder(chatMemory).build(),
            QuestionAnswerAdvisor.builder(vectorStore).build()
        )
        .build();
  }
}
