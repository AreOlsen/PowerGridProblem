package graph;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is a weighted graph where each edge has a weight.
 * This class also work as a Comparator for Edges by using the natural
 * order of the edge weights to compare edges.
 * 
 * @author Martin Vatshelle, Olav Bakken
 *
 * @param <V> Type of vertices
 * @param <E> Type of weights, needs to be comparable
 */
public class WeightedGraph<V, E extends Comparable<E>> implements IGraph<V>, Comparator<Edge<V>> {

	private HashMap<Edge<V>, E> edgeWeight;
	private Graph<V> graph;

	/**
	 * Constructs an empty weighted graph
	 */
	public WeightedGraph() {
		graph = new Graph<V>();
		edgeWeight = new HashMap<Edge<V>, E>();
	}

	/**
	 * Gets the edge weight between a pair of vertices
	 */
	public E getWeight(V a, V b) {
		return getWeight(new Edge<V>(a, b));
	}

	/**
	 * Gets the edge weight of a given edge
	 */
	public E getWeight(Edge<V> e) {
		return edgeWeight.get(e);
	}

	/**
	 * Sets the weight of the edge between a pair of vertices
	 * If there is no edge between those vertices an exception is thrown
	 */
	public void setWeight(V a, V b, E weight) {
		if (!adjacent(a, b))
			throw new IllegalArgumentException("These edges are not adjacent.");
		edgeWeight.put(new Edge<V>(a, b), weight);
	}

	/**
	 * Adds a vertex to the graph if not already in the graph
	 * 
	 * @param v the vertex to add
	 * @return true if a vertex was added, false otherwise
	 */
	public boolean addVertex(V v) {
		return graph.addVertex(v);
	}
	
	/**
	 * This method adds an edge to the graph.
	 * Loops are not supported, addEdge(a,a) should return false;
	 * @param a,b    the pair of vertices to add an edge between
	 * @param weight the weight of the edge
	 * @return true if an edge was added, false otherwise
	 */
	public boolean addEdge(V a, V b, E weight) {
		if (weight == null)
			throw new IllegalArgumentException("Edge weights can not be null.");
		if (graph.addEdge(a, b)) {
			setWeight(a, b, weight);
			return true;
		}
		return false;
	}

	public boolean addEdge(Edge<V> e, E weight) {
		return addEdge(e.a, e.b, weight);
	}

	/**
	 * Removes a vertex from the graph if the vertex exist
	 * 
	 * @param a the vertex to remove
	 * @return true if a vertex was removed, false otherwise
	 */
	public boolean removeVertex(V a) {
		if(!graph.hasVertex(a))
			return false;
		Iterable<V> neighboursCopy = new HashSet<V>((Collection<? extends V>) neighbours(a));
		for (V b : neighboursCopy) {
			removeEdge(a, b);
		}
		return graph.removeVertex(a);
	}
	
	/**
	 * Removes an edge from the graph if the edge exist
	 * 
	 * @param a,b the pair of vertices to remove the edge from
	 * @return true if an edge was removed, false otherwise
	 */
	public boolean removeEdge(V a, V b) {
		if(a.equals(b))
			return false;
		return removeEdge(new Edge<V>(a, b));
	}

	public boolean removeEdge(Edge<V> e) {
		boolean changed = graph.removeEdge(e);
		if (changed)
			edgeWeight.remove(e);
		return changed;
	}

	@Override
	public int compare(Edge<V> o1, Edge<V> o2) {
		return getWeight(o1.a, o1.b).compareTo(getWeight(o2.a, o2.b));
	}

	@Override
	public WeightedGraph<V, E> clone() {
		WeightedGraph<V, E> g = new WeightedGraph<V, E>();
		for (V v : vertices())
			g.addVertex(v);
		for (Edge<V> e : edges())
			g.addEdge(e.a, e.b, getWeight(e));
		return g;
	}

	@Override
	public Iterable<V> vertices() {
		return graph.vertices();
	}

	@Override
	public Iterable<Edge<V>> edges() {
		return graph.edges();
	}

	public boolean hasVertex(V node) {
		return graph.hasVertex(node);
	}
	
	@Override
	public boolean adjacent(V a, V b) {
		return graph.adjacent(a, b);
	}

	public Iterable<Edge<V>> adjacentEdges(V v) {
		return graph.adjacentEdges(v);
	}

	public V getFirstNode() {
		return graph.getFirstNode();
	}

	@Override
	public Set<V> neighbours(V v) {
		return graph.neighbours(v);
	}

	@Override
	public int degree(V v) {
		return graph.degree(v);
	}
}
