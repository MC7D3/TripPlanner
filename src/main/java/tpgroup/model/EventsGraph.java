package tpgroup.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import tpgroup.model.exception.NodeConflictException;
import tpgroup.model.exception.NodeConnectionException;

public class EventsGraph {
	private final EventsNode root;
	private final Map<EventsNode, Set<EventsNode>> connectionsMapping;
	private final TreeSet<EventsNode> nodes;

	public EventsGraph() {
		super();
		this.root = new EventsNode(this, "root");
		this.connectionsMapping = new HashMap<>();
		this.nodes = new TreeSet<>();
		connectionsMapping.put(this.root, new HashSet<>());
	}

	public EventsNode createEmptyNode(EventsNode parent, String newNodeName) throws NodeConflictException {
		if (parent == null)
			parent = root;
		EventsNode newNode = new EventsNode(this, newNodeName);
		if (!nodes.add(newNode))
			throw new NodeConflictException();
		connectionsMapping.put(newNode, new HashSet<>());
		connect(parent, newNode);
		return newNode;
	}

	public List<EventsNode> getAllNodes() {
		return nodes.stream().toList();
	}

	public EventsNode getNode(String nodeName) {
		return nodes.stream().filter(node -> node.getName().equals(nodeName)).findFirst().get();
	}

	public void connect(EventsNode parent, EventsNode child) throws NodeConnectionException {
		if (parent == null)
			parent = root;
		if (!nodes.contains(parent) || !nodes.contains(child)) {
			throw new NodeConnectionException("those nodes arent of the same graph");
		}
		if (!canConnect(parent, child)) {
			throw new NodeConnectionException("chronological conflict arised");
		}
		if (isCycle(child, parent)) {
			throw new NodeConnectionException("cannot create a cycle in the graph");
		}
		connectionsMapping.get(parent).add(child);
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
		return source.getEventsEnd().isBefore(target.getEventsStart());
	}

	public boolean areConnected(EventsNode source, EventsNode target) {
		return connectionsMapping.get(source).contains(target);
	}

	public int connCount(EventsNode node) {
		return connectionsMapping.get(node).size();
	}

	public boolean checkNodesConflicts(EventsNode toCheck, LocalDateTime newStart, LocalDateTime newEnd) {
		for (EventsNode child : connectionsMapping.get(toCheck)) {
			if (child.getEventsStart().isBefore(newEnd)) {
				return false;
			}
		}
		for (EventsNode father : findFathers(toCheck)) {
			if (father.getEventsEnd().isAfter(newStart)) {
				return false;
			}
		}
		return true;
	}

	private List<EventsNode> findFathers(EventsNode node) {
		List<EventsNode> ret = new ArrayList<>();
		for (EventsNode fatherCandidate : nodes) {
			if (fatherCandidate.equals(node)) {
				break;
			}
			if (connectionsMapping.get(fatherCandidate).contains(node)) {
				ret.add(fatherCandidate);
			}

		}
		return ret;
	}

	public void removeNode(EventsNode toRemove) {
		List<EventsNode> fathers = findFathers(toRemove);
		for (EventsNode father : fathers) {
			connectionsMapping.get(father).addAll(connectionsMapping.get(toRemove));
		}
	}

	public void notifySplit(EventsNode eventsNode, EventsNode newNode) {
		connectionsMapping.put(newNode, connectionsMapping.get(eventsNode));
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
