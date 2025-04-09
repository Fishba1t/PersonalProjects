package com.example.demo.Exceptions;

public class InvalidEventTimeException extends RuntimeException {
    public InvalidEventTimeException(String message) {
        super(message);
    }
}
