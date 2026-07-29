package com.redmath.lecture02.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

  @Autowired
  private EmailService emailService;

  @Test
  void sendEmail_validEmail_doesNotThrow() {
    emailService.sendEmail("test@example.com");
  }

  @Test
  void sendEmail_nullEmail_doesNotThrow() {
    emailService.sendEmail(null);
  }

  @Test
  void sendEmail_emptyEmail_doesNotThrow() {
    emailService.sendEmail("");
  }
}