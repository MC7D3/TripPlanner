package tpgroup.model.exception;

public class InvalidPersistenceTypeException extends Exception {
	private static final String MSG = "the persistence type provided is invalid";

	public InvalidPersistenceTypeException() {
		super(MSG);
	}

	public InvalidPersistenceTypeException(Throwable cause) {
		super(MSG, cause);
	}

}
