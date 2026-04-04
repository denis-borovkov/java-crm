package com.denisborovkov.javacrm.exception;

import lombok.NonNull;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(@NonNull String email) {
        super("Customer with email " + email + " not found");
    }
    public CustomerNotFoundException(@NonNull Long id) {
        super("Customer with id " + id + " not found");
    }
}
