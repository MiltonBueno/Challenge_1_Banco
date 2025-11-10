package repositories;

import java.util.List;
import java.util.Optional;

import domain.account.Account;

/**
 * Repository for Account persistence operations.
 */
public interface AccountRepository {
	
    void save(Account account);
    
    List<Account> findAll();
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
}  
