package com.denisborovkov.models;

import com.denisborovkov.interfaces.UserDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("user")
public class User implements UserDetails {

    private Long id;
    private String name;
    private String email;
    private String password;

    public User() {}

    public User(Long id, String name, String email, String password) {
        this.id = id;
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

    public User setId() {
        this.id = System.currentTimeMillis();
        return this;
    }

    @Override
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public User build() {
        return new User(id, name, email, password);
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
