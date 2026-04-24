package com.denisborovkov.javacrm.order.repository;

import com.denisborovkov.javacrm.order.dto.OrderDTO;
import com.denisborovkov.javacrm.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    OrderDTO findOrderByCustomerEmail(String customerEmail);
    OrderDTO findOrderByCustomerPhone(String customerPhone);
    Optional<OrderDTO> findOrderByOrderId(Long orderId);
}



