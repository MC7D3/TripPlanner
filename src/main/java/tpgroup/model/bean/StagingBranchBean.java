package tpgroup.model.bean;

import java.util.NoSuchElementException;

import tpgroup.model.EventsNode;

public class StagingBranchBean extends BranchBean{

	public StagingBranchBean(EventsNode branch) {
		super(branch);
	}

	@Override
	public String toString() {
		try {
			return  super.toString() + " (Staging)";
		} catch (NoSuchElementException e) {
			return "[no events] (Staging)";
		}
	}
	
}
