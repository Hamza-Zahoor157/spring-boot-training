package com.redmath.lecture02.ai.indexing.loader;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ResourceDocumentLoader {

  public List<Document> load() {

    TextReader reader = new TextReader(
        new ClassPathResource("rag/fintech-company-policy-manual.txt"));

    return reader.get();
  }
}