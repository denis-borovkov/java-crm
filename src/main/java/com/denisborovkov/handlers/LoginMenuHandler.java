package com.denisborovkov.handlers;

import com.denisborovkov.ConsoleUI;
import com.denisborovkov.exceptions.UserNotFoundException;
import com.denisborovkov.exceptions.UserRegistrationException;
import com.denisborovkov.interfaces.UserDetails;
import com.denisborovkov.interfaces.UserServiceDetails;
import com.denisborovkov.models.User;
import com.denisborovkov.utils.PasswordUtils;
import com.denisborovkov.utils.ValidationUtils;

public class LoginMenuHandler {
    private final ConsoleUI ui;
    private final UserServiceDetails userService;

    public LoginMenuHandler(ConsoleUI consoleUI, UserServiceDetails userService) {
        this.ui = consoleUI;
        this.userService = userService;
    }

    public boolean signup() {
        ui.println("=== User Registration ===");
        try {
            String name = ui.prompt("Enter name: ");
            String email = ui.prompt("Enter email: ");
            String password = ui.prompt("Enter password: ");

            UserDetails user = new User()
                    .setId()
                    .setName(name)
                    .setEmail(email)
                    .setPassword(PasswordUtils.hashPassword(password))
                    .build();
            if (ValidationUtils.validateUser(user)) {
                userService.registerUser(user);
                ui.printSuccess(" User registered successfully!");
            }
            return true;
        } catch (UserRegistrationException e) {
            ui.printError("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            ui.printError("An unexpected error occurred: " + e.getMessage());
        }
        return false;
    }

    public boolean signin() {
        ui.println("=== User Login ===");
        try {
            String name = ui.prompt("Enter name: ");
            UserDetails user = userService.getUser(name);
            String password = ui.prompt("Enter password: ");
            if (PasswordUtils.checkPassword(password, user.getPassword())) {
                ui.printSuccess(" Login successful! Welcome, " + user.getName());
                return true;
            } else {
                ui.printError("Invalid password");
            }
        } catch (UserNotFoundException e) {
            ui.printError("Login failed: " + e.getMessage());
        }
        return false;
    }
}
