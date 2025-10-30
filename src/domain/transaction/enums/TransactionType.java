package domain.transaction.enums;

public enum TransactionType {
	
	WITHDRAW(1),
	DEPOSIT(2),
	TRANSFERENCE(3);
	
	private int code;

	private TransactionType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static TransactionType valueOf(int code) {
		for (TransactionType value: TransactionType.values()) {
			if(value.getCode() == code) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid TransactionType code");
	}
	
}
