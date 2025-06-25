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

    @Override
    public OrderDetails save(OrderDetails orderDetails) {
        return null;
    }

    public OrderDetails get(Long id) {
        OrderDetails order = orderRepo.get(id);
        ui.println("Order retrieved successfully!");
        ui.println("Order ID: " + order.getId());
        return order;
    }

    @Override
    public boolean isExists(Long id) {
        return orderRepo.isExists(id);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void update(OrderDetails orderDetails) {
    }

    public void update(Long id) throws OrderNotFoundException {
        OrderDetails order = orderRepo.get(id);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        } else {
            orderRepo.update(order);
            ui.println("Order updated successfully!");
        }
    }

    public void delete(Long id) {
        OrderDetails order = orderRepo.get(id);
        if (order == null) {
            ui.println("Order not found");
        }
        orderRepo.delete(id);
        ui.println("Order deleted successfully!");
    }

    @Override
    public OrderDetails getAll() {
        return orderRepo.getAll();
    }
}
