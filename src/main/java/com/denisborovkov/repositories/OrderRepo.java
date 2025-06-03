package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.OrderDetails;
import com.denisborovkov.interfaces.OrderRepository;
import java.util.HashMap;
import java.util.Map;

public class OrderRepo implements OrderRepository {

    private final Map<Integer, OrderDetails> orders = new HashMap<>();

    public void save(OrderDetails order) {
        orders.put(order.getId(), order);
    }

    public OrderDetails getOrder(int id) {
        return orders.get(id);
    }

    public void updateOrder(OrderDetails order) {
        orders.put(order.getId(), order);
    }

    public void deleteOrder(int id) {
        orders.remove(id);
    }

    public Iterable<OrderDetails> getAllOrders() {
        return orders.values();
    }
}
