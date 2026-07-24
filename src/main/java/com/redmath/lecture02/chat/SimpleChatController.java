package com.redmath.lecture02.chat;

import java.util.Objects;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleChatController {

  private final ChatModel chatModel;

  public SimpleChatController(ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  @GetMapping("/api/v1/chat-model")
  public String chat(@RequestParam(defaultValue = "Hello") String message) {

    Prompt prompt = new Prompt(message);
    ChatResponse response = chatModel.call(prompt);
    return Objects.requireNonNull(response.getResult()).getOutput().getText();
  }
}
