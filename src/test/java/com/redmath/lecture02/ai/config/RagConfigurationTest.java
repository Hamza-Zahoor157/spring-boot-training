package com.redmath.lecture02.ai.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RagConfigurationTest {

  @Autowired
  private RagConfiguration ragConfiguration;

  @Test
  void textSplitter_beanIsCreated() {
    assertNotNull(ragConfiguration);
  }

  @Test
  void contextLoads_withTextSplitterBean() {
    TextSplitter splitter = new RagConfiguration().textSplitter();
    assertNotNull(splitter);
    assertTrue(splitter instanceof org.springframework.ai.transformer.splitter.TokenTextSplitter);
  }
}