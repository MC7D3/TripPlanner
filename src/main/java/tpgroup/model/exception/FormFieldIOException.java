package tpgroup.model.exception;

public class FormFieldIOException extends Exception {
	private static final String DEF_MSG = "unable to gather input from form field";

	public FormFieldIOException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public FormFieldIOException(String msg) {
		super(msg);
	}

	public FormFieldIOException(Throwable cause) {
		super(DEF_MSG, cause);
	}

	public FormFieldIOException() {
		super(DEF_MSG);
	}
}
