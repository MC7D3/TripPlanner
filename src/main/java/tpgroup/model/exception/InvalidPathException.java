package tpgroup.model.exception;

public class InvalidPathException extends RuntimeException {
	private static final String DEF_MSG = "invalid path provided";
	public InvalidPathException(String msg) {
		super(msg);
	}

	public InvalidPathException(String msg, Throwable cause) {
		super(cause);
	}

	public InvalidPathException(){
		this(DEF_MSG);
	} 
	
}
