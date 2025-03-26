package tpgroup.model.exception;

public class SQLConnInterruptedException extends RuntimeException {
private static final String DEF_MSG = "The Connection to the SQL database failed";

public SQLConnInterruptedException() {
	super(DEF_MSG);
}

public SQLConnInterruptedException(Throwable cause) {
	super(DEF_MSG, cause);
}

public SQLConnInterruptedException(String msg) {
	super(msg);
}
	
}
