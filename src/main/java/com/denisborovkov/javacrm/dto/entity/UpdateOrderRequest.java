package com.denisborovkov.javacrm.dto.entity;

public record UpdateOrderRequest(String description,
                                 String customerName,
                                 String customerEmail,
                                 String customerPhone,
                                 String status) {
}
