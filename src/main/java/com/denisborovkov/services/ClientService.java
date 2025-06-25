package com.denisborovkov.services;

import com.denisborovkov.ConsoleUI;
import com.denisborovkov.exceptions.ClientNotFoundException;
import com.denisborovkov.exceptions.ClientRegistrationException;
import com.denisborovkov.interfaces.ClientDetails;
import com.denisborovkov.interfaces.ClientRepository;
import com.denisborovkov.interfaces.ClientServiceDetails;
import com.denisborovkov.utils.ValidationUtils;

public class ClientService implements ClientServiceDetails {
    private final ConsoleUI ui;
    private final ClientRepository clientRepo;

    public ClientService(ConsoleUI ui, ClientRepository clientRepo) {
        this.ui = ui;
        this.clientRepo = clientRepo;
    }

    public void createClient(ClientDetails client) throws ClientRegistrationException {
        if (client == null) {
            throw new ClientRegistrationException("Client cannot be null");
        }
        validateClient(client);
        clientRepo.save(client);
    }

    public ClientDetails getClient(Long id) throws ClientNotFoundException {
        if (id == null) {
            throw new ClientNotFoundException("Client cannot be null");
        }
        return clientRepo.get(id);
    }

    public void updateClient(ClientDetails client) throws ClientNotFoundException {
        if (client == null) {
            throw new ClientNotFoundException("Client cannot be null");
        }
        clientRepo.update(client);
        ui.println("Client updated");
    }

    public void deleteClient(Long id) throws ClientNotFoundException {
        if (id == null) {
            throw new ClientNotFoundException("Client cannot be null");
        }
        clientRepo.delete(id);
        ui.println("Client deleted");
    }

    public void getAllClients() {
        clientRepo.getAll();
    }

    public void validateClient(ClientDetails client) throws ClientRegistrationException {
        ValidationUtils.validateClient(client);
    }
}
