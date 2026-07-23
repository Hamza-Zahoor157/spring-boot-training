package com.redmath.lecture02.news.dto;

import java.time.LocalDateTime;

public record NewsResponse(
    Long newsId,
    String title,
    String details,
    String reportedBy,
    LocalDateTime reportedAt) {

}
