package repositories.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import domain.account.Account;
import repositories.AccountRepository;

public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> database = new ConcurrentHashMap<>();

    @Override
    public void save(Account account) {
        if (account == null || account.getAccountNumber() == null) {
            throw new IllegalArgumentException("Cannot save null account or account with null number");
        }
        database.put(account.getAccountNumber(), account);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        return Optional.ofNullable(database.get(accountNumber));
    }
}

