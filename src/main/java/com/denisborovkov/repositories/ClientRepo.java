package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.ClientDetails;
import com.denisborovkov.interfaces.ClientRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientRepo implements ClientRepository {

    private final Map<UUID, ClientDetails> clients = new HashMap<>();

    public void save(ClientDetails client) {
        clients.put(client.getId(), client);
        System.out.println("Client saved successfully!");
    }

    public ClientDetails getClient(UUID id) {
        return clients.get(id);
    }

    public void updateClient(ClientDetails client) {
        clients.put(client.getId(), client);
        System.out.println("Client updated successfully!");
    }

    public void deleteClient(UUID id) {
        clients.remove(id);
    }

    public void getAllClients() {
    }
}
