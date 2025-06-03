package com.denisborovkov.interfaces;

import com.denisborovkov.exceptions.ClientNotFoundException;
import com.denisborovkov.exceptions.ClientRegistrationException;

import java.util.UUID;

public interface ClientServiceDetails {
    void createClient(ClientDetails client) throws ClientRegistrationException;
    ClientDetails getClient(UUID id) throws ClientNotFoundException;
    void updateClient(ClientDetails client) throws ClientNotFoundException ;
    void deleteClient(UUID id) throws ClientNotFoundException;
    void getAllClients();
}
