package tpgroup.model.exception;

public class InvalidPersistenceTypeException extends Exception {
	private static final String msg = "The persistence type provided is invalid";

	public InvalidPersistenceTypeException() {
		super(msg);
	}

	public InvalidPersistenceTypeException(Throwable cause) {
		super(msg, cause);
	}

}	
