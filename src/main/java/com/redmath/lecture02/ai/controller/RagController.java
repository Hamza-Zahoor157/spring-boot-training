package com.redmath.lecture02.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class RagController {

  private final ChatModel chatModel;

  private final VectorStore vectorStore;

  public RagController(ChatModel chatModel,
      VectorStore vectorStore) {

    this.chatModel = chatModel;
    this.vectorStore = vectorStore;
  }

  @GetMapping("/rag")
  public String ask(

      @RequestParam String question

  ) {

    return ChatClient.builder(chatModel)
        .build()
        .prompt()
        .user(question)
        .advisors(
            QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(
                    SearchRequest.builder()
                        .query(question)
                        .topK(4)
                        .build()
                )
                .build()
        )
        .call()
        .content();

  }
}
