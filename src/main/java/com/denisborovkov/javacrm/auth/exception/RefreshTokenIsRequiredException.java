package com.denisborovkov.javacrm.auth.exception;

public class RefreshTokenIsRequiredException extends RuntimeException {
    public RefreshTokenIsRequiredException(String message) {
        super(message);
    }
}



