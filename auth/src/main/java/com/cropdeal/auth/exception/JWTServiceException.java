package com.cropdeal.auth.exception;

public class JWTServiceException extends RuntimeException {
    public JWTServiceException(String message) {
        super(message);
    }
}