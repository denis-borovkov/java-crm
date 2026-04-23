package com.denisborovkov.javacrm.dao;

import com.denisborovkov.javacrm.dto.entity.OrderDTO;
import com.denisborovkov.javacrm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    OrderDTO findOrderByCustomerEmail(String customerEmail);
    OrderDTO findOrderByCustomerPhone(String customerPhone);
    Optional<OrderDTO> findOrderByOrderId(Long orderId);
}
