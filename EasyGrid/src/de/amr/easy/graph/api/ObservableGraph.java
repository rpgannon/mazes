package de.amr.easy.graph.api;

import de.amr.easy.graph.api.event.GraphListener;

/**
 * A graph whose vertex and edge state can be observed by listeners.
 * 
 * @param <V>
 *          the graph vertex type
 * @param <E>
 *          the edge type
 */
public interface ObservableGraph<V, E extends Edge<V>> extends Graph<V, E> {

	public void addGraphListener(GraphListener<V, E> listener);

	public void removeGraphListener(GraphListener<V, E> listener);

	public void setEventsEnabled(boolean enabled);
}
