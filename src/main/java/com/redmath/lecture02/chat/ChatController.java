package com.redmath.lecture02.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

  private final ChatClient chatClient;

  public ChatController(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/api/v1/chat")
  public String chat(
      @RequestParam(defaultValue = "default") String conversationId,
      @RequestParam(defaultValue = "Hello") String message) {

    return chatClient
        .prompt(message)
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
        .call()
        .content();
  }
}