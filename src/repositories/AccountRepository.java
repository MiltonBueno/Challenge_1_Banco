package repositories;

import java.util.List;
import java.util.Optional;

import domain.account.Account;

public interface AccountRepository {
	
    void save(Account account);
    
    List<Account> findAll();
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
}  
