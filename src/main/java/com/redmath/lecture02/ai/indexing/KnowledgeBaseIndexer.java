package com.redmath.lecture02.ai.indexing;

import com.redmath.lecture02.ai.model.IndexingResult;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.redmath.lecture02.ai.indexing.loader.ResourceDocumentLoader;

@Service
public class KnowledgeBaseIndexer {

  private final ResourceDocumentLoader documentLoader;
  private final TextSplitter textSplitter;
  private final VectorStore vectorStore;
  public static final String DOCUMENT_ID = "policy-manual";


  public KnowledgeBaseIndexer(ResourceDocumentLoader documentLoader,
      TextSplitter textSplitter,
      VectorStore vectorStore) {

    this.documentLoader = documentLoader;
    this.textSplitter = textSplitter;
    this.vectorStore = vectorStore;
  }

  public IndexingResult index() {

    long start = System.currentTimeMillis();

    deleteExistingChunks();

    List<Document> documents = documentLoader.load();

    List<Document> chunks = textSplitter.apply(documents);

    addMetadata(chunks);

    vectorStore.add(chunks);

    long end = System.currentTimeMillis();

    return new IndexingResult(
        "SUCCESS",
        documents.size(),
        chunks.size(),
        end - start
    );
  }

  private void addMetadata(List<Document> chunks) {

    for (Document chunk : chunks) {
      chunk.getMetadata().put("documentId", DOCUMENT_ID);
    }

  }

  private void deleteExistingChunks() {

    vectorStore.delete("documentId == 'policy-manual'");

  }
}