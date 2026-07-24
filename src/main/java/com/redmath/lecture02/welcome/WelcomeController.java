package com.redmath.lecture02.welcome;

//import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

  private final WelcomeProperties welcomeProperties;

  public WelcomeController(WelcomeProperties welcomeProperties) {
    this.welcomeProperties = welcomeProperties;
  }

  @GetMapping("/api/v1/welcome")
  public String welcome() {
    return welcomeProperties.welcome();
  }
}
