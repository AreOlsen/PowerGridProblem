package student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import graph.*;

public class ProblemSolver implements IProblem {

	/**
	 * Kruskal's algorithm for finding the Minimum Spanning Tree (MST) of a weighted graph.
	 *
	 * This algorithm sorts all edges by weight and adds them one by one to the MST,
	 * skipping any edge that would create a cycle. The Disjoint Set Union (DSU) data structure
	 * is used to efficiently check for cycles and merge components.
	 * 
	 * Time complexity: O(E log E) for sorting edges, plus near-linear time for DSU operations.
	 * 
	 * @param g the weighted graph
	 * @param <V> vertex type
	 * @param <E> edge weight type (must be Comparable)
	 * @return list of edges in the MST
	 */
	@Override
	public <V, E extends Comparable<E>> ArrayList<Edge<V>> mst(WeightedGraph<V, E> g) {
		// Result MST: stores the edges of the minimum spanning tree
		ArrayList<Edge<V>> results = new ArrayList<>();

		// Sort edges in increasing order of weight
		ArrayList<Edge<V>> edgeList = new ArrayList<>();
		g.edges().forEach(edgeList::add);
		edgeList.sort(Comparator.comparing(g::getWeight));

		// Initialize DSU parent and rank maps for all vertices
		HashMap<V, V> parent = new HashMap<>();
		HashMap<V, Integer> rank = new HashMap<>();
		for (V vertex : g.vertices()) {
			parent.put(vertex, vertex); // each vertex is its own parent
			rank.put(vertex, 0);        // initial rank is 0
		}

		// Iterate through sorted edges and add to MST if they connect different components
		for (Edge<V> edge : edgeList) {
			// Find the root of each endpoint of the edge
			V rootA = DSU.find(edge.a, parent);
			V rootB = DSU.find(edge.b, parent);
			// If the endpoints are in different trees (not already connected, aka. no cycle), add the edge and connect them
			if (!rootA.equals(rootB)) {
				results.add(edge); // add edge to MST
				DSU.unionRoots(rootA, rootB, parent, rank); // connect the trees
			}
		}
		// Return the list of MST edges
		return results;
	}



	/**
	 * Finds the Lowest Common Ancestor (LCA) of two nodes in a tree using parent pointers.
	 *
	 * This method first builds a parent map for all nodes using DFS from the root.
	 * Then, it walks up from node u to the root, storing all visited nodes in a set.
	 * Next, it walks up from node v to the root, returning the first node that is also in u's path.
	 * This is the lowest common ancestor of u and v.
	 *
	 * Time complexity: O(V) per query, since the parent map is rebuilt each time.
	 *
	 * @param g    the tree/graph
	 * @param root the root of the tree
	 * @param u    first node
	 * @param v    second node
	 * @return the lowest common ancestor of u and v
	 */
	@Override
	public <V> V lca(Graph<V> g, V root, V u, V v) {
		// Build parent map for all nodes using DFS from root.
		HashMap<V, V> parentMap =  DFS.dfsParentMap(g, root);

		// Walk up from u to root, storing all visited nodes in a set
		HashSet<V> pathU = new HashSet<>();
		V current = u;
		while (current != null) {
			pathU.add(current);
			current = parentMap.get(current);
		}

		// Walk up from v to root, return the first node also in u's path
		current = v;
		while (current != null) {
			if (pathU.contains(current)) return current;
			current = parentMap.get(current);
		}
		// Should not happen in a valid tree
		return null;
	}

	
	/**
	 * Finds and returns an edge that connects the lowest nodes in the two largest subtrees of the root,
	 * or connects the lowest node in the only subtree to the root if there is only one subtree.
	 *
	 * This is useful for adding a redundant edge to a tree (such as an MST) to improve connectivity and resilience.
	 * The method computes subtree sizes using DFS, sorts the root's children by size, and greedily descends to the lowest node
	 * in each subtree by always following the largest child. The returned edge is optimal for connecting the deepest points
	 * in the largest subtrees.
	 *
	 * @param g the tree/graph (typically an MST)
	 * @param root the root of the tree
	 * @return the redundant edge to add, or null if not possible
	 */
	@Override
	public <V> Edge<V> addRedundant(Graph<V> g, V root) {
		// Compute subtree sizes for all nodes
		HashMap<V, Integer> subtrees = new HashMap<>();
		SubTreeHelper.computeSubtreeSizes(g, root, null, subtrees);

		// Find the subtrees directly under the root
		ArrayList<V> children = new ArrayList<>();
		for (V neighbor : g.neighbours(root)) {
			children.add(neighbor);
		}

		if (children.isEmpty()) return null; // no subtrees

		if (children.size() == 1) {
			// Only one subtree: connect its lowest node to the root.
			V lowest = SubTreeHelper.findLowestNode(g, children.get(0), root, subtrees);
			return new Edge<>(root, lowest);
		}

		// Sort children by descending subtree size
		children.sort((a, b) -> Integer.compare(subtrees.get(b), subtrees.get(a)));

		// Otherwise, connect the lowest nodes in the two largest subtrees
		V firstSubtree = children.get(0);
		V secondSubtree = children.get(1);
		V lowestA = SubTreeHelper.findLowestNode(g, firstSubtree, root, subtrees);
		V lowestB = SubTreeHelper.findLowestNode(g, secondSubtree, root, subtrees);
		return new Edge<>(lowestA, lowestB);
	}
}