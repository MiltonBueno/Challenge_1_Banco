package application;

import repositories.AccountRepository;
import repositories.AgencyRepository;
import repositories.ClientRepository;
import repositories.TransactionRepository;
import repositories.memory.InMemoryAccountRepository;
import repositories.memory.InMemoryAgencyRepository;
import repositories.memory.InMemoryClientRepository;
import repositories.memory.InMemoryTransactionRepository;
import services.AccountService;
import services.AgencyService;
import services.ClientService;
import services.TransactionService;
import ui.InputHandler;
import ui.MainMenu;

public class Program {

	public static void main(String[] args) {
		
        AccountRepository accountRepository = new InMemoryAccountRepository();
        ClientRepository clientRepository = new InMemoryClientRepository();
        AgencyRepository agencyRepository = new InMemoryAgencyRepository();
        TransactionRepository transactionRepository = new InMemoryTransactionRepository();

        TransactionService transactionService = new TransactionService(transactionRepository);
        ClientService clientService = new ClientService(clientRepository);
        AgencyService agencyService = new AgencyService(agencyRepository);
        AccountService accountService = new AccountService(accountRepository, transactionService, clientService, agencyService);

        InputHandler inputHandler = new InputHandler();
        MainMenu mainMenu = new MainMenu(inputHandler, accountService, clientService, agencyService, transactionService);

        mainMenu.start();
		
	}

}
