package com.cropdeal.auth.exception;

public class AuthServiceException extends RuntimeException {
    public AuthServiceException(String message) {
        super(message);
    }
}