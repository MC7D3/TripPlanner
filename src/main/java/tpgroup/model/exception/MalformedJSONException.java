package tpgroup.model.exception;

public class MalformedJSONException extends RuntimeException {
	private static final String DEF_MSG = "the JSON structure is not compliant to the expected structure";

	public MalformedJSONException(String msg) {
		super(msg);
	}	

	public MalformedJSONException(Throwable cause) {
		super(DEF_MSG, cause);
	}

	public MalformedJSONException() {
		super(DEF_MSG);
	}

}
