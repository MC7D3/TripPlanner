package tpgroup.model.exception;

public class InvalidBeanParamException extends Exception{
	private static final String defMsg = "Invalid field format";

	public InvalidBeanParamException(String paramName, String msg) {
		super(String.format("(%s) %s", paramName, msg));
	}

	public InvalidBeanParamException(String paramName) {
		this(paramName, defMsg);
	}
}

