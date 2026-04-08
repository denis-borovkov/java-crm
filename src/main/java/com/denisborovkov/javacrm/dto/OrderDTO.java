package com.denisborovkov.javacrm.dto;

public record OrderDTO(String description,
                       String customerName,
                       String customerEmail,
                       String customerPhone) {
}
