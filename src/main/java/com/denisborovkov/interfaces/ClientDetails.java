package com.denisborovkov.interfaces;

import java.util.UUID;
import com.denisborovkov.models.Client;

public interface ClientDetails {
    UUID getId();
    String getName();
    String getEmail();
    String getPhoneNumber();
    String getStatus();
    Client setName(String name);
    Client setEmail(String email);
    Client setPhoneNumber(String phoneNumber);
    Client setStatus(String status);
    Client build();
}
