package com.denisborovkov.services;

import com.denisborovkov.ConsoleUI;
import com.denisborovkov.interfaces.OrderDetails;
import com.denisborovkov.interfaces.OrderRepository;
import com.denisborovkov.interfaces.OrderServiceDetails;
import com.denisborovkov.exceptions.OrderNotFoundException;
import com.denisborovkov.exceptions.OrderRegistrationException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OrderService implements OrderServiceDetails {

    private final ConsoleUI ui;
    private final OrderRepository orderRepo;

    public OrderService(ConsoleUI ui, OrderRepository orderRepo) {
        this.ui = ui;
        this.orderRepo = orderRepo;
    }

    public void createOrder(OrderDetails order) throws OrderRegistrationException {
        if (order == null) {
            throw new OrderRegistrationException("Order cannot be null");
        }
        orderRepo.save(order);
    }

    public OrderDetails getOrder(UUID id) throws OrderNotFoundException {
        OrderDetails order = orderRepo.get(id);
        if (order == null) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }
        ui.println("Order retrieved successfully!");
        ui.println("Order ID: " + order.getId());
        return order;
    }

    @Override
    public String getAllOrders() throws OrderNotFoundException {
        List<OrderDetails> orders = Collections.singletonList(orderRepo.getAll());
        ui.println("Orders retrieved successfully!");
        return orders.toString();
    }

    public void updateOrder(UUID id) throws OrderNotFoundException {
        OrderDetails order = orderRepo.get(id);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        } else {
            orderRepo.update(order);
            ui.println("Order updated successfully!");
        }
    }

    public void deleteOrder(UUID id) throws OrderNotFoundException {
        if (id == null) {
            throw new OrderNotFoundException("Order not found");
        }
        orderRepo.delete(id);
        ui.println("Order deleted successfully!");
    }
}
