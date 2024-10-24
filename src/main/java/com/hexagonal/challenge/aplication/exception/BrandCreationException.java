package com.hexagonal.challenge.aplication.exception;

public class BrandCreationException extends RuntimeException {
    public BrandCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}