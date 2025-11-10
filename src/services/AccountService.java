package services;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import domain.Agency;
import domain.Client;
import domain.account.Account;
import domain.account.BusinessAccount;
import domain.account.CheckingAccount;
import domain.account.SavingsAccount;
import dto.AccountDTO;
import exceptions.BusinessException;
import exceptions.DomainNotFoundException;
import exceptions.InsufficientBalanceException;
import repositories.AccountRepository;
import util.BankingConfig;
import util.NumberFormatter;

/**
 * Service layer for managing bank account operations.
 * Manages business logic for deposits, withdrawals, transfers
 * and account creation with validation of business rules.
 */
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final ClientService clientService;
    private final AgencyService agencyService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService, 
    		ClientService clientService, AgencyService agencyService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.clientService = clientService;
        this.agencyService = agencyService;
    }

    public void save(Account account) {
        if (account == null || account.getAccountNumber() == null) {
            throw new BusinessException("Invalid account data");
        }

        accountRepository.save(account);
    }

    public Account findByAccountNumber(String number) {
        return accountRepository.findByAccountNumber(number)
                .orElseThrow(() -> new DomainNotFoundException("Account not found for number: " + number));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    /**
     * Transfers money between accounts with validation of night limit (R$ 1,000 between 20:00-06:00)
     * and sufficient balance. Currency format is determined by the global locale configuration.
     * 
     * @param source the source account
     * @param target the target account
     * @param amount the amount to transfer
     * @throws BusinessException if amount is invalid or night limit exceeded
     * @throws InsufficientBalanceException if balance is insufficient
     * @see BusinessAccount for fee information
     */
    public void transfer(Account source, Account target, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Transfer amount must be positive");
        }

        if (LocalTime.now().isAfter(BankingConfig.NIGHT_START) || LocalTime.now().isBefore(BankingConfig.NIGHT_END)) {
            if (amount.compareTo(BankingConfig.NIGHT_TRANSFER_LIMIT) > 0) {
                throw new BusinessException("Transfers above " +
                        NumberFormatter.formatAmount(BankingConfig.NIGHT_TRANSFER_LIMIT) +
                        " are not allowed between " +
                        NumberFormatter.formatTime(BankingConfig.NIGHT_START) +
                        " and " +
                        NumberFormatter.formatTime(BankingConfig.NIGHT_END) + ".");
            }
        }

        BigDecimal maxAvailable = source.getMaxAvailable();
        
        if (maxAvailable.compareTo(amount) < 0) {
            String errorMsg = String.format(
                "Insufficient balance for transfer.\n" +
                "Current balance: %s\n" +
                "Credit limit: %s\n" +
                "Maximum available to transfer: %s",
                NumberFormatter.formatAmount(source.getBalance()),
                NumberFormatter.formatAmount(source.getLimit()),
                NumberFormatter.formatAmount(maxAvailable)
            );
            throw new InsufficientBalanceException(errorMsg);
        }

        source.transferValue(amount, target);
        accountRepository.save(source);
        accountRepository.save(target);

        transactionService.registerTransfer(amount, source, target);
    }
    
    /**
     * Deposits money into an account.
     * 
     * @param targetAccount the account to receive the deposit
     * @param amount the amount to deposit
     * @throws BusinessException if amount is invalid
     * @see BusinessAccount for fee information
     */
    public void deposit(Account targetAccount, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Deposit amount must be positive");
        }

        targetAccount.depositValue(amount);
        accountRepository.save(targetAccount);

        transactionService.deposit(amount, targetAccount);
    }

    /**
     * Withdraws money from an account with balance and limit validation.
     * Currency format is determined by the global locale configuration.
     * 
     * @param sourceAccount the account to withdraw from
     * @param amount the amount to withdraw
     * @throws BusinessException if amount is invalid
     * @throws InsufficientBalanceException if balance is insufficient
     * @see BusinessAccount for fee information
     */
    public void withdraw(Account sourceAccount, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Withdrawal amount must be positive");
        }

        BigDecimal maxAvailable = sourceAccount.getMaxAvailable();
        
        if (maxAvailable.compareTo(amount) < 0) {
            String errorMsg = String.format(
                "Insufficient balance for withdrawal.\n" +
                "Current balance: %s\n" +
                "Credit limit: %s\n" +
                "Maximum available to withdraw: %s",
                NumberFormatter.formatAmount(sourceAccount.getBalance()),
                NumberFormatter.formatAmount(sourceAccount.getLimit()),
                NumberFormatter.formatAmount(maxAvailable)
            );
            throw new InsufficientBalanceException(errorMsg);
        }

        sourceAccount.withdrawValue(amount);
        accountRepository.save(sourceAccount);

        transactionService.withdraw(amount, sourceAccount);
    }

    public void updateLimit(Account account, BigDecimal newLimit) {
        if (newLimit == null || newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Limit must be zero or positive");
        }

        account.setLimit(newLimit);
        accountRepository.save(account);
    }
    
    public AccountDTO createBusinessAccount(String accountNumber, Client loggedClient, Agency agency, BigDecimal initialBalance) {
    	Account newBusinessAccount = new BusinessAccount(accountNumber, loggedClient, agency, initialBalance);
    	return registerNewAccount(newBusinessAccount, loggedClient, agency);
    }
    
    public AccountDTO createCheckingAccount(String accountNumber, Client loggedClient, Agency agency, BigDecimal initialBalance) {
    	Account newCheckingAccount = new CheckingAccount(accountNumber, loggedClient, agency, initialBalance);
    	return registerNewAccount(newCheckingAccount, loggedClient, agency);
    }
    
    public AccountDTO createSavingsAccount(String accountNumber, Client loggedClient, Agency agency, BigDecimal initialBalance) {
    	Account newSavingsAccount = new SavingsAccount(accountNumber, loggedClient, agency, initialBalance);
    	return registerNewAccount(newSavingsAccount, loggedClient, agency);
    }
    
    private AccountDTO registerNewAccount(Account account, Client client, Agency agency) {
        save(account);
        client.addAccount(account);
        agency.addAccount(account);
        clientService.save(client);
        agencyService.save(agency);
        return new AccountDTO(account);
    }
    
}