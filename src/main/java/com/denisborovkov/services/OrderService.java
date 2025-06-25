package com.denisborovkov.services;

import com.denisborovkov.ConsoleUI;
import com.denisborovkov.interfaces.OrderDetails;
import com.denisborovkov.interfaces.OrderRepository;
import com.denisborovkov.interfaces.OrderServiceDetails;
import com.denisborovkov.exceptions.OrderNotFoundException;
import com.denisborovkov.exceptions.OrderRegistrationException;

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

    public OrderDetails getOrder(Long id) throws OrderNotFoundException {
        OrderDetails order = orderRepo.get(id);
        if (order == null) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }
        ui.println("Order retrieved successfully!");
        ui.println("Order ID: " + order.getId());
        return order;
    }

    public void updateOrder(Long id) throws OrderNotFoundException {
        OrderDetails order = orderRepo.get(id);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        } else {
            orderRepo.update(order);
            ui.println("Order updated successfully!");
        }
    }

    public void deleteOrder(Long id) throws OrderNotFoundException {
        if (id == null) {
            throw new OrderNotFoundException("Order not found");
        }
        orderRepo.delete(id);
        ui.println("Order deleted successfully!");
    }
}
