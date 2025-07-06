package com.denisborovkov.handlers;

import com.denisborovkov.ConsoleUI;

public class GlobalMenuHandler {

    private final ConsoleUI ui;

    public GlobalMenuHandler(ConsoleUI ui) {
        this.ui = ui;
    }

    public void showMainMenu() {
        ui.println("Welcome to User Management System");
        ui.println("""
                === Main Menu ===
                1. Register new user
                2. Login
                3. Exit
                Choose an option (1-3):
                """);
    }

    public void showUserMenu() {
        ui.println("""
                 === User Menu ===
                 1. Show client menu
                 2. Show order menu
                 3. Exit
                 Choose an option (1-3):
                """);
    }

    public void showMenuErrorMessage() {
        ui.println("Invalid option. Please choose 1, 2, or 3.");
    }

    public void showGoodbyeMessage() {
        ui.println("Goodbye!");
    }
}
