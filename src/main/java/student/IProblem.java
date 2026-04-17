package student;

import java.util.List;

import graph.*;

/**
 * This interface describes the three tasks you are supposed to solve for this
 * compulsory.
 * 
 * @author Olav Bakken, Martin Vatshelle
 */
public interface IProblem {

	/**
	 * Compute the cheapest way to connect all points to the power station.
	 * Compute the minimum spanning of g, and return an ArrayList of all edges in
	 * the spanning tree
	 * 
	 * @param <V> - The type of vertices
	 * @param <E> - The type of Edge weights
	 * @param g   - The graph of possible power connections the power company can
	 *            build
	 * @return a list of edges that form a minimum spanning tree
	 */
    <V, E extends Comparable<E>> List<Edge<V>> mst(WeightedGraph<V, E> g);

	/**
	 * Compute the best point for the power company to start searching for the cause
	 * of a power outage.
	 * 1. The point needs to be adjacent to a power cable such that a failure of
	 * that power cable would cause
	 * both the two given points to loose power.
	 * 2. The point needs to be as close as possible to the two points given.
	 * 
	 * The problem is equivalent to:
	 * Compute the lowest common ancestor of u and v in the tree rooted at root.
	 * 
	 * @param <V>  - type of vertices.
	 * @param g    - The tree of power lines built by the power company.
	 * @param root - The power station.
	 * @param u    - u and v are the two points with no power.
	 * @param v    - u and v are the two points with no power.
	 * @return the best point to search for the power outage.
	 */
    <V> V lca(Graph<V> g, V root, V u, V v);

	/**
	 * The power company need to add one new power cable to mitigate large power
	 * outages.
	 * Find the edge to add, which minimizes the maximum possible power outage.
	 * As the current power grid is a tree adding one edge will create a cycle,
	 * vertices on
	 * this cycle will not loose power if one of the edges on this cycle fails.
	 * 
	 * Removing an edge from a graph might cause a graph to become disconnected,
	 * All vertices disconnected from the power station will loose power if that
	 * edge fails.
	 * Out of all possible edges, the worst one is the edge that leads to the
	 * largest power outage.
	 * We only consider one edge failure (not 2 edge failures at the same time).
	 * 
	 * Find one edge to add such that the worst edge in the new graph causes as
	 * small outage as possible.
	 * 
	 * @param <V>  - type of vertices
	 * @param tree - the tree built by the power company
	 * @return the best edge for the power company to add a power line
	 */
    <V> Edge<V> addRedundant(Graph<V> tree, V root);
}

