package domain.account;

import java.math.BigDecimal;

import domain.Client;

public class SavingsAccount extends Account {

	public SavingsAccount(Client client, String accountNumber, String agencyNumber, BigDecimal balance,
			BigDecimal limit) {
		super(client, accountNumber, agencyNumber, balance, limit);
	}
	
	public void applyInterest() {    
		BigDecimal interestRate = new BigDecimal("0.005");
		BigDecimal interest = balance.multiply(interestRate);
		balance = balance.add(interest);
	}

}
