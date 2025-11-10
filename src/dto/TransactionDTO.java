package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import domain.transaction.Transaction;
import domain.transaction.TransactionType;
import util.NumberFormatter;

public class TransactionDTO {
	
	private final String id;
	private final TransactionType type;
	private final BigDecimal amount;
	private final LocalDateTime transactionTime;
	private final AccountDTO sourceAccount;
	private final AccountDTO targetAccount;

	public TransactionDTO(Transaction transaction) {
		this.id = transaction.getId().toString();
		this.type = transaction.getType();
		this.amount = transaction.getAmount();
		this.transactionTime = transaction.getTransactionTime();
		this.sourceAccount = (transaction.getSourceAccount() != null) ? new AccountDTO(transaction.getSourceAccount()) : null;
		this.targetAccount = (transaction.getTargetAccount() != null) ? new AccountDTO(transaction.getTargetAccount()) : null;
	}

	public String getId() {
		return id;
	}

	public TransactionType getType() {
		return type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public AccountDTO getSourceAccount() {
		return sourceAccount;
	}

	public AccountDTO getTargetAccount() {
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
		TransactionDTO other = (TransactionDTO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		String sourceInfo = (sourceAccount != null) ? sourceAccount.getAccountNumber() : "N/A";
		String targetInfo = (targetAccount != null) ? targetAccount.getAccountNumber() : "N/A";
		return type + " | Amount: " + NumberFormatter.formatAmount(amount) + " | From: " + sourceInfo + " | To: " + targetInfo + " | Time: " + NumberFormatter.formatDateTime(transactionTime);
	}
	
}