package tpgroup.model.exception;

public class InvalidViewTypeException extends Exception {
	private static final String MSG = "the view type provided is invalid";

	public InvalidViewTypeException() {
		super(MSG);
	}

	public InvalidViewTypeException(Throwable cause) {
		super(MSG, cause);
	}

}
