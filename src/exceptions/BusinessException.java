package exceptions;

public class BusinessException extends DomainException {
	private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
        super(message);
    }
}
