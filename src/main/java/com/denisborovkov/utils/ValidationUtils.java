package com.denisborovkov.utils;

import com.denisborovkov.exceptions.ClientRegistrationException;
import com.denisborovkov.exceptions.UserRegistrationException;
import com.denisborovkov.interfaces.ClientDetails;
import com.denisborovkov.interfaces.UserDetails;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        return email == null || !email.contains("@") || !email.contains(".");
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{11}");
    }

    public static boolean validateUser(UserDetails user) throws UserRegistrationException {
        if (user == null) {
            throw new UserRegistrationException("User cannot be null");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new UserRegistrationException("Name cannot be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new UserRegistrationException("Email cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new UserRegistrationException("Password cannot be null or empty");
        }
        if (isValidEmail(user.getEmail())) {
            throw new UserRegistrationException("Invalid email format");
        }
        return true;
    }

    public static boolean validateClient(ClientDetails client) throws ClientRegistrationException {
        if (client == null) {
            throw new ClientRegistrationException("Client cannot be null");
        }
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new ClientRegistrationException("Name cannot be null or empty");
        }
        if (client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new ClientRegistrationException("Email cannot be null or empty");
        }
        if (client.getPhoneNumber() == null || client.getPhoneNumber().trim().isEmpty()) {
            throw new ClientRegistrationException("Phone number cannot be null or empty");
        }
        if (client.getStatus() == null || client.getStatus().trim().isEmpty()) {
            throw new ClientRegistrationException("Status cannot be null or empty");
        }
        if (isValidEmail(client.getEmail())) {
            throw new ClientRegistrationException("Invalid email format");
        }
        if (!isValidPhoneNumber(client.getPhoneNumber())) {
            throw new ClientRegistrationException("Invalid phone number format");
        }
        return true;
    }
}
