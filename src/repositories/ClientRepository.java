package repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import domain.Client;

public interface ClientRepository {
	
    void save(Client client);
    
    List<Client> findAll();
    
    Optional<Client> findById(UUID uuid);

	void deleteById(UUID uuid);
    
}
