package de.amr.easy.graph.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * Event signaling removal of an edge.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public class EdgeRemovedEvent<V, E extends Edge<V>> {

	private final ObservableGraph<V, E> graph;
	private final E edge;

	public EdgeRemovedEvent(ObservableGraph<V, E> graph, E edge) {
		this.graph = graph;
		this.edge = edge;
	}

	public ObservableGraph<V, E> getGraph() {
		return graph;
	}

	public E getEdge() {
		return edge;
	}
}