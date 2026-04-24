package com.denisborovkov.javacrm.auth.exception;

public class RefreshTokenIsExpiredException extends RuntimeException {
    public RefreshTokenIsExpiredException(String message) {
        super(message);
    }
}



