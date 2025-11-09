package domain.account;

import java.math.BigDecimal;

import domain.Agency;
import domain.Client;
import util.BankingConfig;

public class BusinessAccount extends Account{
	
	public BusinessAccount(String accountNumber, Client client, Agency agency, BigDecimal balance) {
		super(accountNumber, client, agency, balance, BankingConfig.DEFAULT_BUSINESS_ACCOUNT_LIMIT);
	}

	@Override
	public void withdrawValue(BigDecimal amount) {
		BigDecimal fee = calculateFee(amount);
	    balance = balance.subtract(amount.add(fee));
	}

	@Override
	public void depositValue(BigDecimal amount) {
		BigDecimal fee = calculateFee(amount);
	    balance = balance.add(amount.subtract(fee));
	}

	@Override
	public void transferValue(BigDecimal amount, Account targetAccount) {
		BigDecimal fee = calculateFee(amount);
	    balance = balance.subtract(amount.add(fee));
	    targetAccount.receiveTransfer(amount);
	}
	
	private BigDecimal calculateFee(BigDecimal amount) {
		BigDecimal percentualFee = BankingConfig.BUSINESS_ACCOUNT_PERCENTUAL_FEE;
		BigDecimal fee = amount.multiply(percentualFee);
		return fee;
	}
	
	public BigDecimal getMaxAvailableWithFee() {
		BigDecimal totalAvailable = balance.add(limit);
		BigDecimal feeMultiplier = BigDecimal.ONE.add(BankingConfig.BUSINESS_ACCOUNT_PERCENTUAL_FEE);
		return totalAvailable.divide(feeMultiplier, 2, java.math.RoundingMode.DOWN);
	}

	@Override
	public String getAccountType() {
		return "Business Account";
	}

}
