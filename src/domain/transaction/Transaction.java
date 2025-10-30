package domain.transaction;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import domain.BaseEntity;
import domain.account.Account;
import domain.transaction.enums.TransactionType;

public class Transaction extends BaseEntity {
	
	private LocalDateTime transactionTime = LocalDateTime.now();
    private Double amount;
	private TransactionType type;
    private Account sourceAccount;
    private Account targetAccount;

	public Transaction(Double amount, TransactionType type, Account sourceAccount, Account targetAccount) {
		this.amount = amount;
		this.type = type;
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
	}

	public UUID getId() {
		return id;
	}
	
	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public Double getAmount() {
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
		return "Transaction [id=" + id + ", transactionTime=" + transactionTime + ", amount=" + amount + ", type=" + type
				+ ", sourceAccount=" + sourceAccount + ", targetAccount=" + targetAccount + "]";
	}
	
	
	
}
