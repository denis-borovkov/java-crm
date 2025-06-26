package com.denisborovkov.handlers;

import com.denisborovkov.ConsoleUI;
import com.denisborovkov.exceptions.ClientRegistrationException;
import com.denisborovkov.interfaces.ClientDetails;
import com.denisborovkov.interfaces.ClientServiceDetails;
import com.denisborovkov.models.Client;

public class ClientMenuHandler {
    private final ConsoleUI ui;
    private final ClientServiceDetails clientService;

    public ClientMenuHandler(ConsoleUI ui, ClientServiceDetails clientService) {
        this.ui = ui;
        this.clientService = clientService;
    }

    public void showClientMenu() {
        ui.println("=== Client Menu ===");
        ui.println("1. Add new client");
        ui.println("2. Back to main menu");
        ui.println("Choose an option (1-2): ");
        String choice = ui.userInput();

        switch (choice) {
            case "1":
                addNewClient();
                break;
            case "2":
                return;
            default:
                ui.println("Invalid option. Please choose 1 or 2.");
        }
    }

    public void addNewClient() {
        ui.println("=== Add New Client ===");
        ui.println("Enter client details:");
        try {
            String name = ui.prompt("Enter name: ");
            String email = ui.prompt("Enter email: ");
            String phoneNumber = ui.prompt("Enter phone number: ");
            String status = ui.prompt("Enter status: ");

            ClientDetails client = new Client()
                    .setId()
                    .setName(name)
                    .setEmail(email)
                    .setPhoneNumber(phoneNumber)
                    .setStatus(status)
                    .build();

            clientService.createClient(client);
            ui.printSuccess(" Client added successfully!");
        } catch (ClientRegistrationException e) {
            ui.printError("Client registration failed: " + e.getMessage());
        }
    }
}
