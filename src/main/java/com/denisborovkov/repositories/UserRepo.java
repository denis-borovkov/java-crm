package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.UserDetails;
import com.denisborovkov.interfaces.UserRepository;
import java.util.Map;
import java.util.HashMap;

public class UserRepo implements UserRepository {

    private final Map<String, UserDetails> userDatabase = new HashMap<>();

    public void save(UserDetails user) {
        if (user == null || user.getName() == null) {
            throw new IllegalArgumentException("User and user's name cannot be null");
        }
        userDatabase.put(user.getName(), user);
    }

    public UserDetails getUser(String key) {
        if (key == null) {
            return null;
        }
        return userDatabase.get(key);
    }

    public boolean isUserExists(String name) {
        return name != null && userDatabase.containsKey(name);
    }

    public int getUserCount() {
        return userDatabase.size();
    }

    public void deleteUser(String name) {
        if (name != null)
            userDatabase.remove(name);
        else
            throw new IllegalArgumentException("Name cannot be null");
    }

    public void updateUser(UserDetails user) {
        if (user != null && user.getName() != null)
            userDatabase.put(user.getName(), user);
        else
            throw new IllegalArgumentException("User and user's name cannot be null");
    }
}
