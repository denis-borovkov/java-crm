package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.entity.CreateOrderRequest;
import com.denisborovkov.javacrm.dto.entity.OrderDTO;
import com.denisborovkov.javacrm.dto.entity.UpdateOrderRequest;
import com.denisborovkov.javacrm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public OrderDTO create(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping("/save")
    public void save(Long id, @RequestBody UpdateOrderRequest request) {
        orderService.saveOrder(id, request);
    }

    @GetMapping("/all")
    public List<OrderDTO> getAll() {
        return orderService.findAllOrders();
    }

    @GetMapping("/id")
    public OrderDTO getById(@RequestParam Long id) {
        return orderService.findOrderById(id);
    }

    @GetMapping("email")
    public OrderDTO getByEmail(@RequestParam String email) {
        return orderService.findOrderByEmail(email);
    }

    @GetMapping("/phone")
    public OrderDTO getByCustomerPhone(@RequestParam String phone) {
        return orderService.findOrderByCustomerPhone(phone);
    }


}
