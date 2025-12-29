package tpgroup.model;

import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCrypt;

import tpgroup.model.exception.InvalidBeanParamException;

public class PwdBean {
	private static final String PWD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$";
	protected final String password;

	public PwdBean(String password) throws InvalidBeanParamException {
		if(!Pattern.matches(PWD_REGEX, password)){
			throw new InvalidBeanParamException("password", "password requirements:\n- one upper and lower case letter;\n- at least one special character\n- at least one number number");
		}

		this.password = password;
	}

	public PwdBean(String password, String confPassword) throws InvalidBeanParamException {
		if(!Pattern.matches(PWD_REGEX, password)){
			throw new InvalidBeanParamException("password", "password requirements:\n- one upper and lower case letter;\n- at least one special character\n- at least one number number");
		}
		
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
		if(!BCrypt.checkpw(confPassword, this.password)){
			throw new InvalidBeanParamException("confPassword", "the passwords do not match");
		}
	}

	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
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
		PwdBean other = (PwdBean) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

}
