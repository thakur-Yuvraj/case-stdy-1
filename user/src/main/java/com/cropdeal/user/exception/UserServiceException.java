package com.cropdeal.user.exception;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message) {
        super(message);
    }
}