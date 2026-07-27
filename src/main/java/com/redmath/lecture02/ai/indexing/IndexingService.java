package com.redmath.lecture02.ai.indexing;

import com.redmath.lecture02.ai.model.IndexingResult;
import org.springframework.stereotype.Service;

@Service
public class IndexingService {

  private final KnowledgeBaseIndexer knowledgeBaseIndexer;

  public IndexingService(KnowledgeBaseIndexer knowledgeBaseIndexer) {
    this.knowledgeBaseIndexer = knowledgeBaseIndexer;
  }

  public IndexingResult rebuildKnowledgeBase() {
    return knowledgeBaseIndexer.index();
  }
}