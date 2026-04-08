package com.denisborovkov.javacrm.dto;

public record UpdateCustomerResponse(Long customerId,
                                     String firstName,
                                     String lastName,
                                     String email,
                                     String phoneNumber,
                                     String description) {
}
