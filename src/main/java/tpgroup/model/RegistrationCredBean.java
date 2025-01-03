package tpgroup.model;

import org.springframework.security.crypto.bcrypt.BCrypt;

import tpgroup.model.exception.InvalidBeanParamException;

public class RegistrationCredBean extends LoginCredBean{

	public RegistrationCredBean(String email, String password, String confPassword) throws InvalidBeanParamException {

		super(email, password);

		if(!BCrypt.checkpw(confPassword, password)){
			throw new InvalidBeanParamException("confPassword", "the passwords field dont match");
		}
	}
	
}
