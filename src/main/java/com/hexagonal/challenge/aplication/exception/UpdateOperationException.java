package com.hexagonal.challenge.aplication.exception;

public class UpdateOperationException extends RuntimeException {
    public UpdateOperationException(String message) {
        super(message);
    }
}