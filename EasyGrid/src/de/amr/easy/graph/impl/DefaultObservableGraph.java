package de.amr.easy.graph.impl;

import java.util.HashSet;
import java.util.Set;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.event.EdgeChangeEvent;
import de.amr.easy.graph.api.event.GraphListener;
import de.amr.easy.graph.api.event.VertexChangeEvent;

/**
 * Adjacency set based implementation of an undirected, observable graph.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          vertex type
 * 
 * @param <E>
 *          edge type
 */
public class DefaultObservableGraph<V> extends DefaultGraph<V> implements ObservableGraph<V, SimpleEdge<V>> {

	private Set<GraphListener<V, SimpleEdge<V>>> listeners = new HashSet<>();
	private boolean listeningSuspended = false;

	public DefaultObservableGraph() {
		this.listeningSuspended = false;
	}

	@Override
	public void addGraphListener(GraphListener<V, SimpleEdge<V>> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeGraphListener(GraphListener<V, SimpleEdge<V>> listener) {
		listeners.remove(listener);
	}

	@Override
	public void setEventsEnabled(boolean enabled) {
		listeningSuspended = enabled;
	}

	protected void fireVertexChange(V vertex, Object oldValue, Object newValue) {
		if (!listeningSuspended) {
			for (GraphListener<V, SimpleEdge<V>> listener : listeners) {
				listener.vertexChanged(new VertexChangeEvent<>(this, vertex, oldValue, newValue));
			}
		}
	}

	protected void fireEdgeChange(SimpleEdge<V> edge, Object oldValue, Object newValue) {
		if (!listeningSuspended) {
			for (GraphListener<V, SimpleEdge<V>> listener : listeners) {
				listener.edgeChanged(new EdgeChangeEvent<>(this, edge, oldValue, newValue));
			}
		}
	}

	protected void fireGraphChange(ObservableGraph<V, SimpleEdge<V>> graph) {
		if (!listeningSuspended) {
			for (GraphListener<V, SimpleEdge<V>> listener : listeners) {
				listener.graphChanged(graph);
			}
		}
	}

	@Override
	public void addVertex(V vertex) {
		super.addVertex(vertex);
		fireVertexChange(vertex, null, vertex);
	}

	@Override
	public void addEdge(V v, V w) {
		super.addEdge(v, w);
		edge(v, w).ifPresent(edge -> fireEdgeChange(edge, null, edge));
	}

	@Override
	public void removeEdge(V v, V w) {
		edge(v, w).ifPresent(edge -> {
			super.removeEdge(v, w);
			fireEdgeChange(edge, edge, null);
		});
	}

	@Override
	public void removeEdges() {
		super.removeEdges();
		fireGraphChange(this);
	}
}
