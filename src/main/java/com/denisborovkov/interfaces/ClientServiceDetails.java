package com.denisborovkov.interfaces;

import com.denisborovkov.exceptions.ClientNotFoundException;
import com.denisborovkov.exceptions.ClientRegistrationException;

public interface ClientServiceDetails {
    void createClient(ClientDetails client) throws ClientRegistrationException;
    void updateClient(Long id, ClientDetails client) throws ClientNotFoundException;
    void deleteClient(Long id);
    ClientDetails getAllClients();
    void validateClient(ClientDetails client) throws ClientRegistrationException;


}
