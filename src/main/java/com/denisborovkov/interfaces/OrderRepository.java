package com.denisborovkov.interfaces;

public interface OrderRepository {
    void save(OrderDetails order);
    OrderDetails getOrder(int id);
    void updateOrder(OrderDetails order);
    void deleteOrder(int id);
    Iterable<OrderDetails> getAllOrders();
}
