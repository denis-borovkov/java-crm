package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.OrderDetails;
import com.denisborovkov.interfaces.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderRepo implements OrderRepository {

    private final Map<UUID, OrderDetails> ordersData = new HashMap<>();

    public Map<UUID, OrderDetails> getOrdersData() {
        return new HashMap<>(ordersData);
    }

    @Override
    public void loadAll(Map<UUID, OrderDetails> orders) {
        ordersData.putAll(orders);
    }

    public OrderDetails save(OrderDetails order) {
        ordersData.put(order.getId(), order);
        return order;
    }

    public OrderDetails get(UUID id) {
        return ordersData.get(id);
    }

    @Override
    public boolean isExists(UUID id) {
        return ordersData.containsKey(id);
    }

    @Override
    public int getCount() {
        return ordersData.size();
    }

    public void update(OrderDetails order) {
        ordersData.put(order.getId(), order);
    }

    public void delete(UUID id) {
        ordersData.remove(id);
    }

    public OrderDetails getAll() {
        return (OrderDetails) ordersData.values();
    }
}
