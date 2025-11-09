package domain.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import domain.Agency;
import domain.Client;
import domain.transaction.Transaction;

public abstract class Account{

	private final String accountNumber;
	private final Client client;
	private final Agency agency;
    protected BigDecimal balance = BigDecimal.ZERO;
    protected BigDecimal limit = BigDecimal.ZERO;
	private final List<Transaction> transactions = new ArrayList<>();

	public Account(String accountNumber, Client client, Agency agency, BigDecimal balance, BigDecimal limit) {
		this.accountNumber = accountNumber;
		this.client = client;
		this.agency = agency;
		this.balance = balance;
		this.limit = limit;
	}

	public Client getClient() {
		return client;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public Agency getAgency() {
		return agency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getLimit() {
		return limit;
	}

	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}
	
	public void withdrawValue(BigDecimal amount) {
	    balance = balance.subtract(amount);
	}
	
	public void depositValue(BigDecimal amount) {
	    balance = balance.add(amount);
	}
	
	public void receiveTransfer(BigDecimal amount) {
	    balance = balance.add(amount);
	}
	
	public void transferValue(BigDecimal amount, Account targetAccount) {
	    balance = balance.subtract(amount);
	    targetAccount.receiveTransfer(amount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return Objects.equals(accountNumber, other.accountNumber);
	}

	public abstract String getAccountType();

	@Override
	public String toString() {
		return getAccountType() + " #" + accountNumber + 
		       " | Owner: " + client.getName() + 
		       " | Agency: " + agency.getAgencyNumber() + 
		       " | Balance: " + balance + 
		       " | Limit: " + limit;
	}
	
}
