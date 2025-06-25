package com.denisborovkov.interfaces;

import com.denisborovkov.exceptions.UserNotFoundException;
import com.denisborovkov.exceptions.UserRegistrationException;

public interface UserServiceDetails {
    void registerUser(UserDetails user) throws UserRegistrationException;
    UserDetails getUser(String username) throws UserNotFoundException;
    void validateUser(UserDetails user) throws UserRegistrationException;
    void deleteUser(Long id) throws UserNotFoundException;
    boolean isLoggedIn(boolean flag);
}
