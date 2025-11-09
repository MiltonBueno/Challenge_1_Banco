package domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import domain.BaseIdEntity;
import domain.account.Account;
import exceptions.InvalidInputException;

public class Transaction extends BaseIdEntity {
	
	private final LocalDateTime transactionTime = LocalDateTime.now();
    private final BigDecimal amount;
	private final TransactionType type;
    private final Account sourceAccount;
    private final Account targetAccount;

	public Transaction(BigDecimal amount, TransactionType type, Account sourceAccount, Account targetAccount) {
		this.amount = amount;
		this.type = type;
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
	}
	
	public Transaction(BigDecimal amount, TransactionType type, Account account) {
		this.amount = amount;
		this.type = type;
		if(type == TransactionType.DEPOSIT) {
			this.targetAccount = account;
			this.sourceAccount = null;
		} else if(type == TransactionType.WITHDRAW) {
			this.sourceAccount = account;
			this.targetAccount = null;
		} else {
			throw new InvalidInputException("The transaction type is invalid");
		}
	}

	public UUID getId() {
		return id;
	}
	
	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public TransactionType getType() {
		return type;
	}

	public Account getSourceAccount() {
		return sourceAccount;
	}

	public Account getTargetAccount() {
		return targetAccount;
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
		Transaction other = (Transaction) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		String sourceAccountNumber = (sourceAccount != null) ? sourceAccount.getAccountNumber() : "N/A";
		String targetAccountNumber = (targetAccount != null) ? targetAccount.getAccountNumber() : "N/A";
		return "Transaction [id=" + id + ", transactionTime=" + transactionTime + ", amount=" + amount + ", type=" + type
				+ ", sourceAccount=" + sourceAccountNumber + ", targetAccount=" + targetAccountNumber + "]";
	}
	
}
