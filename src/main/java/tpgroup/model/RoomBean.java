package tpgroup.model;

import java.util.regex.Pattern;

import tpgroup.model.exception.InvalidBeanParamException;

public class RoomBean {
	private static final String NAME_REGEX = "^[a-zA-Z0-9_-]+$";
	private String name;
	private String destination;

	public RoomBean(String name, String destination) throws InvalidBeanParamException{	 
		if(!Pattern.matches(NAME_REGEX, name)){
			throw new InvalidBeanParamException("name");
		}
		//TODO validate destination string so that is an actual destination
		this.name = name;
		this.destination = destination;
	}

	public String getName() {
		return name;
	}

	public String getDestination() {
		return destination;
	}
	
}
