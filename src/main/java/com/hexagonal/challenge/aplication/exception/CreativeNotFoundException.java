package com.hexagonal.challenge.aplication.exception;

public class CreativeNotFoundException extends RuntimeException {
    public CreativeNotFoundException(String message) {
        super(message);
    }
}
