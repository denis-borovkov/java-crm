package com.denisborovkov.javacrm.customer.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(@NonNull String email) {
        super("Customer with email " + email + " not found");
    }
    public CustomerNotFoundException(@NonNull Long id) {
        super("Customer with id " + id + " not found");
    }
}



