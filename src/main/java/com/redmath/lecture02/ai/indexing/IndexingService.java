package com.redmath.lecture02.ai.indexing;

import com.redmath.lecture02.ai.model.IndexingResult;
import org.springframework.stereotype.Service;

@Service
public class IndexingService {

  private final PhoneDetailsIndexer phoneDetailsIndexer;

  public IndexingService(PhoneDetailsIndexer phoneDetailsIndexer) {
    this.phoneDetailsIndexer = phoneDetailsIndexer;
  }

  public IndexingResult indexPhoneDetails() {
    return phoneDetailsIndexer.index();
  }
}
