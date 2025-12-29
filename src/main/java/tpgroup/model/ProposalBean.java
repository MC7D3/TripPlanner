package tpgroup.model;

import java.util.Objects;

import tpgroup.model.domain.Proposal;

public class ProposalBean {
	private final Proposal proposal;

	public ProposalBean(Proposal proposal) {
		super();
		this.proposal = proposal;
	}

	public Proposal getProposal() {
		return proposal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(proposal);
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
		return Objects.equals(proposal, other.proposal);
	}

}
