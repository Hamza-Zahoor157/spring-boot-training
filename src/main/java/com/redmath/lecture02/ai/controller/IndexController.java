package com.redmath.lecture02.ai.controller;

import com.redmath.lecture02.ai.indexing.IndexingService;
import com.redmath.lecture02.ai.model.IndexingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class IndexController {

  private final IndexingService indexingService;

  public IndexController(IndexingService indexingService) {
    this.indexingService = indexingService;
  }

  @PostMapping("/index-phones")
  public ResponseEntity<IndexingResult> indexPhoneDetails() {

    IndexingResult result = indexingService.indexPhoneDetails();

    return ResponseEntity.ok(result);
  }
}
