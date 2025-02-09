package tpgroup.controller;

import java.util.List;

import tpgroup.model.Event;
import tpgroup.model.EventsNode;
import tpgroup.model.IntervalBean;
import tpgroup.model.Session;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.ProposalType;

public class TripController {

	public static List<Proposal> getProposals() {
		return Session.getInstance().getEnteredRoom().getTrip().getProposals();
	}

	public static List<EventsNode> getSequences() {
		return Session.getInstance().getEnteredRoom().getTrip().getSequenceNodes();
	}

	public static String getDestination() {
		return Session.getInstance().getEnteredRoom().getTrip().getDestination();
	}

	public static boolean addEvent(EventsNode where, PointOfInterest poi, IntervalBean interval) {
		return where.addEvent(new Event(poi, interval.getStartTime(), interval.getEndTime()));
	}

	public static boolean createAddProposal(EventsNode where, PointOfInterest poi, IntervalBean interval) {
		return Session.getInstance().getEnteredRoom().getTrip().addProposal(new Proposal(ProposalType.ADD, where,
				new Event(poi, interval.getStartTime(), interval.getEndTime()), Session.getInstance().getLogged()));
	}

	public static boolean createRemoveProposal(EventsNode where, Event toRemove) {
		if (!where.getEvents().contains(toRemove)) {
			return false;
		}
		return Session.getInstance().getEnteredRoom().getTrip()
				.addProposal(new Proposal(ProposalType.REMOVE, where, toRemove, Session.getInstance().getLogged()));
	}

	public static boolean createUpdateProposal(EventsNode where, Event toRemove) {
		if (!where.getEvents().contains(toRemove)) {
			return false;
		}
		return Session.getInstance().getEnteredRoom().getTrip()
				.addProposal(new Proposal(ProposalType.UPDATE, where, toRemove, Session.getInstance().getLogged()));
	}

	public static List<Event> getEvents(EventsNode from) {
		return from.getEvents().stream().toList();
	}

	public static List<Proposal> getLoggedProposals() {
		return Session.getInstance().getEnteredRoom().getTrip().getProposals().stream()
				.filter(proposal -> proposal.getCreator().equals(Session.getInstance().getLogged())).toList();
	}

    public static boolean undoProposal(Proposal chosen) {
		return Session.getInstance().getEnteredRoom().getTrip().getProposals().remove(chosen);
    }


}
