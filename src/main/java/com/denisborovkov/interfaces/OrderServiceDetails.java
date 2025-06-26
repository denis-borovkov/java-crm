package com.denisborovkov.interfaces;

import com.denisborovkov.exceptions.OrderNotFoundException;
import com.denisborovkov.exceptions.OrderRegistrationException;

import java.util.UUID;

public interface OrderServiceDetails {
    void createOrder(OrderDetails orderDetails) throws OrderRegistrationException;
    OrderDetails getOrder(UUID id) throws OrderNotFoundException;
    String getAllOrders() throws OrderNotFoundException;
    void updateOrder(UUID id) throws OrderNotFoundException;
    void deleteOrder(UUID id) throws OrderNotFoundException;
}
