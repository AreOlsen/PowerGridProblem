
package student;

import java.util.HashMap;

/**
 * Disjoint Set Union (DSU) Static Helper Class.
 *
 * DSU efficiently tracks a partition of a set into disjoint subsets and supports two main operations:
 * - find: Determines the representative (root) of the subset containing a given element, with path compression.
 * - unionRoots: Merges two subsets by their roots, using union by rank to keep trees shallow.
 */
public final class DSU {

	/**
	 * Finds the representative (root) of the set containing v, with path compression.
	 *
	 * @param v      the element to find
	 * @param parent map from element to its parent
	 * @param <V>    element type
	 * @return the root of the set containing v
	 * @throws IllegalArgumentException if v is not found in parent
	 */
	public static <V> V find(V v, HashMap<V, V> parent) {
		// Get the parent of v
		V p = parent.get(v);
		if (p == null)
			throw new IllegalArgumentException("Vertex not found in DSU: " + v);
		
		// If v is its own parent, it is the root
		if (p.equals(v))
			return v;
		
		// Recursively find the root of v's parent
		V root = find(p, parent);

		// Path compression: directly link v to its root for efficiency
		parent.put(v, root);

		// Return the root representative
		return root;
	}



	/**
	 * Merges two sets by their roots, using union by rank to keep trees shallow.
	 * Does not perform additional find() calls; expects ra and rb to be roots.
	 *
	 * @param ra    root of first set
	 * @param rb    root of second set
	 * @param parent map from element to its parent
	 * @param rank   map from element to its rank
	 * @param <V>    element type
	 */
	public static <V> void unionRoots(V ra, V rb, HashMap<V, V> parent, HashMap<V, Integer> rank) {
		// If both roots are the same, they are already connected
		if (ra.equals(rb))
			return;

		// Get the rank (tree height) of each root
		int rA = rank.getOrDefault(ra, 0);
		int rB = rank.getOrDefault(rb, 0);

		// Attach the smaller tree under the larger tree to keep trees shallow
		if (rA < rB) {
			parent.put(ra, rb); // ra becomes child of rb
		} else if (rA > rB) {
			parent.put(rb, ra); // rb becomes child of ra
		} else {
			// If ranks are equal, arbitrarily choose one as new root and increment its rank
			parent.put(rb, ra);
			rank.put(ra, rA + 1);
		}
	}
}
