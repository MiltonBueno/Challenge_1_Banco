package repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import domain.transaction.Transaction;

/**
 * Repository for Transaction persistence operations.
 */
public interface TransactionRepository {
	
    void save(Transaction transaction);
    
    List<Transaction> findAll();
    
    Optional<Transaction> findById(UUID uuid);

	void deleteById(UUID uuid);
    
}
