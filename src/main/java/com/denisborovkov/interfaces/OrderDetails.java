package com.denisborovkov.interfaces;

import com.denisborovkov.models.Client;
import com.denisborovkov.models.Order;
import java.time.LocalDate;

public interface OrderDetails {
    int getId();
    Order setClient(Client client);
    Order setDescription(String description);
    Order setPrice(double price);
    Order setStatus(String status);
    Order setCreatedAt(LocalDate createdAt);
    Order build();
}
