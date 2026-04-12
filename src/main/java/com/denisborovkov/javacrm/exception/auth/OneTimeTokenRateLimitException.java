package com.denisborovkov.javacrm.exception.auth;

public class OneTimeTokenRateLimitException extends RuntimeException {
    public OneTimeTokenRateLimitException(String message) {
        super(message);
    }
}
