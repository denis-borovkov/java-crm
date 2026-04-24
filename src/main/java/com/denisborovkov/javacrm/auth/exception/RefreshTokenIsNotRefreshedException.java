package com.denisborovkov.javacrm.auth.exception;

public class RefreshTokenIsNotRefreshedException extends RuntimeException {
    public RefreshTokenIsNotRefreshedException(String message) {
        super(message);
    }
}



