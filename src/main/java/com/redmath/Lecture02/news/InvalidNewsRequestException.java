package com.redmath.Lecture02.news;

public class InvalidNewsRequestException extends RuntimeException {

    public InvalidNewsRequestException(String message) {
        super(message);
    }
}
