package tpgroup.persistence.gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.Event;
import tpgroup.model.EventsGraph;
import tpgroup.model.EventsNode;
import tpgroup.model.exception.MalformedJSONException;

public class EventsGraphJSONTypeAdapter extends TypeAdapter<EventsGraph> {

	// Use other type adapters instead of manual serialization
	private static final EventTypeAdapter eventAdapter = new EventTypeAdapter();

	@Override
	public void write(JsonWriter out, EventsGraph value) throws IOException {
		out.beginObject();

		out.name("rootId").value(value.getRoot().getId().toString());

		writeNodesArray(out, value.getAllNodes());

		writeEdgesArray(out, value.getConnectionsMapping());

		writeStagingNodeIds(out, value.getStagingNodes());

		out.endObject();
	}

	private void writeNodesArray(JsonWriter out, List<EventsNode> nodes) throws IOException {
		out.name("nodes");
		out.beginArray();
		for (EventsNode node : nodes) {
			writeNode(out, node);
		}
		out.endArray();
	}

	private void writeNode(JsonWriter out, EventsNode node) throws IOException {
		out.beginObject();
		out.name("id").value(node.getId().toString());

		out.name("events");
		out.beginArray();
		for (Event event : node.allEvents()) {
			eventAdapter.write(out, event);
		}
		out.endArray();

		out.endObject();
	}

	private void writeEdgesArray(JsonWriter out, Map<EventsNode, Set<EventsNode>> connections) throws IOException {
		out.name("edges");
		out.beginArray();
		for (Map.Entry<EventsNode, Set<EventsNode>> entry : connections.entrySet()) {
			EventsNode parent = entry.getKey();
			for (EventsNode child : entry.getValue()) {
				writeEdge(out, parent, child);
			}
		}
		out.endArray();
	}

	private void writeEdge(JsonWriter out, EventsNode from, EventsNode to) throws IOException {
		out.beginObject();
		out.name("from").value(from.getId().toString());
		out.name("to").value(to.getId().toString());
		out.endObject();
	}

	private void writeStagingNodeIds(JsonWriter out, List<EventsNode> stagingNodes) throws IOException {
		out.name("stagingNodeIds");
		out.beginArray();
		for (EventsNode node : stagingNodes) {
			out.value(node.getId().toString());
		}
		out.endArray();
	}

	@Override
	public EventsGraph read(JsonReader in) throws IOException {

		GraphParseResult parseResult = parseJsonStructure(in);

		validateParseResult(parseResult);

		Map<String, EventsNode> allNodes = createEventsNodes(parseResult.nodeDataMap);

		NodeOrganization nodeOrg = organizeNodes(allNodes, parseResult.stagingNodeIds, parseResult.rootId);

		Map<EventsNode, Set<EventsNode>> connections = buildConnections(
				parseResult.edges, allNodes, nodeOrg.activeNodes);

		return new EventsGraph(nodeOrg.root, connections, nodeOrg.activeNodes, nodeOrg.stagingNodes);
	}

	private GraphParseResult parseJsonStructure(JsonReader in) throws IOException {
		GraphParseResult result = new GraphParseResult();

		in.beginObject();
		while (in.hasNext()) {
			String fieldName = in.nextName();
			parseJsonField(in, fieldName, result);
		}
		in.endObject();

		return result;
	}

	private void parseJsonField(JsonReader in, String fieldName, GraphParseResult result) throws IOException {
		switch (fieldName) {
			case "rootId":
				result.rootId = in.nextString();
				break;
			case "nodes":
				result.nodeDataMap = parseNodesArray(in);
				break;
			case "edges":
				result.edges = parseEdgesArray(in);
				break;
			case "stagingNodeIds":
				result.stagingNodeIds = parseStagingNodeIdsArray(in);
				break;
			default:
				in.skipValue();
		}
	}

	private Map<String, NodeData> parseNodesArray(JsonReader in) throws IOException {
		Map<String, NodeData> nodeDataMap = new HashMap<>();

		in.beginArray();
		while (in.hasNext()) {
			NodeData nodeData = parseNodeData(in);
			nodeDataMap.put(nodeData.id, nodeData);
		}
		in.endArray();

		return nodeDataMap;
	}

	private NodeData parseNodeData(JsonReader in) throws IOException {
		in.beginObject();
		String id = null;
		NavigableSet<Event> events = new TreeSet<>();

		while (in.hasNext()) {
			String fieldName = in.nextName();
			switch (fieldName) {
				case "id":
					id = in.nextString();
					break;
				case "events":
					events = parseEventsArray(in);
					break;
				default:
					in.skipValue();
			}
		}
		in.endObject();

		if (id == null) {
			throw new MalformedJSONException("Node missing ID");
		}

		return new NodeData(id, events);
	}

	private NavigableSet<Event> parseEventsArray(JsonReader in) throws IOException {
		NavigableSet<Event> events = new TreeSet<>();

		in.beginArray();
		while (in.hasNext()) {
			Event event = eventAdapter.read(in);
			if (event == null) {
				throw new MalformedJSONException("Event is missing required fields");
			}
			events.add(event);
		}
		in.endArray();

		return events;
	}

	private List<EdgeData> parseEdgesArray(JsonReader in) throws IOException {
		List<EdgeData> edges = new ArrayList<>();

		in.beginArray();
		while (in.hasNext()) {
			edges.add(parseEdgeData(in));
		}
		in.endArray();

		return edges;
	}

	private EdgeData parseEdgeData(JsonReader in) throws IOException {
		in.beginObject();
		String from = null;
		String to = null;

		while (in.hasNext()) {
			String fieldName = in.nextName();
			switch (fieldName) {
				case "from":
					from = in.nextString();
					break;
				case "to":
					to = in.nextString();
					break;
				default:
					in.skipValue();
			}
		}
		in.endObject();

		return new EdgeData(from, to);
	}

	private Set<String> parseStagingNodeIdsArray(JsonReader in) throws IOException {
		Set<String> stagingNodeIds = new HashSet<>();

		in.beginArray();
		while (in.hasNext()) {
			stagingNodeIds.add(in.nextString());
		}
		in.endArray();

		return stagingNodeIds;
	}

	private void validateParseResult(GraphParseResult parseResult) {
		if (parseResult.rootId == null) {
			throw new MalformedJSONException("Root ID is required in JSON");
		}
		if (parseResult.nodeDataMap == null || !parseResult.nodeDataMap.containsKey(parseResult.rootId)) {
			throw new MalformedJSONException("Root node not found in nodes list");
		}
	}

	private Map<String, EventsNode> createEventsNodes(Map<String, NodeData> nodeDataMap) {
		Map<String, EventsNode> createdNodes = new HashMap<>();

		for (Map.Entry<String, NodeData> entry : nodeDataMap.entrySet()) {
			String nodeId = entry.getKey();
			NodeData nodeData = entry.getValue();

			EventsNode node = new EventsNode(UUID.fromString(nodeId), nodeData.events);
			createdNodes.put(nodeId, node);
		}

		return createdNodes;
	}

	private NodeOrganization organizeNodes(Map<String, EventsNode> allNodes,
			Set<String> stagingNodeIds, String rootId) {
		EventsNode root = allNodes.get(rootId);
		Set<EventsNode> activeNodes = new HashSet<>();
		Set<EventsNode> stagingNodes = new HashSet<>();

		for (Map.Entry<String, EventsNode> entry : allNodes.entrySet()) {
			String nodeId = entry.getKey();
			EventsNode node = entry.getValue();

			if (stagingNodeIds.contains(nodeId)) {
				stagingNodes.add(node);
			} else {
				activeNodes.add(node);
			}
		}

		if (stagingNodes.contains(root)) {
			stagingNodes.remove(root);
			activeNodes.add(root);
		} else if (!activeNodes.contains(root)) {
			activeNodes.add(root);
		}

		return new NodeOrganization(root, activeNodes, stagingNodes);
	}

	private Map<EventsNode, Set<EventsNode>> buildConnections(List<EdgeData> edges,
			Map<String, EventsNode> allNodes,
			Set<EventsNode> activeNodes) {
		Map<EventsNode, Set<EventsNode>> connections = new HashMap<>();
		for (EventsNode node : activeNodes) {
			connections.put(node, new HashSet<>());
		}

		for (EdgeData edge : edges) {
			addConnection(edge, allNodes, activeNodes, connections);
		}

		return connections;
	}

	private void addConnection(EdgeData edge, Map<String, EventsNode> allNodes,
			Set<EventsNode> activeNodes, Map<EventsNode, Set<EventsNode>> connections) {
		EventsNode fromNode = allNodes.get(edge.from);
		EventsNode toNode = allNodes.get(edge.to);

		if (fromNode == null || toNode == null) {
			throw new MalformedJSONException("Edge references non-existent node: " +
					edge.from + " -> " + edge.to);
		}

		if (activeNodes.contains(fromNode) && activeNodes.contains(toNode)) {
			connections.get(fromNode).add(toNode);
		}
	}

	private static class GraphParseResult {
		String rootId;
		Map<String, NodeData> nodeDataMap;
		List<EdgeData> edges;
		Set<String> stagingNodeIds;

		GraphParseResult() {
			nodeDataMap = new HashMap<>();
			edges = new ArrayList<>();
			stagingNodeIds = new HashSet<>();
		}
	}

	private static class NodeData {
		final String id;
		final NavigableSet<Event> events;

		NodeData(String id, NavigableSet<Event> events) {
			this.id = id;
			this.events = events;
		}
	}

	private static class EdgeData {
		final String from;
		final String to;

		EdgeData(String from, String to) {
			this.from = from;
			this.to = to;
		}
	}

	private static class NodeOrganization {
		final EventsNode root;
		final Set<EventsNode> activeNodes;
		final Set<EventsNode> stagingNodes;

		NodeOrganization(EventsNode root, Set<EventsNode> activeNodes, Set<EventsNode> stagingNodes) {
			this.root = root;
			this.activeNodes = activeNodes;
			this.stagingNodes = stagingNodes;
		}
	}
}
