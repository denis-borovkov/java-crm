package com.denisborovkov.interfaces;

import java.util.UUID;

public interface ClientRepository {
    void save(ClientDetails client);
    ClientDetails getClient(UUID id);
    void updateClient(ClientDetails client);
    void deleteClient(UUID id);
    void getAllClients();
}
