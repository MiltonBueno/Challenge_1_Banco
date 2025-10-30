package domain.account;

import java.math.BigDecimal;

import domain.Client;

public class BusinessAccount extends Account{
	
	public BusinessAccount(Client client, String accountNumber, String agencyNumber, BigDecimal balance,
			BigDecimal limit) {
		super(client, accountNumber, agencyNumber, balance, BigDecimal.valueOf(1000));
	}
	
	

}
