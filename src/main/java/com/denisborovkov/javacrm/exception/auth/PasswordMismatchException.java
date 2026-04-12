package com.denisborovkov.javacrm.exception.auth;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Old password is incorrect");
    }
}
