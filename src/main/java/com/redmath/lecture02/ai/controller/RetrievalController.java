package com.redmath.lecture02.ai.controller;

import com.redmath.lecture02.ai.model.RetrievalResult;
import com.redmath.lecture02.ai.retrieval.RetrievalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetrievalController {

  private final RetrievalService retrievalService;

  public RetrievalController(RetrievalService retrievalService) {
    this.retrievalService = retrievalService;
  }

  @GetMapping("/api/v1/ai/retrieve")
  public ResponseEntity<RetrievalResult> retrieve(

      @RequestParam String question

  ) {

    return ResponseEntity.ok(
        retrievalService.search(question)
    );

  }
}