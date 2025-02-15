package tpgroup.model;

import tpgroup.controller.TripController;
import tpgroup.model.exception.InvalidBeanParamException;

public class BranchNameBean {
	private final String name;

	public BranchNameBean(String name) throws InvalidBeanParamException{
		super();
		if(TripController.getBranches().stream().anyMatch(branch -> branch.getName().equals(name))){
			throw new InvalidBeanParamException("name", "a branch with this name already exist");
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		BranchNameBean other = (BranchNameBean) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
