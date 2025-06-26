package com.denisborovkov.models;

import com.denisborovkov.interfaces.ClientDetails;

public class Client implements ClientDetails {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String status;

    public Client() {}

    public Client(Long id, String name, String email, String phoneNumber, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Client setId() {
        this.id = System.currentTimeMillis();
        return this;
    }

    public String getName() {
        return name;
    }

    public Client setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Client setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Client setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Client setStatus(String status) {
        this.status = status;
        return this;
    }

    public Client build() {
        return new Client(id, name, email, phoneNumber, status);
    }
}
