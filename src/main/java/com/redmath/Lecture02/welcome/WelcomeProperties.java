package com.redmath.Lecture02.welcome;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.message")
public record WelcomeProperties(String welcome) {
}
