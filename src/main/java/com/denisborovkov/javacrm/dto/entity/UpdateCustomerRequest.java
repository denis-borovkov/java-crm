package com.denisborovkov.javacrm.dto.entity;

import jakarta.validation.constraints.Email;

public record UpdateCustomerRequest(String firstName,
                                    String lastName,
                                    @Email String email,
                                    String phoneNumber,
                                    String description) {
}
