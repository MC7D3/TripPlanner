package tpgroup.model;

import java.util.regex.Pattern;

import tpgroup.model.exception.InvalidBeanParamException;

public class EmailBean {
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	private final String email;
	
	public EmailBean(String email) throws InvalidBeanParamException {
		if(!Pattern.matches(EMAIL_REGEX, email)){
			throw new InvalidBeanParamException("email", "email format:\nemailtext@provider.it/org/eu...");
		}

		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailBean other = (EmailBean) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

}
