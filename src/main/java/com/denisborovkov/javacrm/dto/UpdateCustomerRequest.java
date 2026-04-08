package com.denisborovkov.javacrm.dto;

import jakarta.validation.constraints.Email;

public record UpdateCustomerRequest(String firstName,
                                    String lastName,
                                    @Email String email,
                                    String phoneNumber,
                                    String description) {
}
