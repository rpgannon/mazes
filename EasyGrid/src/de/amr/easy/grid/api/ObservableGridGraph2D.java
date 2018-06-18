package de.amr.easy.grid.api;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.event.ObservableGraph;

/**
 * A two-dimensional grid that can be observed by graph observers.
 * 
 * @author Armin Reichert
 */
public interface ObservableGridGraph2D<E extends Edge> extends GridGraph2D<E>, ObservableGraph<E> {
}