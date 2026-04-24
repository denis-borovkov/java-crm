package com.denisborovkov.javacrm.auth.exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Old password is incorrect");
    }
}



