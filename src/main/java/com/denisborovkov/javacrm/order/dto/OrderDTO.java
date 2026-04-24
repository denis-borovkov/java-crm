package com.denisborovkov.javacrm.order.dto;

import jakarta.validation.constraints.Email;

public record OrderDTO(String description,
                       String customerName,
                       @Email String customerEmail,
                       String customerPhone) {
}



