package exceptions;

public class InsufficientBalanceException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public InsufficientBalanceException(String message) {
        super(message);
    }
}
