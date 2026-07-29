package com.redmath.lecture02.ai.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RetrievalResultTest {

  @Test
  void retrievalResult_constructor_returnsCorrectValues() {
    ChunkResult chunk = new ChunkResult("chunk text", Map.of("doc", "doc1"));
    RetrievalResult result = new RetrievalResult("test query", 1, List.of(chunk));

    assertEquals("test query", result.query());
    assertEquals(1, result.resultsFound());
    assertNotNull(result.chunks());
    assertEquals(1, result.chunks().size());
  }

  @Test
  void retrievalResult_emptyChunks_returnsZeroResults() {
    RetrievalResult result = new RetrievalResult("query", 0, List.of());

    assertEquals("query", result.query());
    assertEquals(0, result.resultsFound());
    assertEquals(0, result.chunks().size());
  }

  @Test
  void retrievalResult_multipleChunks_returnsCorrectCount() {
    ChunkResult c1 = new ChunkResult("text1", Map.of());
    ChunkResult c2 = new ChunkResult("text2", Map.of());
    RetrievalResult result = new RetrievalResult("multi", 2, List.of(c1, c2));

    assertEquals("multi", result.query());
    assertEquals(2, result.resultsFound());
    assertEquals(2, result.chunks().size());
  }
}