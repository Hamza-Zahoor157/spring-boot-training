package com.redmath.lecture02.ai.model;

public record IndexingResult(
    String status,
    int documentsRead,
    int chunksCreated,
    long durationInMillis
) {
}