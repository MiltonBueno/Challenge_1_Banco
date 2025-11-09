package domain.account;

import java.math.BigDecimal;

import domain.Agency;
import domain.Client;
import util.BankingConfig;

public class SavingsAccount extends Account {

	public SavingsAccount(String accountNumber, Client client, Agency agency, BigDecimal balance) {
		super(accountNumber, client, agency, balance, BankingConfig.DEFAULT_SAVINGS_ACCOUNT_LIMIT);
	}
	
	public void applyInterest() {    
		BigDecimal interestRate = BankingConfig.SAVINGS_ACCOUNT_INTEREST_RATE;
		BigDecimal interest = balance.multiply(interestRate);
		balance = balance.add(interest);
	}

	@Override
	public String getAccountType() {
		return "Savings Account";
	}

}
