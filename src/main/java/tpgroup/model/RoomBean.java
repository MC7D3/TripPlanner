package tpgroup.model;

import java.util.regex.Pattern;

import tpgroup.model.exception.InvalidBeanParamException;

public class RoomBean {
	private static final String NAME_REGEX = "^[a-zA-Z0-9_-]+$";
	private String name;

	public RoomBean(String name) throws InvalidBeanParamException{	 
		if(Pattern.matches(NAME_REGEX, name)){
			throw new InvalidBeanParamException("name");
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
