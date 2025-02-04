package tpgroup.model.exception;

public class NodeConflictException extends Exception {
	private static final String MSG = "a node with this name already exist";

	public NodeConflictException() {
		super(MSG);
	}

	public NodeConflictException(Throwable cause) {
		super(MSG, cause);
	}

}
