package com.redmath.lecture02.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AsyncController {

  private final EmailService emailService;

  public AsyncController(EmailService emailService) {
    this.emailService = emailService;
  }

  @GetMapping("/api/v1/send-email")
  public String sendEmail() {

    log.info("Request Thread : {}", Thread.currentThread().getName());

    emailService.sendEmail("hamza@test.com");

    return "Email request accepted.";

  }

}