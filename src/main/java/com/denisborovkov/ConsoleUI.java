package com.denisborovkov;

import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    public void println(String message) {
        System.out.println(message);
    }
    public void printError(String message) {
        System.err.println(message);
    }

    public void printSuccess(String message) {
        System.out.println("✓ " + message);
    }

    public String prompt(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        while (input == null || input.trim().isEmpty()) {
            System.out.print("Input cannot be empty. " + prompt);
            input = scanner.nextLine();
        }
        return input.trim();
    }

    public String userInput() {
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }

}
