package de.amr.easy.graph.api;

/**
 * Interface for accessing content stored in a graph vertex.
 *
 * @param <C>
 *          vertex content type
 */
public interface VertexContent<C> {

	/**
	 * Returns the content of the given vertex.
	 * 
	 * @param vertex
	 *          a vertex
	 * @return the content or the default content if no content has been set for this vertex
	 */
	public C get(int vertex);

	/**
	 * Sets the content of the given vertex.
	 * 
	 * @param vertex
	 *          a vertex
	 * @param data
	 *          the vertex content
	 */
	public void set(int vertex, C data);

	/**
	 * Clears the complete grid content.
	 */
	public void clearContent();

	/**
	 * Sets the default content for the cells
	 * 
	 * @param data
	 *          the default content
	 */
	public void setDefaultContent(C data);

	/**
	 * @return the default content of the cells
	 */
	public C getDefaultContent();
}