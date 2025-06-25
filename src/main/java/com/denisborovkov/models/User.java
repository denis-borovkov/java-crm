package com.denisborovkov.models;

import com.denisborovkov.interfaces.UserDetails;

public class User implements UserDetails {

    private final Long id = System.currentTimeMillis();
    private String name;
    private String email;
    private String password;

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public User build() {
        return new User(name, email, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
