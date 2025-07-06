package com.denisborovkov.interfaces;

import java.util.Map;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<OrderDetails, UUID> {
    Map<UUID, OrderDetails> getOrdersData();
    void loadAll(Map<UUID, OrderDetails> users);
}
