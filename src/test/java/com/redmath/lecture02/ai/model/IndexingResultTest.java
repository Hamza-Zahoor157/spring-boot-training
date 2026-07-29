package com.redmath.lecture02.ai.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class IndexingResultTest {

  @Test
  void indexingResult_constructor_returnsCorrectValues() {
    IndexingResult result = new IndexingResult("SUCCESS", 5, 10, 1500L);

    assertEquals("SUCCESS", result.status());
    assertEquals(5, result.documentsRead());
    assertEquals(10, result.chunksCreated());
    assertEquals(1500L, result.durationInMillis());
    assertNotNull(result.status());
  }

  @Test
  void indexingResult_emptyStatus_returnsEmptyString() {
    IndexingResult result = new IndexingResult("", 0, 0, 0L);

    assertEquals("", result.status());
    assertEquals(0, result.documentsRead());
    assertEquals(0, result.chunksCreated());
    assertEquals(0L, result.durationInMillis());
  }

  @Test
  void indexingResult_failureStatus_returnsCorrectValues() {
    IndexingResult result = new IndexingResult("FAILURE", 3, 5, 500L);

    assertEquals("FAILURE", result.status());
    assertEquals(3, result.documentsRead());
    assertEquals(5, result.chunksCreated());
    assertEquals(500L, result.durationInMillis());
  }
}