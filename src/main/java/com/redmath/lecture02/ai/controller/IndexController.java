package com.redmath.lecture02.ai.controller;

import com.redmath.lecture02.ai.indexing.IndexingService;
import com.redmath.lecture02.ai.model.IndexingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  private final IndexingService indexingService;

  public IndexController(IndexingService indexingService) {
    this.indexingService = indexingService;
  }

  @PostMapping("/api/v1/ai/index")
  public ResponseEntity<IndexingResult> indexKnowledgeBase() {

    IndexingResult result = indexingService.rebuildKnowledgeBase();

    return ResponseEntity.ok(result);
  }
}