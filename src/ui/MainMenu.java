package ui;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import config.DataSeeder;
import domain.Agency;
import domain.Client;
import domain.account.Account;
import domain.transaction.Transaction;
import dto.AccountDTO;
import dto.ClientDTO;
import enums.CsvDelimiter;
import enums.LocaleFormat;
import exceptions.BusinessException;
import exceptions.DomainNotFoundException;
import services.AccountService;
import services.AgencyService;
import services.ClientService;
import services.TransactionService;
import util.BankingConfig;
import util.CsvExporter;
import util.NumberFormatter;
import util.ScreenUtil;

public class MainMenu {

    private final InputHandler inputHandler;
    private final AccountService accountService;
    private final ClientService clientService;
    private final AgencyService agencyService;
    private final TransactionService transactionService;

    public MainMenu(InputHandler inputHandler, AccountService accountService, ClientService clientService,
                    AgencyService agencyService, TransactionService transactionService) {
        this.inputHandler = inputHandler;
        this.accountService = accountService;
        this.clientService = clientService;
        this.agencyService = agencyService;
        this.transactionService = transactionService;
    }

    private Client loggedClient;
    private LocaleFormat localeFormat;
    private DataSeeder dataSeeder;
    
    public void start() {
        ScreenUtil.clearScreen();
        ScreenUtil.printSeparator();
        System.out.println("  Welcome to the Banking System");
        ScreenUtil.printSeparator();

        boolean populate = inputHandler.getConfirmation("\nDo you want to pre-populate the system with sample data?");
        if (populate) {
            populateData();
        }

        configureLocale();
        
        inputHandler.waitForEnter();

        while (true) {
            ScreenUtil.clearScreen();
            handleLoginOrRegister();
            
            boolean shouldExit = mainLoop();
            
            if (shouldExit) {
                ScreenUtil.clearScreen();
                ScreenUtil.printSeparator();
                System.out.println("  Thank you for using our services!");
                ScreenUtil.printSeparator();
                return;
            }
        }
    }
    
    private void populateData() {
    	dataSeeder = new DataSeeder(agencyService, clientService, accountService, transactionService);
    	dataSeeder.seed();
        System.out.println("System populated successfully!");
    }

    private void configureLocale() {
        System.out.println("\nSelect currency format:");
        System.out.println("1 - BR (R$ 1.234,56)");
        System.out.println("2 - US ($1,234.56)");
        int choice = inputHandler.getInt("Option");

        if(choice != 1 && choice != 2) {
        	System.out.println("The inserted number is not valid.");
        }
        
        this.localeFormat = LocaleFormat.valueOf(choice);
        System.out.println("\nLocale set to: " + localeFormat);
    }

    private void handleLoginOrRegister() {
    	while (true) {
            System.out.println("\n1 - Register new client");
            System.out.println("2 - Login with existing client");
            int choice = inputHandler.getInt("Select an option");

            if (choice == 1) {
                String name = inputHandler.getString("Enter your name");
                String password = inputHandler.getString("Enter your password");
                Client newClient = new Client(name, password);
                clientService.save(newClient);
                loggedClient = newClient;
                System.out.println("\nClient registered successfully! You are now logged in as " + newClient.getName());
                inputHandler.waitForEnter();
                return;
            } else if (choice == 2) {
                List<Client> clients = clientService.findAll();
                if (clients.isEmpty()) {
                    System.out.println("No clients found. Please register first.");
                    continue;
                }

                System.out.println("\nAvailable clients:");
                listAllClients(clients);

                int selection = inputHandler.getInt("Select client number");
                if (selection < 1 || selection > clients.size()) {
                    System.out.println("Invalid selection.");
                    continue;
                }

                Client selectedClient = clients.get(selection - 1);
                
                int attempts = 3;
                while (attempts > 0) {
                	attempts--;
                    String password = inputHandler.getString("Enter password for " + selectedClient.getName());
                    if (selectedClient.validatePassword(password)) {
                        loggedClient = selectedClient;
                        System.out.println("\nLogin successful! Welcome back, " + loggedClient.getName());
                        inputHandler.waitForEnter();
                        return;
                    }
                    System.out.println("Incorrect password. Attempts left: " + attempts);
                }
                System.out.println("Too many incorrect password attempts.");
                continue;
                
            } else {
            	System.out.println("The inserted number is not valid. Try again.");
            }
    	}
    }

    private void listAllClients(List<Client> clients) {
        for (int i = 0; i < clients.size(); i++) {
            System.out.println((i + 1) + " - " + new ClientDTO(clients.get(i)) + ")");
        }
    }

    private boolean mainLoop() {
        while (true) {
            ScreenUtil.clearScreen();
            ScreenUtil.printSeparator();
            System.out.println(" Main Menu - Welcome, " + loggedClient.getName());
            ScreenUtil.printSeparator();
            System.out.println("1 - Deposit");
            System.out.println("2 - Withdraw");
            System.out.println("3 - Transfer");
            System.out.println("4 - Update limit of an account");
            System.out.println("5 - View my accounts");
            System.out.println("6 - Create new account");
            System.out.println("7 - Export transactions (CSV)");
            System.out.println("8 - Logout");
            System.out.println("0 - Exit");
            ScreenUtil.printSeparator();

            int option = inputHandler.getInt("Select an option");


            try {
            	switch (option) {
	                case 1 -> deposit();
	                case 2 -> withdraw();
	                case 3 -> makeTransfer();
	                case 4 -> updateLimit();
	                case 5 -> listLoggedClientAccounts();
	                case 6 -> createNewAccount();
	                case 7 -> exportTransactions();
	                case 8 -> {
	                    System.out.println("\n Logging out...");
	                    loggedClient = null;
	                    return false; // Return false to indicate logout (not exit)
	                }
	                case 0 -> { 
	                    return true; // Return true to indicate exit
	                }
	                default -> System.out.println("Invalid option.");
	            }
            } catch (BusinessException | DomainNotFoundException e) {
                System.out.println("\nOperation failed: " + e.getMessage());
                inputHandler.waitForEnter();
            } catch (RuntimeException e) {
                System.out.println("\nUnexpected error: " + e.getMessage());
                inputHandler.waitForEnter();
            }
        }
    }

	private void deposit() {
        List<Account> allAccounts = accountService.findAll();
        if (allAccounts.isEmpty()) {
            System.out.println("\nNo accounts exist in the system. Please create an account first.");
            inputHandler.waitForEnter();
            return;
        }
        
        String accountNumber = inputHandler.getString("Enter account number for deposit (any account)");
        Account targetAccount = accountService.findByAccountNumber(accountNumber);
        BigDecimal amount = inputHandler.getBigDecimal("Amount to deposit");
        accountService.deposit(targetAccount, amount);
        
        System.out.println("\nDeposit successful!");
        if(loggedClient.getAccounts().contains(targetAccount)) {
            System.out.println("New balance: " + NumberFormatter.formatAmount(targetAccount.getBalance(), localeFormat));
        }
        inputHandler.waitForEnter();
    }

    private void withdraw() {
        if (loggedClient.getAccounts().isEmpty()) {
            System.out.println("\nYou don't have any accounts. Please create an account first.");
            inputHandler.waitForEnter();
            return;
        }
        
        String accountNumber = inputHandler.getString("Enter account number for withdraw (must be your account)");
        Account sourceAccount = accountService.findByAccountNumber(accountNumber);
        if (!loggedClient.getAccounts().contains(sourceAccount)) {
            System.out.println("\nYou don't own the inserted account, you can only withdraw from your own accounts.");
            inputHandler.waitForEnter();
            return;
        }

        BigDecimal value = inputHandler.getBigDecimal("Amount to withdraw");
        accountService.withdraw(sourceAccount, value, localeFormat);
        
        System.out.println("\nWithdraw successful!");
        System.out.println("New balance: " + NumberFormatter.formatAmount(sourceAccount.getBalance(), localeFormat));
        inputHandler.waitForEnter();
    }

    private void makeTransfer() {
        if (loggedClient.getAccounts().isEmpty()) {
            System.out.println("\nYou don't have any accounts. Please create an account first.");
            inputHandler.waitForEnter();
            return;
        }
        
        List<Account> allAccounts = accountService.findAll();
        if (allAccounts.size() < 2) {
            System.out.println("\nThere must be at least 2 accounts in the system to perform a transfer.");
            inputHandler.waitForEnter();
            return;
        }
        
        if (LocalTime.now().isAfter(BankingConfig.NIGHT_START) || LocalTime.now().isBefore(BankingConfig.NIGHT_END)) {
            System.out.println("\nAttention! Transfers above " +
                NumberFormatter.formatAmount(BankingConfig.NIGHT_TRANSFER_LIMIT, localeFormat) +
                " are not allowed between " +
                NumberFormatter.formatTime(BankingConfig.NIGHT_START) +
                " and " +
                NumberFormatter.formatTime(BankingConfig.NIGHT_END) + ".");
        }
        
        String sourceAccountNumber = inputHandler.getString("Source account number");
        Account source = accountService.findByAccountNumber(sourceAccountNumber);

        if (!loggedClient.getAccounts().contains(source)) {
            System.out.println("\nYou don't own the inserted account, you can only transfer from your own accounts.");
            inputHandler.waitForEnter();
            return;
        }

        String targetAccountNumber = inputHandler.getString("Target account number");
        
        if (sourceAccountNumber.equals(targetAccountNumber)) {
            System.out.println("\nYou cannot transfer to the same account.");
            inputHandler.waitForEnter();
            return;
        }
        
        Account target = accountService.findByAccountNumber(targetAccountNumber);

        BigDecimal value = inputHandler.getBigDecimal("Amount to transfer");

        accountService.transfer(source, target, value, localeFormat);
        
        System.out.println("\nTransfer completed successfully!");
        ScreenUtil.printSeparator();
        System.out.println("  TRANSFER RECEIPT");
        ScreenUtil.printSeparator();
        System.out.println("Amount: " + NumberFormatter.formatAmount(value, localeFormat));
        System.out.println("From: Account " + source.getAccountNumber() + " (Agency " + source.getAgency().getAgencyNumber() + ")");
        System.out.println("To: Account " + target.getAccountNumber() + " (Agency " + target.getAgency().getAgencyNumber() + ")");
        System.out.println("Recipient: " + target.getClient().getName());
        System.out.println("New balance: " + NumberFormatter.formatAmount(source.getBalance(), localeFormat));
        ScreenUtil.printSeparator();
        inputHandler.waitForEnter();
    }

    private void updateLimit() {
        if (loggedClient.getAccounts().isEmpty()) {
            System.out.println("\nYou don't have any accounts. Please create an account first.");
            inputHandler.waitForEnter();
            return;
        }
        
        String accountNumber = inputHandler.getString("Account number");
        Account account = accountService.findByAccountNumber(accountNumber);
        if (!loggedClient.getAccounts().contains(account)) {
            System.out.println("\nYou don't own the inserted account, you can only update limits of your accounts.");
            inputHandler.waitForEnter();
            return;
        }
        BigDecimal newLimit = inputHandler.getBigDecimal("New limit");
        accountService.updateLimit(account, newLimit);
        
        System.out.println("\nLimit updated successfully!");
        System.out.println("New limit: " + NumberFormatter.formatAmount(newLimit, localeFormat));
        inputHandler.waitForEnter();
    }

    private void listLoggedClientAccounts() {
        List<Account> accounts = loggedClient.getAccounts();
        if (accounts == null || accounts.isEmpty()) {
            System.out.println("\nYou have no accounts.");
            inputHandler.waitForEnter();
            return;
        }
        
        System.out.println("\n" + loggedClient.getName() + "'s Accounts:");
        ScreenUtil.printSeparator();
        accounts.forEach(account -> {
            System.out.println(account.getAccountType() + " - #" + account.getAccountNumber());
            System.out.println("  Agency: " + account.getAgency().getAgencyNumber());
            System.out.println("  Balance: " + NumberFormatter.formatAmount(account.getBalance(), localeFormat));
            System.out.println("  Limit: " + NumberFormatter.formatAmount(account.getLimit(), localeFormat));
            System.out.println("  Transactions: " + account.getTransactions().size());
            System.out.println();
        });
        ScreenUtil.printSeparator();
        inputHandler.waitForEnter();
    }

    private void createNewAccount() {
        String agencyNumber = inputHandler.getString("Insert the agency number (it can be a new one or an existing one)");
        String accountNumber = inputHandler.getString("Now insert the account number (it can't be an existing one)");

        while (accountNumberExists(accountNumber)) {
            System.out.println("An account with this number already exists. Please choose a different one.");
            accountNumber = inputHandler.getString("Insert the account number (it can't be an existing one)");
        }

        System.out.println("\nNow select the account type:");
        System.out.println("1 - Business Account");
        System.out.println("2 - Checking Account");
        System.out.println("3 - Savings Account");
        int choice = inputHandler.getInt("Option");

        while(choice != 1 && choice != 2 && choice != 3) {
        	System.out.println("The inserted number is not valid.");
            choice = inputHandler.getInt("Option");
        }
        
        BigDecimal initialBalance = inputHandler.getBigDecimal("How much would you like to deposit to open this account");
        
        while (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            initialBalance = inputHandler.getBigDecimal("The deposit amount cannot be negative. Please enter a valid amount");
        }

        Agency agency = agencyService.findOrCreate(agencyNumber);
        AccountDTO accountDTO;
        
        if(choice == 1) {
        	accountDTO = accountService.createBusinessAccount(accountNumber, loggedClient, agency, initialBalance);
        } else if(choice == 2) {
        	accountDTO = accountService.createCheckingAccount(accountNumber, loggedClient, agency, initialBalance);
        } else {
        	accountDTO = accountService.createSavingsAccount(accountNumber, loggedClient, agency, initialBalance);
        }
        
        System.out.println("\nCongratulations! Account created successfully!");
        ScreenUtil.printSeparator();
    	System.out.println(accountDTO);
        ScreenUtil.printSeparator();
        inputHandler.waitForEnter();
    }
    
    private boolean accountNumberExists(String accountNumber) {
        return accountService.findAll().stream()
            .anyMatch(account -> account.getAccountNumber().equals(accountNumber));
    }

    private void exportTransactions() {
        System.out.println("1 - Export transactions for a single account");
        System.out.println("2 - Export transactions for all my accounts");
        int choice = inputHandler.getInt("Option");

        List<Transaction> toExport;

        if (choice == 1) {
            String accountNumber = inputHandler.getString("Enter account number");
            Account account = accountService.findByAccountNumber(accountNumber);
            toExport = transactionService.findByAccount(account);
            if (toExport.isEmpty()) {
                System.out.println("\nNo transactions found for this account.");
                inputHandler.waitForEnter();
                return;
            }
        } else if (choice == 2) {
            List<Account> accounts = loggedClient.getAccounts();
            toExport = accounts.stream()
                    .flatMap(acc -> transactionService.findByAccount(acc).stream())
                    .collect(Collectors.toList());
            if (toExport.isEmpty()) {
                System.out.println("\nNo transactions found for your accounts.");
                inputHandler.waitForEnter();
                return;
            }
        } else {
            System.out.println("\nInvalid option.");
            inputHandler.waitForEnter();
            return;
        }

        System.out.println("\nSelect CSV delimiter:");
        System.out.println("1 - Comma (,) - Use this for English Excel");
        System.out.println("2 - Semicolon (;) - Use this for Portuguese Excel");
        int delimiterChoice = inputHandler.getInt("Option");
        
        CsvDelimiter delimiter;
        try {
            delimiter = CsvDelimiter.valueOf(delimiterChoice);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid delimiter option. Using comma as default.");
            delimiter = CsvDelimiter.COMMA;
        }

        String path = inputHandler.getString("Enter file path where CSV will be saved (example: C:\\temp\\transactions.csv)");
        File file = new File(path);
        try {
            CsvExporter.exportTransactionsToCsv(toExport, file, localeFormat, delimiter);
            System.out.println("\nCSV exported successfully!");
            System.out.println("Location: " + file.getAbsolutePath());
            System.out.println("Transactions exported: " + toExport.size());
        } catch (RuntimeException e) {
            System.out.println("\nFailed to export CSV: " + e.getMessage());
        }
        inputHandler.waitForEnter();
    }

    
}

