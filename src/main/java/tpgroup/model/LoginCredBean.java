package tpgroup.model;

import java.util.regex.Pattern;

import tpgroup.model.exception.InvalidBeanParamException;

public class LoginCredBean {
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	private static final String PWD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$";

	protected String email;
	protected String password;

	public LoginCredBean(String email, String password) throws InvalidBeanParamException {
		if(!Pattern.matches(EMAIL_REGEX, email)){
			throw new InvalidBeanParamException("email");
		}

		if(!Pattern.matches(PWD_REGEX, password)){
			throw new InvalidBeanParamException("password", "the password must be at least 8 characters long, have an uppercase and lowercase letter and a special character");
		}

		this.email = email;
		this.password = password;

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return String.format("LoginCredBean{%s, %s}", this.email, this.password);
	}
}

