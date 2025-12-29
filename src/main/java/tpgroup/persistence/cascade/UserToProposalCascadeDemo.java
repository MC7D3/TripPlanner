package tpgroup.persistence.cascade;

import java.util.List;

import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;
import tpgroup.persistence.Cascade;
import tpgroup.persistence.DAO;

public class UserToProposalCascadeDemo extends Cascade<User, Room> {

	public UserToProposalCascadeDemo(DAO<Room> to) {
		super(to);
	}

	@Override
	public boolean propagateAdd(User toAdd) {
		// not needed
		return true;
	}

	@Override
	public boolean propagateUpdate(User toUpdate) {
		List<Proposal> proposalToUpdate = getTo().getFiltered(room -> room.getMembers().contains(toUpdate)).stream()
				.flatMap(room -> room.getTrip().getProposals().stream())
				.filter(proposal -> proposal.getCreator().equals(toUpdate)).toList();
		for (Proposal prop : proposalToUpdate) {
			prop.setCreator(toUpdate);
		}
		return true;
	}

	@Override
	public boolean propagateDelete(User toDelete) {
		for(Room room : getTo().getFiltered(room -> room.getMembers().contains(toDelete))){
			room.getTrip().getProposals().removeIf(prop -> prop.getCreator().equals(toDelete));
		}
		return true;
	}

	@Override
	public List<User> propagateGetAll(List<User> toGet) {
		return toGet; //not needed
	}

	@Override
	public User propagateGet(User toGet) {
		return toGet; //not needed
	}

}
