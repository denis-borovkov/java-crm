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

    public void getOrder(int id) throws OrderNotFoundException {
        OrderDetails order = orderRepo.getOrder(id);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        } else {
            ui.println("Order retrieved successfully!");
            ui.println("Order ID: " + order.getId());
        }
    }

    public void updateOrder(int id) throws OrderNotFoundException {
        OrderDetails order = orderRepo.getOrder(id);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        } else {
            orderRepo.updateOrder(order);
            ui.println("Order updated successfully!");
        }
    }

    public void deleteOrder(int id) throws OrderNotFoundException {
        OrderDetails order = orderRepo.getOrder(id);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        } else {
            orderRepo.deleteOrder(id);
            ui.println("Order deleted successfully!");
        }
    }

    public Iterable<OrderDetails> getAllOrders() {
        return orderRepo.getAllOrders();
    }
}
