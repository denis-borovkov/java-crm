package com.denisborovkov.javacrm.exception;

public class PasswordMismatchException extends Exception {
    public PasswordMismatchException() {
        super("Old password is incorrect");
    }
}
