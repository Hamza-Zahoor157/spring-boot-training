package com.redmath.lecture02.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

  @Async
  public void sendEmail(String email) {

    log.info("Started sending email...");
    log.info("Thread : {}", Thread.currentThread().getName());

    try {
      Thread.sleep(5000);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    log.info("Email sent to {}", email);

  }

}