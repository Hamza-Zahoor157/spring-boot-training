package com.redmath.lecture02.news;

public class InvalidNewsIdException extends RuntimeException {

  public InvalidNewsIdException(String message) {
    super(message);
  }
}
