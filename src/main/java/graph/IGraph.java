package graph;

import java.util.Set;

/**
 * This interface represents a graph
 * A graph consists of a set of vertices and edges.
 * Edges connects pairs of vertices.
 * 
 * @author Martin Vatshelle
 *
 * @param <V> The type of vertices in the graph
 */
public interface IGraph<V> {

	/**
	 * Gives a way to iterate through all vertices of this graph
	 */
    Iterable<V> vertices();

	/**
	 * Gives a way to iterate through all edges of this graph
	 */
    Iterable<Edge<V>> edges();

	/**
	 * Checks if two given vertices are adjacent
	 * 
	 * @return true if both vertices are in the graph and there is an edge between
	 *         them in the graph
	 */
    boolean adjacent(V a, V b);

	/**
	 * Gives a way to iterate through the neighbours of a vertex
	 */
    Set<V> neighbours(V v);

	/**
	 * Counts the number of neighbours a vertex has
	 */
    int degree(V v);
}
