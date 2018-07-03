package de.amr.easy.graph.api;

/**
 * Interface for labeling graph vertices.
 *
 * @param <V>
 *          vertex label type
 */
public interface VertexLabels<V> {

	/**
	 * Returns the vertex label for the given vertex.
	 * 
	 * @param v
	 *          a vertex
	 * @return the label for the vertex or the default vertex label
	 */
	V get(int v);

	/**
	 * Sets the vertex label for the given vertex.
	 * 
	 * @param v
	 *          a vertex
	 * @param vertexLabel
	 *          the vertex label for this vertex
	 */
	void set(int v, V vertexLabel);

	/**
	 * Clears all vertex labels.
	 */
	void clearVertexLabels();

	/**
	 * Sets the default vertex label.
	 * 
	 * @param vertexLabel
	 *          the default vertex label
	 */
	void setDefaultVertexLabel(V vertexLabel);

	/**
	 * @return the default vertex label of the graph
	 */
	V getDefaultVertexLabel();
}