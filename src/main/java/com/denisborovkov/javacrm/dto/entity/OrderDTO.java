package com.denisborovkov.javacrm.dto.entity;

public record OrderDTO(String description,
                       String customerName,
                       String customerEmail,
                       String customerPhone) {
}
