package config;

import java.math.BigDecimal;

import domain.Agency;
import domain.Client;
import domain.account.Account;
import domain.account.BusinessAccount;
import domain.account.CheckingAccount;
import services.AccountService;
import services.AgencyService;
import services.ClientService;

/**
 * Populates system with sample data: 2 agencies, 2 clients, 2 accounts, and 3 initial transactions.
 */
public class DataSeeder {
	
    private final AgencyService agencyService;
    private final ClientService clientService;
    private final AccountService accountService;

    public DataSeeder(AgencyService agencyService, ClientService clientService, AccountService accountService) {
        this.agencyService = agencyService;
        this.clientService = clientService;
        this.accountService = accountService;
    }
	
    public void seed() {
        Agency agency1 = new Agency("001");
        Agency agency2 = new Agency("002");
        agencyService.save(agency1);
        agencyService.save(agency2);

        Client client1 = new Client("Alice", "123");
        Client client2 = new Client("Bob", "123");
        clientService.save(client1);
        clientService.save(client2);

        Account acc1 = new CheckingAccount("0001", client1, agency1, new BigDecimal("1000"));
        Account acc2 = new BusinessAccount("0002", client2, agency2, new BigDecimal("5000"));
        accountService.save(acc1);
        accountService.save(acc2);
        
        agency1.addAccount(acc1);
        agency2.addAccount(acc2);
        agencyService.save(agency1);
        agencyService.save(agency2);
        
        client1.addAccount(acc1);
        client2.addAccount(acc2);
        clientService.save(client1);
        clientService.save(client2);
        
        accountService.deposit(acc1, new BigDecimal("500"));
        accountService.withdraw(acc1, new BigDecimal("200"));
        accountService.transfer(acc1, acc2, new BigDecimal("150"));
    }
}

