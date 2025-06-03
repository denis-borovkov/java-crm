package com.denisborovkov.services;

import com.denisborovkov.ConsoleUI;
import com.denisborovkov.exceptions.UserNotFoundException;
import com.denisborovkov.exceptions.UserRegistrationException;
import com.denisborovkov.interfaces.UserDetails;
import com.denisborovkov.interfaces.UserRepository;
import com.denisborovkov.interfaces.UserServiceDetails;
import com.denisborovkov.utils.ValidationUtils;
import java.util.Objects;

public class UserService implements UserServiceDetails {
    private final ConsoleUI ui;
    private final UserRepository userRepo;

    public UserService(ConsoleUI ui, UserRepository userRepo) {
        this.ui = ui;
        this.userRepo = Objects.requireNonNull(userRepo, "UserRepo cannot be null");
    }

    public void registerUser(UserDetails user) throws UserRegistrationException {
        if (user == null) {
            throw new UserRegistrationException("User cannot be null");
        }
        validateUser(user);
        if (userRepo.isUserExists(user.getName())) {
            throw new UserRegistrationException("User with name '" + user.getName() + "' already exists");
        }
        userRepo.save(user);
    }

    public UserDetails getUser(String name) throws UserNotFoundException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        UserDetails user = userRepo.getUser(name);
        if (user == null) {
            throw new UserNotFoundException("User with name '" + name + "' not found");
        }
        return user;
    }

    public void validateUser(UserDetails user) throws UserRegistrationException {
        ValidationUtils.validateUser(user);
    }

    public void deleteUser(String name) {
        userRepo.deleteUser(name);
        ui.println("User deleted successfully!");
    }

    public void updateUser(UserDetails user) {
        userRepo.updateUser(user);
        ui.println("User updated successfully!");
    }

    public boolean isLoggedIn(boolean flag) {
        return flag;
    }
}
