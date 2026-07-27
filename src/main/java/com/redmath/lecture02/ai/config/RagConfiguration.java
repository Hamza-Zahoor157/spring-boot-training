package com.redmath.lecture02.ai.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfiguration {

  @Bean
  public TextSplitter textSplitter() {

    return TokenTextSplitter.builder()
        .withChunkSize(500)
        .withMinChunkSizeChars(350)
        .withMinChunkLengthToEmbed(10)
        .withMaxNumChunks(10000)
        .withKeepSeparator(true)
        .build();
  }
  @Bean
  public VectorStore vectorStore(EmbeddingModel embeddingModel) {
    return SimpleVectorStore.builder(embeddingModel)
        .build();
  }
}