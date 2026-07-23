package com.redmath.lecture02.news;

public class InvalidNewsRequestException extends RuntimeException {

  public InvalidNewsRequestException(String message) {
    super(message);
  }
}
