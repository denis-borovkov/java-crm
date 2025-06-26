package com.denisborovkov.interfaces;

import com.denisborovkov.exceptions.OrderNotFoundException;
import com.denisborovkov.exceptions.OrderRegistrationException;

public interface OrderServiceDetails {
    void createOrder(OrderDetails orderDetails) throws OrderRegistrationException;
    OrderDetails getOrder(Long id) throws OrderNotFoundException;
    String getAllOrders() throws OrderNotFoundException;
    void updateOrder(Long id) throws OrderNotFoundException;
    void deleteOrder(Long id) throws OrderNotFoundException;
}
