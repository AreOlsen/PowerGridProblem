package student;

import java.util.ArrayList;
import java.util.HashMap;

import graph.Graph;

/**
 * Static Sub Tree Helper Class.
 */
public final class SubTreeHelper {
    private SubTreeHelper(){}


	/**
	 * Greedily descends to the lowest node in a subtree by always choosing the child with the largest subtree size.
	 *
	 * @param g the graph/tree to traverse
	 * @param node the starting node of the subtree
	 * @param parent the parent of the starting node (or null for root)
	 * @param subtrees a map containing the size of each subtree (precomputed)
	 * @param <V> vertex type
	 * @return the lowest node found by greedy descent
	 */
	public static <V> V findLowestNode(Graph<V> g, V node, V parent, HashMap<V, Integer> subtrees) {
		V current = node;
		while (true) {
			// Collect all children of the current node (excluding the parent)
			ArrayList<V> children = new ArrayList<>();
			for (V neighbor : g.neighbours(current)) {
				if (!neighbor.equals(parent)) {
					children.add(neighbor);
				}
			}
			// If there are no children, we've reached a leaf node
			if (children.isEmpty()) break;

			// Pick the child with the largest subtree size
			V next = children.get(0);
			for (V child : children) {
				if (subtrees.get(child) > subtrees.get(next)) {
					next = child;
				}
			}

			// Move down to the chosen child, updating parent and current
			parent = current;
			current = next;
		}
		// Return the lowest node found by greedy descent
		return current;
	}


	/**
	 * Recursively computes the size of the subtree rooted at each node and stores it in the provided map.
	 * Note: Destructive function for subtrees parameter.
     * 
	 * @param g the graph/tree to traverse
	 * @param node the current node
	 * @param parent the parent of the current node (or null for root)
	 * @param subtrees a map to store the size of each subtree
	 * @param <V> vertex type
	 * @return the size of the subtree rooted at the current node
	 */
	public static <V> int computeSubtreeSizes(Graph<V> g, V node, V parent, HashMap<V, Integer> subtrees) {
		int size = 1; // Start with 1 to count the current node itself

		// Recursively visit all children (excluding the parent to avoid cycles)
		for (V neighbor : g.neighbours(node)) {
			if (neighbor.equals(parent)) continue; // Skip the parent node
			// Add the size of the child's subtree to the current node's size
			size += computeSubtreeSizes(g, neighbor, node, subtrees);
		}

		// Store the computed subtree size for this node in the map
		subtrees.put(node, size);

		return size;
	}
}
