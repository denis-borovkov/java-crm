package com.denisborovkov.models;

import com.denisborovkov.interfaces.UserDetails;

import java.util.Objects;

public class User implements UserDetails {

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
        this.name = name != null ? name.trim() : null;
        return this;
    }

    @Override
    public User setEmail(String email) {
        this.email = email != null ? email.trim().toLowerCase() : null;
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
                ", password='[PROTECTED]'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
