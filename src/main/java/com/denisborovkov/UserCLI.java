package com.denisborovkov;

import com.denisborovkov.handlers.ClientMenuHandler;
import com.denisborovkov.handlers.LoginMenuHandler;
import com.denisborovkov.handlers.OrderMenuHandler;
import com.denisborovkov.interfaces.ClientServiceDetails;
import com.denisborovkov.interfaces.OrderServiceDetails;
import com.denisborovkov.interfaces.UserServiceDetails;
import com.denisborovkov.repositories.ClientRepo;
import com.denisborovkov.repositories.OrderRepo;
import com.denisborovkov.repositories.UserRepo;
import com.denisborovkov.services.ClientService;
import com.denisborovkov.services.OrderService;
import com.denisborovkov.services.UserService;

public class UserCLI {
    private final ConsoleUI ui = new ConsoleUI();
    private final UserServiceDetails userService = new UserService(ui, new UserRepo());
    private final OrderServiceDetails orderService = new OrderService(ui, new OrderRepo());
    private final ClientServiceDetails clientService = new ClientService(ui ,new ClientRepo());
    private final LoginMenuHandler login = new LoginMenuHandler(ui, userService);
    private final OrderMenuHandler order = new OrderMenuHandler(ui,clientService, orderService);
    private final ClientMenuHandler client = new ClientMenuHandler(ui, clientService);

    public void run() {
        ui.println("Welcome to User Management System");
        try {
            while (true) {
                ui.println("""
                        === Main Menu ===
                        1. Register new user
                        2. Login
                        3. Exit
                        Choose an option (1-3):
                        """);

                String choice = ui.userInput();

                switch (choice) {
                    case "1":
                        login.signup();
                        break;
                    case "2":
                        login.signin();
                        break;
                    case "3":
                        ui.println("Goodbye!");
                        return;
                    default:
                        ui.println("Invalid option. Please choose 1, 2, or 3.");
                }
            }
        } finally {
            ui.close();
        }
    }
}

