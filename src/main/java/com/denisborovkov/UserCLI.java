package com.denisborovkov;

import com.denisborovkov.exceptions.ClientNotFoundException;
import com.denisborovkov.exceptions.OrderNotFoundException;
import com.denisborovkov.exceptions.OrderRegistrationException;
import com.denisborovkov.handlers.ClientMenuHandler;
import com.denisborovkov.handlers.LoginMenuHandler;
import com.denisborovkov.handlers.OrderMenuHandler;
import com.denisborovkov.interfaces.*;
import com.denisborovkov.repositories.*;
import com.denisborovkov.services.ClientService;
import com.denisborovkov.services.FileService;
import com.denisborovkov.services.OrderService;
import com.denisborovkov.services.UserService;

public class UserCLI {

    private final ConsoleUI ui = new ConsoleUI();
    private final UserRepository userRepo = new UserRepo();
    private final OrderRepository orderRepo = new OrderRepo();
    private final ClientRepository clientRepo = new ClientRepo();
    private final NotificationRepository notificationRepo = new NotificationRepo();
    private final MessageRepository messageRepo = new MessageRepo();
    private final AuthenticationRepository authenticationRepo = new AuthenticationRepo();
    private final UserServiceDetails userService = new UserService(ui, userRepo);
    private final OrderServiceDetails orderService = new OrderService(ui, orderRepo);
    private final ClientServiceDetails clientService = new ClientService(ui , clientRepo);
    private final LoginMenuHandler login = new LoginMenuHandler(ui, userService);
    private final OrderMenuHandler order = new OrderMenuHandler(ui,clientService, orderService);
    private final ClientMenuHandler client = new ClientMenuHandler(ui, clientService);
    private final FileServiceDetails fileService = new FileService(
            userRepo,
            orderRepo,
            clientRepo,
            notificationRepo,
            messageRepo,
            authenticationRepo,
            ui);

    public void run() {
        ui.println("Welcome to User Management System");
        fileService.loadUsersFromFile();
        try {
            while (true) {
                ui.println("""
                        === Main Menu ===
                        1. Register new user
                        2. Login
                        3. Exit
                        Choose an option (1-3):
                        """);

                switch (ui.userInput()) {
                    case "1":
                        login.signup();
                        break;
                    case "2":
                        login.signin();
                        while (userService.isLoggedIn(true)) {
                            ui.println("""
                                     === User Menu ===
                                     1. Register new client
                                     2. Register new order
                                     3. Exit
                                     Choose an option (1-3):
                                    """);
                            switch (ui.userInput()) {
                                case "1":
                                    client.showClientMenu();
                                    break;
                                case "2":
                                     order.showOrderMenu();
                                    break;
                                case "3":
                                     return;
                                default:
                                    ui.println("Invalid option. Please choose 1, 2, or 3.");
                            }
                        }
                        break;
                    case "3":
                        ui.println("Goodbye!");
                        return;
                    default:
                        ui.println("Invalid option. Please choose 1, 2, or 3.");
                }
            }
        } catch (ClientNotFoundException | OrderRegistrationException | OrderNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            ui.close();
        }
    }
}

