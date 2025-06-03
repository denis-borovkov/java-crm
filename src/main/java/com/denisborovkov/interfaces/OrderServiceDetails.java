package com.denisborovkov.interfaces;

import com.denisborovkov.exceptions.OrderNotFoundException;
import com.denisborovkov.exceptions.OrderRegistrationException;

public interface OrderServiceDetails {
    void createOrder(OrderDetails order) throws OrderRegistrationException;
    void getOrder(int id) throws OrderNotFoundException;
    void updateOrder(int id) throws OrderNotFoundException;
    void deleteOrder(int id) throws OrderNotFoundException;
}
