package com.denisborovkov.javacrm.exception.auth;

public class RefreshTokenIsAlreadyRevokedException extends RuntimeException {
    public RefreshTokenIsAlreadyRevokedException(String message) {
        super(message);
    }
}
