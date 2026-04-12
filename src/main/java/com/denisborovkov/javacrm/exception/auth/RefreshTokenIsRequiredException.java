package com.denisborovkov.javacrm.exception.auth;

public class RefreshTokenIsRequiredException extends RuntimeException {
    public RefreshTokenIsRequiredException(String message) {
        super(message);
    }
}
