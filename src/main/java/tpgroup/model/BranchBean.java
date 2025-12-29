package tpgroup.model;

import java.util.Objects;

public class BranchBean {
	private final EventsNode node;

	public BranchBean(EventsNode node){
		this.node = node;
	}

	public EventsNode getEventsNode(){
		return node;
	}

	@Override
	public int hashCode() {
		return Objects.hash(node);
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
		BranchBean other = (BranchBean) obj;
		return Objects.equals(node, other.node);
	}

}
