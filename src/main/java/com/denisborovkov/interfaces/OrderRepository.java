package com.denisborovkov.interfaces;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<OrderDetails, UUID> {
}
