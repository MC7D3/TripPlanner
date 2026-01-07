package tpgroup.model.bean;

import java.util.Objects;
import java.util.regex.Pattern;

import tpgroup.model.domain.User;
import tpgroup.model.exception.InvalidBeanParamException;

public class UserBean {
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	private static final String PWD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$";

	private String email;
	private String password;
	private String confPassword;

	public UserBean(){}

	public UserBean(String email, String password) throws InvalidBeanParamException{
		if(!Pattern.matches(EMAIL_REGEX, email)){
			throw new InvalidBeanParamException("email", "email format:\nemailtext@provider.it/org/eu...");
		}
		if(!Pattern.matches(PWD_REGEX, password)){
			throw new InvalidBeanParamException("password", "password requirements:\n- one upper and lower case letter;\n- at least one special character\n- at least one number number");
		}
		this.email = email;
		this.password = password;
		this.confPassword = null;
	}

	public UserBean(String email, String password, String confPassword) throws InvalidBeanParamException{
		this(email, password);
		this.confPassword = confPassword;
	}

	public UserBean(User user){
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.confPassword = null;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getConfPassword() {
		return confPassword;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserBean other = (UserBean) obj;
		return Objects.equals(email, other.email) && Objects.equals(password, other.password);
	}

	@Override
	public String toString() {
		return "UserBean{email=" + email + ", password=" + password + ", confPassword=" + confPassword + "}";
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}

}
