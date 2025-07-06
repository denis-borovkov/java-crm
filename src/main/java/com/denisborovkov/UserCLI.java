package com.denisborovkov;

import com.denisborovkov.exceptions.ClientNotFoundException;
import com.denisborovkov.exceptions.OrderNotFoundException;
import com.denisborovkov.exceptions.OrderRegistrationException;
import com.denisborovkov.handlers.ClientMenuHandler;
import com.denisborovkov.handlers.GlobalMenuHandler;
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
    private final GlobalMenuHandler menuHandler = new GlobalMenuHandler(ui);
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
        fileService.loadUsersFromFile();
        try {
            while (true) {
                menuHandler.showMainMenu();
                switch (ui.userInput()) {
                    case "1":
                        if (login.signup())
                            fileService.saveUsersToFile();
                        break;
                    case "2":
                        while (login.signin()) {
                            menuHandler.showUserMenu();
                            switch (ui.userInput()) {
                                case "1":
                                    client.showClientMenu();
                                    continue;
                                case "2":
                                     order.showOrderMenu();
                                    continue;
                                case "3":
                                     continue;
                                default:
                                    menuHandler.showMenuErrorMessage();
                            }
                        }
                        break;
                    case "3":
                        menuHandler.showGoodbyeMessage();
                        return;
                    default:
                        menuHandler.showMenuErrorMessage();
                }
            }
        } catch (ClientNotFoundException | OrderRegistrationException | OrderNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            ui.close();
        }
    }
}

