package services;

import java.util.List;
import java.util.UUID;

import domain.Client;
import exceptions.BusinessException;
import exceptions.DomainNotFoundException;
import repositories.ClientRepository;

public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void save(Client client) {
        if (client == null || client.getName() == null || client.getName().isBlank()) {
            throw new BusinessException("Client name cannot be null or empty");
        }
        clientRepository.save(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Client not found for id: " + id));
    }

    public void deleteById(UUID id) {
        clientRepository.deleteById(id);
    }

}

