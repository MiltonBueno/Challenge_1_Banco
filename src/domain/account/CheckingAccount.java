package domain.account;

import java.math.BigDecimal;

import domain.Agency;
import domain.Client;
import util.BankingConfig;

public class CheckingAccount extends Account {

	public CheckingAccount(String accountNumber, Client client, Agency agency, BigDecimal balance) {
		super(accountNumber, client, agency, balance, BankingConfig.DEFAULT_CHECKING_ACCOUNT_LIMIT);
	}

	@Override
	public String getAccountType() {
		return "Checking Account";
	}
	
}
