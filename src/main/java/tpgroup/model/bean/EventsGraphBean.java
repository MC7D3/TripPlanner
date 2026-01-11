package tpgroup.model.bean;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import tpgroup.model.EventsGraph;

public class EventsGraphBean {
	private final BranchBean root;
	private final Map<BranchBean, Set<BranchBean>> connectionsMapping;
	private final Set<BranchBean> nodes;


	public EventsGraphBean(EventsGraph graph) {
		this.root = new BranchBean(graph.getRoot());
		this.connectionsMapping = graph.getConnectionsMapping().entrySet().stream().collect(Collectors.toMap(b -> new BranchBean(b.getKey()), b -> new HashSet<>(b.getValue()).stream().map(event -> new BranchBean(event)).collect(Collectors.toSet())));
		this.nodes = graph.getGraphNodes().stream().map(node -> new BranchBean(node)).collect(Collectors.toCollection(() -> new HashSet<>()));
	}


	public BranchBean getRoot() {
		return root;
	}


	public Map<BranchBean, Set<BranchBean>> getConnectionsMapping() {
		return connectionsMapping;
	}


	public Set<BranchBean> getNodes() {
		return nodes;
	}


	@Override
	public int hashCode() {
		return Objects.hash(root, connectionsMapping, nodes);
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
		EventsGraphBean other = (EventsGraphBean) obj;
		return Objects.equals(root, other.root) && Objects.equals(connectionsMapping, other.connectionsMapping)
				&& Objects.equals(nodes, other.nodes);
	}

	@Override
	public String toString() {
		return "EventsGraphBean{root=" + root + ", connectionsMapping=" + connectionsMapping + ", nodes=" + nodes + "}";
	}

}
