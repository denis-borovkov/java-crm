package com.denisborovkov.handlers;

import com.denisborovkov.ConsoleUI;
import com.denisborovkov.exceptions.ClientNotFoundException;
import com.denisborovkov.exceptions.OrderNotFoundException;
import com.denisborovkov.exceptions.OrderRegistrationException;
import com.denisborovkov.interfaces.ClientDetails;
import com.denisborovkov.interfaces.ClientServiceDetails;
import com.denisborovkov.interfaces.OrderDetails;
import com.denisborovkov.interfaces.OrderServiceDetails;
import com.denisborovkov.models.Client;
import com.denisborovkov.models.Order;

import java.time.LocalDate;

public class OrderMenuHandler {
    private final ConsoleUI ui;
    private final ClientServiceDetails clientService;
    private final OrderServiceDetails orderService;

    public OrderMenuHandler(ConsoleUI ui, ClientServiceDetails clientService, OrderServiceDetails orderService) {
        this.ui = ui;
        this.clientService = clientService;
        this.orderService = orderService;
    }

    public void showOrderMenu() throws ClientNotFoundException, OrderRegistrationException, OrderNotFoundException {
        ui.println("""
                === Order Menu ===
                1. Create new order
                2. Show all orders
                3. Delete order
                4. Update order
                5. Exit
                Choose an option (1-2):
                """);

        switch (ui.userInput()) {
            case "1":
                createNewOrder();
                break;
            case "2":
                ui.println(orderService.getAll());
                break;
            case "3":
                Long orderId = Long.parseLong(ui.prompt("Enter an order id"));
                orderService.deleteOrder(orderId);
                break;
            case "4":
                //TODO
                break;
            case "5":
                return;
            default:
                ui.println("Invalid option. Please choose 1 or 5.");
        }
    }

    public void createNewOrder() throws OrderRegistrationException {
        ui.println("=== Create New Order ===");
        clientService.getAll();
        Long id = Long.valueOf(ui.prompt("Enter client uuid:"));
        ClientDetails client = clientService.get(id);
        ui.println("Enter order details:");

        try {
            String description = ui.prompt("Enter description: ");
            double price = Double.parseDouble(ui.prompt("Enter price: "));
            String status = ui.prompt("Enter status: ");
            OrderDetails order = new Order()
                    .setId()
                    .setClient((Client) client)
                    .setDescription(description)
                    .setPrice(price)
                    .setStatus(status)
                    .setCreatedAt(LocalDate.now())
                    .build();
            orderService.createOrder(order);
        } catch (OrderRegistrationException e) {
            throw new OrderRegistrationException(e.getMessage());
        }
    }
}
