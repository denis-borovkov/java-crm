package com.denisborovkov.javacrm.order.dto;

public record UpdateOrderRequest(String description,
                                 String customerName,
                                 String customerEmail,
                                 String customerPhone,
                                 String status) {
}



