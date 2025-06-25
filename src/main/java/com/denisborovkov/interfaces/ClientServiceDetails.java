package com.denisborovkov.interfaces;

import com.denisborovkov.exceptions.ClientNotFoundException;
import com.denisborovkov.exceptions.ClientRegistrationException;

public interface ClientServiceDetails {
    void createClient(ClientDetails client) throws ClientRegistrationException;
    ClientDetails getClient(Long id) throws ClientNotFoundException;
    void updateClient(ClientDetails client) throws ClientNotFoundException ;
    void deleteClient(Long id) throws ClientNotFoundException;
    void getAllClients();
}
