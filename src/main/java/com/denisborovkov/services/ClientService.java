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
        validateClient(client);
        clientRepo.save(client);
    }

    public void updateClient(Long id, ClientDetails client) throws ClientNotFoundException {
        if (clientRepo.get(id) == null) {
            throw new ClientNotFoundException("Client with id " + id + " not found");
        }
        clientRepo.update(client);
        ui.println("Client updated");
    }

    public void deleteClient(Long id) {
        clientRepo.delete(id);
        ui.println("Client deleted");
    }

    public ClientDetails getAllClients() {
        return clientRepo.getAll();
    }

    public void validateClient(ClientDetails client) throws ClientRegistrationException {
        ValidationUtils.validateClient(client);
    }
}
