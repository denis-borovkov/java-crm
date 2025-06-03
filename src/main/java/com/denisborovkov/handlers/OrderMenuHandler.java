package com.denisborovkov.handlers;

import com.denisborovkov.ConsoleUI;
import com.denisborovkov.exceptions.ClientNotFoundException;
import com.denisborovkov.interfaces.ClientDetails;
import com.denisborovkov.interfaces.ClientServiceDetails;
import com.denisborovkov.interfaces.OrderDetails;
import com.denisborovkov.interfaces.OrderServiceDetails;
import com.denisborovkov.models.Client;
import com.denisborovkov.models.Order;

import java.time.LocalDate;
import java.util.UUID;

public class OrderMenuHandler {
    private final ConsoleUI ui;
    private final ClientServiceDetails clientService;
    private final OrderServiceDetails orderService;

    public OrderMenuHandler(ConsoleUI ui, ClientServiceDetails clientService, OrderServiceDetails orderService) {
        this.ui = ui;
        this.clientService = clientService;
        this.orderService = orderService;
    }

    public void showOrderMenu() throws ClientNotFoundException {
        ui.println("=== Order Menu ===");
        ui.println("1. Create new order");
        ui.println("2. Back to main menu");
        ui.println("Choose an option (1-2): ");

        String choice = ui.userInput();

        switch (choice) {
            case "1":
                createNewOrder();
                break;
            case "2":
                return;
            default:
                ui.println("Invalid option. Please choose 1 or 2.");
        }
    }

    public void createNewOrder() throws ClientNotFoundException {
        ui.println("=== Create New Order ===");
        ui.prompt("Enter client id:");
        clientService.getAllClients();
        UUID id = UUID.fromString(ui.prompt("Enter client uuid:"));
        ClientDetails client = clientService.getClient(id);
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
        } catch (Exception e) {
            ui.printError("Order creation failed: " + e.getMessage());
        }
    }
}
