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
        UserDetails existingUser = userRepo.findByUsername(user.getName());
        if (existingUser != null) {
            throw new UserRegistrationException("User with name '" + user.getName() + "' already exists");
        }
        if (user.getEmail() != null && !user.getEmail().equals(user.getName())) {
            existingUser = userRepo.findByEmail(user.getEmail());
            if (existingUser != null) {
                throw new UserRegistrationException("User with email '" + user.getEmail() + "' already exists");
            }
        }
        userRepo.save(user);
    }

    public UserDetails getUser(String username) throws UserNotFoundException {
        if (username == null) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }
        UserDetails user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with username: " + username + " not found");
        }
        return user;
    }

    public void validateUser(UserDetails user) throws UserRegistrationException {
        ValidationUtils.validateUser(user);
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        if(!userRepo.isExists(id)) {
            throw new UserNotFoundException("User with id '" + id + "' not found");
        }
        userRepo.delete(id);
        ui.println("User deleted successfully!");
    }

    public void updateUser(UserDetails user) {
        userRepo.update(user);
        ui.println("User updated successfully!");
    }

    public boolean isLoggedIn(boolean flag) {
        return flag;
    }
}
