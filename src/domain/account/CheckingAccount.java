package domain.account;

import java.math.BigDecimal;

import domain.Client;

public class CheckingAccount extends Account {

	public CheckingAccount(Client client, String accountNumber, String agencyNumber, BigDecimal balance,
			BigDecimal limit) {
		super(client, accountNumber, agencyNumber, balance, BigDecimal.valueOf(1000));
	}
	
}
