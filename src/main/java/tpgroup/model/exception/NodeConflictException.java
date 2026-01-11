package tpgroup.model.exception;

public class NodeConflictException extends Exception {
	private static final String MSG = "invalid interaction with the trip Graph";

	public NodeConflictException() {
		super(MSG);
	}

	public NodeConflictException(String msg){
		super(msg);
	}

	public NodeConflictException(Throwable cause) {
		super(MSG, cause);
	}

}
