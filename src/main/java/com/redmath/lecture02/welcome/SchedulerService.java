package com.redmath.lecture02.welcome;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerService {

  @Scheduled(fixedRate = 5000)
  public void printCurrentTime() {

    log.info("Scheduler executed at : {}", LocalDateTime.now());
  }

}