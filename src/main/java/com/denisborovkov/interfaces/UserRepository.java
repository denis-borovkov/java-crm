package com.denisborovkov.interfaces;

public interface UserRepository {
    void save(UserDetails user);
    UserDetails getUser(String key);
    boolean isUserExists(String name);
    int getUserCount();
    void updateUser(UserDetails user);
    void deleteUser(String name);
}
