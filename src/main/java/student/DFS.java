
package student;
import java.util.HashMap;
import java.util.HashSet;

import graph.Graph;


/**
 * Depth First Search Static Helper Class.
 */
public final class DFS {

	/**
	 * Standard recursive DFS: visits all reachable nodes from 'start'.
	 * Each iteration is a call to visitNode.
	 *
	 * @param g      the graph to traverse
	 * @param start  the starting node
	 * @param visited set to track visited nodes
	 */
	public static <V> void dfs(Graph<V> g, V start, HashSet<V> visited) {
		visitNode(g, start, visited);
	}

	private static <V> void visitNode(Graph<V> g, V node, HashSet<V> visited) {
		// If this node was already visited, stop to avoid cycles
		if (visited.contains(node)) return;
		// Mark the node as visited
		visited.add(node);
		// Recurse on each neighbour
		for (V neighbor : g.neighbours(node)) {
			visitNode(g, neighbor, visited);
		}
	}

	

	/**
	 * Builds a parent map for all nodes in the tree using DFS from the given start node.
	 * Each node's parent is recorded in the map (root has parent null).
	 *
	 * @param g the tree to traverse
	 * @param start the node to start DFS from (usually the root)
	 * @param <V> vertex type
	 * @return HashMap mapping each node to its parent
	 */
	public static <V> HashMap<V, V> dfsParentMap(Graph<V> g, V start) {
		HashMap<V, V> parentMap = new HashMap<>();
		visitNodeStoreParent(g, start, null, parentMap);
		return parentMap;
	}

	private static <V> void visitNodeStoreParent(Graph<V> g, V node, V parent, HashMap<V, V> parentMap) {
		parentMap.put(node, parent);
		for (V neighbor : g.neighbours(node)) {
			if (!parentMap.containsKey(neighbor)) {
				visitNodeStoreParent(g, neighbor, node, parentMap);
			}
		}
	}


	/**
	 * Returns the set of all nodes reachable from 'start' using DFS.
	 *
	 * @param g     the graph to traverse
	 * @param start the starting node
	 * @return set of reachable nodes
	 */
	public static <V> HashSet<V> reachable(Graph<V> g, V start) {
		HashSet<V> visited = new HashSet<>();
		dfs(g, start, visited);
		return visited;
	}
}
