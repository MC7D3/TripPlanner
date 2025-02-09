package tpgroup.model.exception;

public class EnumNotFoundException extends Exception{
	private static final String DEF_MSG = "no enum constant was found binded to the provided name";

	public EnumNotFoundException(String msg) {
		super(msg);
	}
	
	public EnumNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public EnumNotFoundException(Throwable cause) {
		super(DEF_MSG, cause);
	}

	public EnumNotFoundException() {
		super(DEF_MSG);
	}
}
