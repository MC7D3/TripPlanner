package tpgroup.model.exception;

public class RecordNotFoundException extends Exception{
	private static final String msg = "No matching record found for the provided info";
	public RecordNotFoundException(Throwable cause) {
		super(msg, cause);
	}

	public RecordNotFoundException() {
		super(msg);
	}

}

