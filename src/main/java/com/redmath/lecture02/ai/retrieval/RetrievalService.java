package com.redmath.lecture02.ai.retrieval;

import com.redmath.lecture02.ai.model.ChunkResult;
import com.redmath.lecture02.ai.model.RetrievalResult;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RetrievalService {

  private final VectorStore vectorStore;

  public RetrievalService(VectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  public RetrievalResult search(String question) {
    SearchRequest request =
        SearchRequest.builder()
            .query(question)
            .topK(4)
            .similarityThreshold(0.0)
            .build();

    List<Document> documents =
        vectorStore.similaritySearch(request);

    List<ChunkResult> chunks =
        documents.stream()
            .map(document ->
                new ChunkResult(
                    document.getText(),
                    document.getMetadata()
                ))
            .toList();

    return new RetrievalResult(
        question,
        chunks.size(),
        chunks
    );
  }
}
