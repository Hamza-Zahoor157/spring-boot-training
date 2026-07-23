package com.redmath.lecture02.news;

public class NewsNotFoundException extends RuntimeException {

  public NewsNotFoundException(Long newsId) {
    super("News not found with id: " + newsId);
  }
}
