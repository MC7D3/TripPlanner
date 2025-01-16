package tpgroup.model;

import java.util.regex.Pattern;

import tpgroup.model.exception.InvalidBeanParamException;

public class EmailBean {
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	private final String email;
	
	public EmailBean(String email) throws InvalidBeanParamException {
		if(!Pattern.matches(EMAIL_REGEX, email)){
			throw new InvalidBeanParamException("email");
		}

		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
