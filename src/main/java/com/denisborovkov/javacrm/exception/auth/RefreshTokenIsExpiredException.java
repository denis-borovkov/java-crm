package com.denisborovkov.javacrm.exception.auth;

public class RefreshTokenIsExpiredException extends RuntimeException {
    public RefreshTokenIsExpiredException(String message) {
        super(message);
    }
}
