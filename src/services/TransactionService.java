package services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import domain.account.Account;
import domain.transaction.Transaction;
import domain.transaction.TransactionType;
import exceptions.BusinessException;
import exceptions.DomainNotFoundException;
import repositories.TransactionRepository;

/**
 * Service layer for managing and registering transaction operations.
 * This class don't directly alter informations of the accounts like balance,
 * its main purpose is to manage and save all the information about the transactions.
 */
public class TransactionService {

	private final TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public void registerDeposit(BigDecimal amount, Account account) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException("Deposit amount must be positive");
		}
		Transaction transaction = new Transaction(amount, TransactionType.DEPOSIT, account);
		transactionRepository.save(transaction);
	}

	public void registerWithdraw(BigDecimal amount, Account account) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException("Withdrawal amount must be positive");
		}
		Transaction transaction = new Transaction(amount, TransactionType.WITHDRAW, account);
		transactionRepository.save(transaction);
	}

	public void registerTransfer(BigDecimal amount, Account source, Account target) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException("Transaction amount must be positive");
		}
		Transaction transaction = new Transaction(amount, TransactionType.TRANSFERENCE, source, target);
		transactionRepository.save(transaction);
	}

	public List<Transaction> findAll() {
		return transactionRepository.findAll();
	}
	
	/**
	 * Finds all transactions where account is either source or target.
	 */
	public List<Transaction> findByAccount(Account account) {
	    return transactionRepository.findAll().stream()
	            .filter(t -> account.equals(t.getSourceAccount()) || account.equals(t.getTargetAccount()))
	            .collect(Collectors.toList());
	}

	public Transaction findById(UUID id) {
		return transactionRepository.findById(id)
				.orElseThrow(() -> new DomainNotFoundException("Transaction not found for id: " + id));
	}

	public void deleteById(UUID id) {
		transactionRepository.deleteById(id);
	}

}
