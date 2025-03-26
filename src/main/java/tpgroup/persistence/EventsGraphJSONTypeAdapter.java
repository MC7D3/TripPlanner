package tpgroup.persistence;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.Event;
import tpgroup.model.EventsGraph;
import tpgroup.model.EventsNode;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.exception.MalformedJSONException;
import tpgroup.model.exception.NodeConnectionException;

public class EventsGraphJSONTypeAdapter extends TypeAdapter<EventsGraph> {

	@Override
	public void write(JsonWriter out, EventsGraph value) throws IOException {
		out.beginObject();
		out.name("nodes");
		out.beginArray();
		for (EventsNode node : value.getAllNodes()) {
			serializeNode(out, node);
		}
		out.endArray();
		out.name("edges");
		out.beginArray();
		for (EventsNode parent : value.getAllNodes()) {
			for (EventsNode child : value.getConnectedNodes(parent)) {
				serializeEdge(out, parent, child);
			}
		}
		out.endArray();
		out.endObject();
	}

	private void serializeNode(JsonWriter out, EventsNode node) throws IOException {
		out.beginObject();
		out.name("name").value(node.getName());
		out.name("events");
		out.beginArray();
		for (Event event : node.getEvents()) {
			out.beginObject();
			out.name("poi");
			out.beginObject();
			out.name("name").value(event.getInfo().getName());
			out.name("location").value(event.getInfo().getLocation());
			out.endObject();
			out.name("start").value(event.getStart().toString());
			out.name("end").value(event.getEnd().toString());
			out.endObject();
		}
		out.endArray();
		out.endObject();
	}

	private void serializeEdge(JsonWriter out, EventsNode from, EventsNode to) throws IOException {
		out.beginObject();
		out.name("from").value(from.getName());
		out.name("to").value(to.getName());
		out.endObject();
	}

	@Override
	public EventsGraph read(JsonReader in) throws IOException {
		EventsGraph graph = new EventsGraph();
		Map<String, EventsNode> nodeMap = new HashMap<>();

		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case "nodes":
					in.beginArray();
					while (in.hasNext()) {
						EventsNode node = deserializeNode(in, graph);
						nodeMap.put(node.getName(), node);
					}
					in.endArray();
					break;

				case "edges":
					in.beginArray();
					while (in.hasNext()) {
						deserializeEdge(in, graph, nodeMap);
					}
					in.endArray();
					break;
				default:
					throw new MalformedJSONException();
			}
		}
		in.endObject();

		return graph;
	}

	private EventsNode deserializeNode(JsonReader in, EventsGraph graph) throws IOException {
		in.beginObject();
		String name = null;
		List<Event> events = new ArrayList<>();

		while (in.hasNext()) {
			switch (in.nextName()) {
				case "name":
					name = in.nextString();
					break;
				case "events":
					in.beginArray();
					while (in.hasNext()) {
						events.add(deserializeEvent(in));
					}
					in.endArray();
					break;
				default:
					throw new MalformedJSONException();
			}
		}
		in.endObject();

		EventsNode node = new EventsNode(graph, name);
		events.forEach(node::addEvent);
		return node;
	}

	private Event deserializeEvent(JsonReader in) throws IOException {
		in.beginObject();
		String poiName = null;
		String poiLocation = null;
		LocalDateTime start = null;
		LocalDateTime end = null;

		while (in.hasNext()) {
			switch (in.nextName()) {
				case "poi":
					in.beginObject();
					while (in.hasNext()) {
						switch (in.nextName()) {
							case "name":
								poiName = in.nextString();
								break;
							case "location":
								poiLocation = in.nextString();
								break;
							default:
								throw new MalformedJSONException();
						}
					}
					in.endObject();
					break;
				case "start":
					start = LocalDateTime.parse(in.nextString());
					break;
				case "end":
					end = LocalDateTime.parse(in.nextString());
					break;
				default:
					throw new MalformedJSONException();
			}
		}
		in.endObject();

		return new Event(
				new PointOfInterest(poiName, poiLocation),
				start,
				end);
	}

	private void deserializeEdge(JsonReader in, EventsGraph graph, Map<String, EventsNode> nodeMap)
			throws IOException {
		in.beginObject();
		String from = null;
		String to = null;

		while (in.hasNext()) {
			switch (in.nextName()) {
				case "from":
					from = in.nextString();
					break;
				case "to":
					to = in.nextString();
					break;
				default:
					throw new MalformedJSONException();
			}
		}
		in.endObject();

		try {
			graph.connect(nodeMap.get(from), nodeMap.get(to));
		} catch (NodeConnectionException e) {
			throw new IOException("Error reconstructing graph", e);
		}
	}

}
