package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.ClientDetails;
import com.denisborovkov.interfaces.ClientRepository;
import java.util.HashMap;
import java.util.Map;

public class ClientRepo implements ClientRepository {

    private final Map<Long, ClientDetails> clients = new HashMap<>();

    @Override
    public Map<Long, ClientDetails> getClientDatabase() {
        return new HashMap<>(clients);
    }

    public ClientDetails save(ClientDetails client) {
        clients.put(client.getId(), client);
        System.out.println("Client saved successfully!");
        return client;
    }

    public ClientDetails get(Long id) {
        return clients.get(id);
    }

    public void update(ClientDetails client) {
        clients.put(client.getId(), client);
        System.out.println("Client updated successfully!");
    }

    public void delete(Long id) {
        clients.remove(id);
    }

    @Override
    public ClientDetails getAll() {
        return (ClientDetails) clients.values();
    }

    public boolean isExists(Long id) {
        return clients.containsKey(id);
    }

    public int getCount() {
        return clients.size();
    }
}
