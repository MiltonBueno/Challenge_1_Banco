package repositories.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import domain.Client;
import repositories.ClientRepository;

public class InMemoryClientRepository implements ClientRepository {

    private final Map<UUID, Client> database = new ConcurrentHashMap<>();

    @Override
    public void save(Client client) {
        if (client == null || client.getId() == null) {
            throw new IllegalArgumentException("Cannot save null client or client with null id");
        }
        database.put(client.getId(), client);
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<Client> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Client ID cannot be null");
        }
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Client ID cannot be null");
        }

        if (!database.containsKey(id)) {
            throw new NoSuchElementException("Client not found for id: " + id);
        }

        database.remove(id);
    }
    
}
