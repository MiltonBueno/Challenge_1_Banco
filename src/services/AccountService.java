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
import enums.LocaleFormat;
import exceptions.BusinessException;
import exceptions.DomainNotFoundException;
import exceptions.InsufficientBalanceException;
import repositories.AccountRepository;
import util.BankingConfig;
import util.NumberFormatter;

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

    public void transfer(Account source, Account target, BigDecimal amount, LocaleFormat localeFormat) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Transfer amount must be positive");
        }

        if (LocalTime.now().isAfter(BankingConfig.NIGHT_START) || LocalTime.now().isBefore(BankingConfig.NIGHT_END)) {
            if (amount.compareTo(BankingConfig.NIGHT_TRANSFER_LIMIT) > 0) {
                throw new BusinessException("Transfers above " +
                        NumberFormatter.formatAmount(BankingConfig.NIGHT_TRANSFER_LIMIT, localeFormat) +
                        " are not allowed between " +
                        NumberFormatter.formatTime(BankingConfig.NIGHT_START) +
                        " and " +
                        NumberFormatter.formatTime(BankingConfig.NIGHT_END) + ".");
            }
        }

        BigDecimal maxAvailable;
        if (source instanceof BusinessAccount) {
            maxAvailable = ((BusinessAccount) source).getMaxAvailableWithFee();
        } else {
            maxAvailable = source.getBalance().add(source.getLimit());
        }
        
        if (maxAvailable.compareTo(amount) < 0) {
            String errorMsg = String.format(
                "Insufficient balance for transfer.\n" +
                "Current balance: %s\n" +
                "Credit limit: %s\n" +
                "Maximum available to transfer: %s",
                NumberFormatter.formatAmount(source.getBalance(), localeFormat),
                NumberFormatter.formatAmount(source.getLimit(), localeFormat),
                NumberFormatter.formatAmount(maxAvailable, localeFormat)
            );
            throw new InsufficientBalanceException(errorMsg);
        }

        source.transferValue(amount, target);
        accountRepository.save(source);
        accountRepository.save(target);

        transactionService.registerTransfer(amount, source, target);
    }
    
    public void deposit(Account targetAccount, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Deposit amount must be positive");
        }

        targetAccount.depositValue(amount);
        accountRepository.save(targetAccount);

        transactionService.deposit(amount, targetAccount);
    }

    public void withdraw(Account sourceAccount, BigDecimal amount, LocaleFormat localeFormat) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Withdrawal amount must be positive");
        }

        BigDecimal maxAvailable;
        if (sourceAccount instanceof BusinessAccount) {
            maxAvailable = ((BusinessAccount) sourceAccount).getMaxAvailableWithFee();
        } else {
            maxAvailable = sourceAccount.getBalance().add(sourceAccount.getLimit());
        }
        
        if (maxAvailable.compareTo(amount) < 0) {
            String errorMsg = String.format(
                "Insufficient balance for withdrawal.\n" +
                "Current balance: %s\n" +
                "Credit limit: %s\n" +
                "Maximum available to withdraw: %s",
                NumberFormatter.formatAmount(sourceAccount.getBalance(), localeFormat),
                NumberFormatter.formatAmount(sourceAccount.getLimit(), localeFormat),
                NumberFormatter.formatAmount(maxAvailable, localeFormat)
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
    	
    	save(newBusinessAccount);
    	loggedClient.addAccount(newBusinessAccount);
    	agency.addAccount(newBusinessAccount);
    	clientService.save(loggedClient);
    	agencyService.save(agency);
    	
    	return new AccountDTO(newBusinessAccount);
    	
    }
    
    public AccountDTO createCheckingAccount(String accountNumber, Client loggedClient, Agency agency, BigDecimal initialBalance) {
    	
    	Account newChekingAccount = new CheckingAccount(accountNumber, loggedClient, agency, initialBalance);
    	
    	save(newChekingAccount);
    	loggedClient.addAccount(newChekingAccount);
    	agency.addAccount(newChekingAccount);
    	clientService.save(loggedClient);
    	agencyService.save(agency);
    	
    	return new AccountDTO(newChekingAccount);
    	
    }
    
    public AccountDTO createSavingsAccount(String accountNumber, Client loggedClient, Agency agency, BigDecimal initialBalance) {
    	
    	Account newSavingsAccount = new SavingsAccount(accountNumber, loggedClient, agency, initialBalance);
    	
    	save(newSavingsAccount);
    	loggedClient.addAccount(newSavingsAccount);
    	agency.addAccount(newSavingsAccount);
    	clientService.save(loggedClient);
    	agencyService.save(agency);
    	
    	return new AccountDTO(newSavingsAccount);
    	
    }
    
}