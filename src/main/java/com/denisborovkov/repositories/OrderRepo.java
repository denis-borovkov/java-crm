package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.OrderDetails;
import com.denisborovkov.interfaces.OrderRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderRepo implements OrderRepository {

    private final Map<UUID, OrderDetails> orders = new HashMap<>();

    public OrderDetails save(OrderDetails order) {
        orders.put(order.getId(), order);
        return order;
    }

    public OrderDetails get(UUID id) {
        return orders.get(id);
    }

    @Override
    public boolean isExists(UUID id) {
        return orders.containsKey(id);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    public void update(OrderDetails order) {
        orders.put(order.getId(), order);
    }

    public void delete(UUID id) {
        orders.remove(id);
    }

    public OrderDetails getAll() {
        return (OrderDetails) orders.values();
    }
}
