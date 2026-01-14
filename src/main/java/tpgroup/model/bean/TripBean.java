package tpgroup.model.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import tpgroup.model.domain.Trip;

public class TripBean {
	private final String country;
	private final String mainCity;
	private final EventsGraphBean tripGraph;
	private final Set<ProposalBean> proposals;

	public TripBean(Trip trip) {
		this.country = trip.getCountry();
		this.mainCity = trip.getMainCity();
		this.tripGraph = new EventsGraphBean(trip.getTripGraph());
		this.proposals = trip.getProposals().stream().map(prop -> new ProposalBean(prop)).collect(Collectors.toSet());
	}

	public TripBean(String country, String city) {
		this.country = country;
		this.mainCity = city;
		this.tripGraph = null;
		this.proposals = null;
	}

	public String getCountry() {
		return country;
	}

	public String getMainCity() {
		return mainCity;
	}

	public EventsGraphBean getTripGraph() {
		return tripGraph;
	}

	public Set<ProposalBean> getProposals() {
		return proposals;
	}

	@Override
	public int hashCode() {
		return Objects.hash(country, mainCity, tripGraph, proposals);
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
		TripBean other = (TripBean) obj;
		return Objects.equals(country, other.country) && Objects.equals(mainCity, other.mainCity)
				&& Objects.equals(tripGraph, other.tripGraph) && Objects.equals(proposals, other.proposals);
	}

	@Override
	public String toString() {
		if (tripGraph == null) {
			return String.format("Trip to %s, %s (not initialized)", mainCity, country);
		}

		StringBuilder sb = new StringBuilder();

		sb.append("═══════════════════════════════════════════════════════════════\n");
		sb.append(String.format("  TRIP TO %s, %s%n", mainCity.toUpperCase(), country.toUpperCase()));
		sb.append("═══════════════════════════════════════════════════════════════\n\n");

		sb.append("📍 TRIP STRUCTURE:\n");
		sb.append("───────────────────────────────────────────────────────────────\n");

		Set<BranchBean> visited = new HashSet<>();
		buildGraphTree(tripGraph.getRoot(), sb, "", true, visited);

		Set<StagingBranchBean> stagingBranches = getStagingBranches();
		if (!stagingBranches.isEmpty()) {
			sb.append("\n🔧 STAGING AREA:\n");
			sb.append("───────────────────────────────────────────────────────────────\n");
			for (BranchBean branch : stagingBranches) {
				buildNodeWithEvents(branch, sb, "   ", true);
			}
		}

		sb.append("═══════════════════════════════════════════════════════════════\n");

		return sb.toString();
	}

	private void buildGraphTree(BranchBean node, StringBuilder sb, String prefix,
			boolean isLast, Set<BranchBean> visited) {
		if (visited.contains(node)) {
			sb.append(prefix).append(isLast ? "└── " : "├── ")
					.append("⚠️  [Cycle detected]\n");
			return;
		}

		visited.add(node);

		buildNodeWithEvents(node, sb, prefix, isLast);

		Set<BranchBean> children = tripGraph.getConnectionsMapping()
				.getOrDefault(node, Collections.emptySet());

		if (!children.isEmpty()) {
			String childPrefix = prefix + (isLast ? "    " : "│   ");
			List<BranchBean> childList = new ArrayList<>(children);

			for (int i = 0; i < childList.size(); i++) {
				boolean isLastChild = (i == childList.size() - 1);
				buildGraphTree(childList.get(i), sb, childPrefix, isLastChild, visited);
			}
		}
	}

	private void buildNodeWithEvents(BranchBean node, StringBuilder sb,
			String prefix, boolean isLast) {
		String connector = isLast ? "└── " : "├── ";

		sb.append(prefix).append(connector).append(node).append("\n");

		if (!node.getEvents().isEmpty()) {
			String eventPrefix = prefix + (isLast ? "    " : "│   ");
			List<EventBean> eventList = new ArrayList<>(node.getEvents());

			for (int i = 0; i < eventList.size(); i++) {
				boolean isLastEvent = (i == eventList.size() - 1);
				String eventConnector = isLastEvent ? "└─ " : "├─ ";
				EventBean event = eventList.get(i);

				sb.append(eventPrefix).append(eventConnector)
						.append(String.format("%d. ", i + 1))
						.append(event).append("\n");
			}
		}
	}

	private Set<StagingBranchBean> getStagingBranches() {
		return tripGraph.getStagingNodes();
	}
}
