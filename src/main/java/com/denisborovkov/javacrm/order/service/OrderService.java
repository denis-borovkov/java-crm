package com.denisborovkov.javacrm.order.service;

import com.denisborovkov.javacrm.order.repository.OrderRepository;
import com.denisborovkov.javacrm.order.dto.CreateOrderRequest;
import com.denisborovkov.javacrm.order.dto.OrderDTO;
import com.denisborovkov.javacrm.order.dto.UpdateOrderRequest;
import com.denisborovkov.javacrm.order.domain.Order;
import com.denisborovkov.javacrm.customer.exception.CustomerNotFoundException;
import com.denisborovkov.javacrm.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderDTO findOrderById(Long orderId) {
        return orderRepository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new CustomerNotFoundException(orderId));
    }

    public OrderDTO findOrderByEmail(String email) {
        return orderRepository.findOrderByCustomerEmail(email);
    }

    public OrderDTO findOrderByCustomerPhone(String phone) {
        return orderRepository.findOrderByCustomerPhone(phone);
    }

    public List<OrderDTO> findAllOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO createOrder(CreateOrderRequest request) throws CustomerNotFoundException {
        Order order = orderRepository.save(orderMapper.toEntity(request));
        return orderMapper.toDTO(order);
    }

    public void saveOrder(Long orderId, UpdateOrderRequest request) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new CustomerNotFoundException(orderId));
        orderMapper.updateOrder(request, order);
        orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(Long orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomerNotFoundException(orderId));
        orderMapper.updateOrder(request, order);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new CustomerNotFoundException(orderId));
        orderRepository.delete(order);
    }
}



