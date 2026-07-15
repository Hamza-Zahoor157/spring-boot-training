package com.redmath.Lecture02.news;

public class InvalidNewsIdException extends RuntimeException {

    public InvalidNewsIdException(String message) {
        super(message);
    }
}
