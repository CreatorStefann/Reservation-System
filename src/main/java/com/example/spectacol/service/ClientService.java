package com.example.spectacol.service;

import com.example.spectacol.dto.UpdateClientRequest;
import com.example.spectacol.model.Client;
import com.example.spectacol.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {

        clientRepository.findByEmail(client.getEmail())
                .ifPresent(c -> {
                    throw new RuntimeException("Email already exists");
                });

        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public Client updateClient(Long id, UpdateClientRequest request) {
        Client client = getClientById(id);

        // Check if new email already exists (and it's not the same client)
        clientRepository.findByEmail(request.getEmail())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        throw new RuntimeException("Email already exists");
                    }
                });

        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());

        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        Client client = getClientById(id);
        clientRepository.delete(client);
    }
}


