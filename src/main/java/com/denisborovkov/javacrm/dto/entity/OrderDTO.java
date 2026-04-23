package com.denisborovkov.javacrm.dto.entity;

import jakarta.validation.constraints.Email;

public record OrderDTO(String description,
                       String customerName,
                       @Email String customerEmail,
                       String customerPhone) {
}
