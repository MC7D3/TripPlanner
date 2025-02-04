package tpgroup.model.exception;

public class NodeConnectionException extends RuntimeException{
	private static final String DEF_MSG = "failed node connection";

	public NodeConnectionException(String msg) {
		super(msg);
	}

	public NodeConnectionException() {
		super(DEF_MSG);
	}

	public NodeConnectionException(String msg, Throwable cause){
		super(msg, cause);
	}

	public NodeConnectionException(Throwable cause){
		super(DEF_MSG, cause);
	}
}
