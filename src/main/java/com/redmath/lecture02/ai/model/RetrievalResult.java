package com.redmath.lecture02.ai.model;

import java.util.List;

public record RetrievalResult(

    String query,

    int resultsFound,

    List<ChunkResult> chunks

) {
}