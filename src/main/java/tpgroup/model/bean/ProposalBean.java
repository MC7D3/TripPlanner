package tpgroup.model.bean;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.ProposalType;

public class ProposalBean {
	private final UserBean creator;
	private final LocalDateTime creationTime;
	private final BranchBean node;
	private final EventBean event;
	private final Optional<EventBean> updateEvent;
	private final ProposalType proposalType;
	private final int likes;

	public ProposalBean(Proposal proposal) {
		this.creator = new UserBean(proposal.getCreator());
		this.creationTime = proposal.getCreationTime();
		this.node = new BranchBean(proposal.getNodeName());
		this.event = new EventBean(proposal.getEvent());
		this.updateEvent = Optional.ofNullable(
				proposal.getUpdateEvent().isPresent() ? new EventBean(proposal.getUpdateEvent().get()) : null);
		this.proposalType = proposal.getProposalType();
		this.likes = proposal.getLikes();
	}

	public UserBean getCreator() {
		return creator;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public BranchBean getNode() {
		return node;
	}

	public EventBean getEvent() {
		return event;
	}

	public Optional<EventBean> getUpdateEvent() {
		return updateEvent;
	}

	public ProposalType getProposalType() {
		return proposalType;
	}

	public int getLikes() {
		return likes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(creator, creationTime, event);
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
		ProposalBean other = (ProposalBean) obj;
		return Objects.equals(creator, other.creator) && Objects.equals(creationTime, other.creationTime)
				&& Objects.equals(event, other.event);
	}

	@Override
	public String toString() {
		return proposalType + " (" + getLikes() + " likes): " + event + (proposalType.equals(ProposalType.UPDATE) ? "becomes: " + updateEvent : "")
				+ "\n(createdBy: " + creator.getEmail() + ")\ninserted in: " + node;
	}

}
