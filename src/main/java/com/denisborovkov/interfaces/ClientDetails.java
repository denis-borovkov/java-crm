package com.denisborovkov.interfaces;

import com.denisborovkov.models.Client;

public interface ClientDetails {
    Long getId();
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
