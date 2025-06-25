package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.OrderDetails;
import com.denisborovkov.interfaces.OrderRepository;
import java.util.HashMap;
import java.util.Map;

public class OrderRepo implements OrderRepository {

    private final Map<Long, OrderDetails> orders = new HashMap<>();

    public OrderDetails save(OrderDetails order) {
        orders.put(order.getId(), order);
        return order;
    }

    public OrderDetails get(Long id) {
        return orders.get(id);
    }

    @Override
    public boolean isExists(Long id) {
        return orders.containsKey(id);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    public void update(OrderDetails order) {
        orders.put(order.getId(), order);
    }

    public void delete(Long id) {
        orders.remove(id);
    }

    public OrderDetails getAll() {
        return (OrderDetails) orders.values();
    }
}
