package tpgroup.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import tpgroup.model.exception.NodeConflictException;
import tpgroup.model.exception.NodeConnectionException;

public class EventsGraph {
	private final EventsNode root;
	private final Map<EventsNode, Set<EventsNode>> connectionsMapping;
	private final Set<EventsNode> nodes;
	private final Set<EventsNode> stagingArea;

	public EventsGraph() {
		this.root = new EventsNode(this);
		this.connectionsMapping = new HashMap<>();
		this.nodes = new HashSet<>();
		this.stagingArea = new HashSet<>();
		connectionsMapping.put(this.root, new HashSet<>());
		nodes.add(root);
	}

	public List<EventsNode> getConnectedNodes(EventsNode of) {
		return connectionsMapping.get(of).stream().toList();
	}

	public EventsNode createEmptyNode() throws NodeConflictException {
		EventsNode newNode = new EventsNode(this);
		if (!stagingArea.add(newNode))
			throw new NodeConflictException();
		return newNode;
	}

	public List<EventsNode> getGraphNodes() {
		return nodes.stream().toList();
	}

	public List<EventsNode> getStagingNodes() {
		return stagingArea.stream().toList();
	}

	public List<EventsNode> getAllNodes() {
		List<EventsNode> graphNodes = nodes.stream().collect(Collectors.toList());
		graphNodes.addAll(getStagingNodes());
		return graphNodes;

	}

	public void connect(EventsNode parent, EventsNode child) throws NodeConnectionException {
		boolean stageChild = false;
		if (parent == null)
			parent = root;
		if (!nodes.contains(parent)) {
			throw new NodeConnectionException("invalid parent, it might be in the staging area");
		}
		if (!nodes.contains(child)) {
			if (!stagingArea.contains(child)) {
				throw new NodeConnectionException("invalid child node");
			}else{
				stageChild = true;
			}
		}
		if (!canConnect(parent, child)) {
			throw new NodeConnectionException("chronological conflict arised");
		}

		if(stageChild){
			nodes.add(child);
			connectionsMapping.put(child, new HashSet<>());
			stagingArea.remove(child);

		}else if (isCycle(child, parent)) {
			throw new NodeConnectionException("cannot create a cycle in the graph");
		}

		connectionsMapping.get(parent).add(child);
	}

	public void disconnect(EventsNode parent, EventsNode child) throws NodeConnectionException {
		if (parent == null)
			parent = root;
		if (!nodes.contains(parent) || !nodes.contains(child)) {
			throw new NodeConnectionException("those nodes arent of the same graph");
		}
		if (!getConnectedNodes(parent).contains(child)) {
			throw new NodeConnectionException("the nodes arent connected");
		}
		if (this.findFathers(child).size() <= 1) {
			throw new NodeConnectionException(
					"cannot disconnect the nodes, the child would remain orphan, consider deletion instead");
		}
		connectionsMapping.get(parent).remove(child);
		if(connectionsMapping.get(child).isEmpty()){
			nodes.remove(child);
			stagingArea.add(child);
		}

	}

	private boolean isCycle(EventsNode start, EventsNode target) {
		Set<EventsNode> visited = new HashSet<>();
		return dfs(start, target, visited);
	}

	private boolean dfs(EventsNode current, EventsNode target, Set<EventsNode> visited) {
		if (current.equals(target)) {
			return true;
		}
		visited.add(current);
		for (EventsNode child : connectionsMapping.get(current)) {
			if (!visited.contains(child) && dfs(child, target, visited)) {
				return true;
			}
		}
		return false;
	}

	private boolean canConnect(EventsNode source, EventsNode target) {
		return !source.getEvents().isEmpty() && !target.getEvents().isEmpty() && source.getEventsEnd().isBefore(target.getEventsStart());
	}

	public boolean areConnected(EventsNode source, EventsNode target) {
		return connectionsMapping.get(source).contains(target);
	}

	public int connCount(EventsNode node) {
		return connectionsMapping.get(node).size();
	}

	public EventsNode getRoot() {
		return root;
	}

	public Map<EventsNode, Set<EventsNode>> getConnectionsMapping() {
		return connectionsMapping;
	}

	public boolean checkFatherNodesConflicts(EventsNode toCheck, LocalDateTime newStart) {
		for (EventsNode father : findFathers(toCheck)) {
			if (father.getEventsEnd().isAfter(newStart)) {
				return false;
			}
		}
		return true;
	}

	public boolean checkChildNodesConflicts(EventsNode toCheck, LocalDateTime newEnd) {
		for (EventsNode child : connectionsMapping.get(toCheck)) {
			if (child.getEventsStart().isBefore(newEnd)) {
				return false;
			}
		}
		return true;
	}

	private List<EventsNode> findFathers(EventsNode node) {
		List<EventsNode> ret = new ArrayList<>();
		for (EventsNode fatherCandidate : nodes) {
			if (connectionsMapping.get(fatherCandidate).contains(node)) {
				ret.add(fatherCandidate);
			}

		}
		return ret;
	}

	public void removeNode(EventsNode toRemove) throws NodeConflictException {
		if(toRemove.equals(root)){
			root.resetEvents();
			return;
		}
		List<EventsNode> fathers = findFathers(toRemove);
		if (fathers.isEmpty())
			throw new NodeConflictException();
		for (EventsNode father : fathers) {
			Set<EventsNode> connections = connectionsMapping.get(father);
			connections.remove(toRemove);
			connections.addAll(connectionsMapping.get(toRemove));
		}
		nodes.remove(toRemove);
		connectionsMapping.remove(toRemove);
	}

	public void notifySplit(EventsNode eventsNode, EventsNode newNode) {
		connectionsMapping.put(newNode, new HashSet<>(connectionsMapping.get(eventsNode)));
		connectionsMapping.put(eventsNode, new HashSet<>());
		nodes.add(newNode);
		connect(eventsNode, newNode);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		result = prime * result + ((connectionsMapping == null) ? 0 : connectionsMapping.hashCode());
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
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
		EventsGraph other = (EventsGraph) obj;
		if (root == null) {
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
			return false;
		if (connectionsMapping == null) {
			if (other.connectionsMapping != null)
				return false;
		} else if (!connectionsMapping.equals(other.connectionsMapping))
			return false;
		if (nodes == null) {
			if (other.nodes != null)
				return false;
		} else if (!nodes.equals(other.nodes))
			return false;
		return true;
	}

}
