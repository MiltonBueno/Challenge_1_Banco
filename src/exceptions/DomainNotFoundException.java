package exceptions;

public class DomainNotFoundException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DomainNotFoundException(String message) {
        super(message);
    }
}
