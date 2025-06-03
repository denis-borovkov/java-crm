package com.denisborovkov.handlers;

import com.denisborovkov.ConsoleUI;
import com.denisborovkov.exceptions.UserNotFoundException;
import com.denisborovkov.exceptions.UserRegistrationException;
import com.denisborovkov.interfaces.UserDetails;
import com.denisborovkov.interfaces.UserServiceDetails;
import com.denisborovkov.models.User;

public class LoginMenuHandler {
    private final ConsoleUI ui;
    private final UserServiceDetails userService;

    public LoginMenuHandler(ConsoleUI consoleUI, UserServiceDetails userService) {
        this.ui = consoleUI;
        this.userService = userService;
    }

    public void signup() {
        ui.println("=== User Registration ===");
        try {
            String name = ui.prompt("Enter name: ");
            String email = ui.prompt("Enter email: ");
            String password = ui.prompt("Enter password: ");

            UserDetails user = new User()
                    .setName(name)
                    .setEmail(email)
                    .setPassword(password)
                    .build();

            userService.registerUser(user);
            ui.printSuccess(" User registered successfully!");
        } catch (UserRegistrationException e) {
            ui.printError("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            ui.printError("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void signin() {
        ui.println("=== User Login ===");
        try {
            String name = ui.prompt("Enter name: ");
            UserDetails user = userService.getUser(name);

            String password = ui.prompt("Enter password: ");
            if (password.equals(user.getPassword())) {
                ui.printSuccess(" Login successful! Welcome, " + user.getName());
            } else {
                ui.printError("✗ Invalid password");
            }
        } catch (UserNotFoundException e) {
            ui.printError("Login failed: " + e.getMessage());
        } catch (Exception e) {
            ui.printError("An unexpected error occurred: " + e.getMessage());
        }
    }
}
