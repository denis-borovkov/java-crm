package com.denisborovkov.models;

import com.denisborovkov.interfaces.OrderDetails;
import java.time.LocalDate;
import java.util.UUID;

public class Order implements OrderDetails {
    private UUID id;
    private Client client;
    private String description;
    private double price;
    private String status;
    private LocalDate createdAt;

    public Order() {}

    public Order(UUID id, Client client, String description, double price, String status, LocalDate createdAt) {
        this.id = id;
        this.client = client;
        this.description = description;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Order setId() {
        return this;
    }

    public Client getClient() {
        return client;
    }

    public Order setClient(Client client) {
        this.client = client;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Order setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Order setPrice(double price) {
        this.price = price;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Order setStatus(String status) {
        this.status = status;
        return this;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Order setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Order build() {
        return new Order(id, client, description, price, status, createdAt);
    }
}
