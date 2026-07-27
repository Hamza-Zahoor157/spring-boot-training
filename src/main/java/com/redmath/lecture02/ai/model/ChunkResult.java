package com.redmath.lecture02.ai.model;

import java.util.Map;

public record ChunkResult(

    String text,

    Map<String, Object> metadata

) {
}