package domain.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import domain.BaseEntity;
import domain.Client;
import domain.transaction.Transaction;

public abstract class Account extends BaseEntity {
	
	private Client client;
	private String accountNumber;
	private String agencyNumber;
    protected BigDecimal balance = BigDecimal.ZERO;
    protected BigDecimal limit = BigDecimal.ZERO;
	private List<Transaction> transactions = new ArrayList<>();

	public Account(Client client, String accountNumber, String agencyNumber, BigDecimal balance, BigDecimal limit) {
		this.client = client;
		this.accountNumber = accountNumber;
		this.agencyNumber = agencyNumber;
		this.balance = balance;
		this.limit = limit;
	}

	public UUID getId() {
		return id;
	}

	public Client getClient() {
		return client;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getAgencyNumber() {
		return agencyNumber;
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

	public void removeTransaction(Transaction transaction) {
		transactions.remove(transaction);
	}
	
	public void withdrawValue(String value) {
	    BigDecimal withdrawedValue = new BigDecimal(value);
	    balance = balance.subtract(withdrawedValue);
	}
	
	public void depositValue(String value) {
	    BigDecimal addedValue = new BigDecimal(value);
	    balance = balance.add(addedValue);
	}
	
	public void transferValue(String value, Account targetAccount) {
	    BigDecimal transferedValue = new BigDecimal(value);
	    balance = balance.subtract(transferedValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", client=" + client + ", accountNumber=" + accountNumber + ", agencyNumber=" + agencyNumber
				+ ", balance=" + balance + ", limit=" + limit + ", transactions=" + transactions + "]";
	}
	
}
