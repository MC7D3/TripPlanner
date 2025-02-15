package tpgroup.model.exception;

public class BranchConnectionException extends Exception {
	private static final String DEF_MSG = "failed to connect the branches";

	public BranchConnectionException() {
		super(DEF_MSG);
	}

	public BranchConnectionException(Throwable cause) {
		super(DEF_MSG, cause);
	}

	public BranchConnectionException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
