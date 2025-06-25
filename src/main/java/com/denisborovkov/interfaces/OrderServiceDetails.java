package com.denisborovkov.interfaces;

import com.denisborovkov.exceptions.OrderRegistrationException;

public interface OrderServiceDetails extends CrudRepository<OrderDetails, Long> {
    void createOrder(OrderDetails orderDetails) throws OrderRegistrationException;
}
