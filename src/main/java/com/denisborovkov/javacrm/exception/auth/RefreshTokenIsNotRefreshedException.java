package com.denisborovkov.javacrm.exception.auth;

public class RefreshTokenIsNotRefreshedException extends RuntimeException {
    public RefreshTokenIsNotRefreshedException(String message) {
        super(message);
    }
}
