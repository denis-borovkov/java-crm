package com.denisborovkov.javacrm.auth.exception;

public class OneTimeTokenRateLimitException extends RuntimeException {
    public OneTimeTokenRateLimitException(String message) {
        super(message);
    }
}



