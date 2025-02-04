package tpgroup.model.exception;

public class RecordNotFoundException extends Exception {
	private static final String MSG = "no matching record found for the provided info";

	public RecordNotFoundException(Throwable cause) {
		super(MSG, cause);
	}

	public RecordNotFoundException() {
		super(MSG);
	}

}
