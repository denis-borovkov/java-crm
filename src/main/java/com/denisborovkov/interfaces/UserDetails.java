package com.denisborovkov.interfaces;

import com.denisborovkov.models.User;

public interface UserDetails {
    Long getId();
    String getName();
    String getEmail();
    String getPassword();
    User setName(String name);
    User setEmail(String email);
    User setPassword(String password);
    User build();
}
