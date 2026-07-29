package com.redmath.lecture02.ai.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import org.junit.jupiter.api.Test;

class ChunkResultTest {

  @Test
  void chunkResult_constructor_returnsCorrectValues() {
    Map<String, Object> metadata = Map.of("key", "value");
    ChunkResult result = new ChunkResult("text content", metadata);

    assertEquals("text content", result.text());
    assertEquals(metadata, result.metadata());
    assertNotNull(result.text());
    assertNotNull(result.metadata());
  }

  @Test
  void chunkResult_emptyMetadata_returnsEmptyMap() {
    ChunkResult result = new ChunkResult("text content", Map.of());

    assertEquals("text content", result.text());
    assertEquals(0, result.metadata().size());
  }

  @Test
  void chunkResult_nullText_returnsNullText() {
    ChunkResult result = new ChunkResult(null, Map.of("key", "val"));

    assertEquals(null, result.text());
    assertEquals(1, result.metadata().size());
  }
}