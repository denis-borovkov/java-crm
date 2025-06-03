package com.denisborovkov.interfaces;

import com.denisborovkov.exceptions.UserNotFoundException;
import com.denisborovkov.exceptions.UserRegistrationException;

public interface UserServiceDetails {
    void registerUser(UserDetails user) throws UserRegistrationException;
    UserDetails getUser(String name) throws UserNotFoundException;
    void validateUser(UserDetails user) throws UserRegistrationException;
    void deleteUser(String name);
    boolean isLoggedIn(boolean flag);
}
