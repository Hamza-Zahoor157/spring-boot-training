package com.redmath.lecture02.ai.indexing;

import com.redmath.lecture02.ai.model.IndexingResult;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class PhoneDetailsIndexer {

  public static final String DOCUMENT_ID = "phone-details";
  private final TextSplitter textSplitter;
  private final VectorStore vectorStore;

  public PhoneDetailsIndexer(
      TextSplitter textSplitter,
      VectorStore vectorStore) {
    this.textSplitter = textSplitter;
    this.vectorStore = vectorStore;
  }

  public IndexingResult index() {
    long start = System.currentTimeMillis();

    deleteExistingChunks();

    JsonReader jsonReader = new JsonReader(new ClassPathResource("phone-details.json"));
    List<Document> documents = jsonReader.get();

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
    vectorStore.delete("documentId == 'phone-details'");
  }
}
