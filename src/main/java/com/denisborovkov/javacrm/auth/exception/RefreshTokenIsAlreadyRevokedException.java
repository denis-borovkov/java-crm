package com.denisborovkov.javacrm.auth.exception;

public class RefreshTokenIsAlreadyRevokedException extends RuntimeException {
    public RefreshTokenIsAlreadyRevokedException(String message) {
        super(message);
    }
}



