package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.UserDetails;
import com.denisborovkov.interfaces.UserRepository;
import java.util.Map;
import java.util.HashMap;

public class UserRepo implements UserRepository {

    private final Map<Long, UserDetails> userDatabase = new HashMap<>();

    public UserDetails save(UserDetails user) {
        if (user == null || user.getName() == null) {
            throw new IllegalArgumentException("User and user's name cannot be null");
        }
        userDatabase.put(user.getId(), user);
        return user;
    }

    public UserDetails findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userDatabase.get(Long.parseLong(username));
    }

    public UserDetails get(Long key) {
        if (key == null) {
            return null;
        }
        return userDatabase.get(key);
    }

    public boolean isExists(Long id) {
        return id != null && userDatabase.containsKey(id);
    }

    public int getCount() {
        return userDatabase.size();
    }

    public void delete(Long id) {
        if (id != null)
            userDatabase.remove(id);
        else
            throw new IllegalArgumentException("Name cannot be null");
    }

    public void update(UserDetails user) {
        if (user != null && user.getName() != null)
            userDatabase.put(user.getId(), user);
        else
            throw new IllegalArgumentException("User and user's name cannot be null");
    }

    public UserDetails getAll() {
        for (UserDetails user : userDatabase.values()) {
            System.out.println(user);
        }
        return userDatabase.values().iterator().next();
    }
}
