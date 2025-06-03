package com.denisborovkov.exceptions;

public class ClientNotFoundException extends Exception {
    public ClientNotFoundException(String message) {
        super(message);
    }
}
