package repositories.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import domain.transaction.Transaction;
import repositories.TransactionRepository;

public class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<UUID, Transaction> database = new ConcurrentHashMap<>();

    @Override
    public void save(Transaction transaction) {
        if (transaction == null || transaction.getId() == null) {
            throw new IllegalArgumentException("Cannot save null transaction or transaction with null id");
        }
        database.put(transaction.getId(), transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }

        if (!database.containsKey(id)) {
            throw new NoSuchElementException("Transaction not found for id: " + id);
        }

        database.remove(id);
    }
    
}