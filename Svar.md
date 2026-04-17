# Answer File - Semesteroppgave 2

# Description of each Implementation

Below are the approaches taken during the answerings of the tasks, and below that are the different methods implemented in the program and their runtime analysis.

## Task 1 - mst

The two obvious choices for the MST problem are Prim's algorithm and Kruskal's algorithm; before deciding which to implement I decided to do some research on which may be more performant and why. Kruskal's algorithm is usually denoted as being O(E log E), and Prim's algorithm can be O(V^2) if using a naive adjacency-based approach, O(E log V) when using an adjacency list with a binary heap (Java's PriorityQueue), and O(E + V log V) if using a Fibonacci heap with an adjacency list. As Fibonacci heaps are quite mathematically heavy and outside the course scope of INF102, the two realistic choices were Prim's algorithm with a priority queue and Kruskal's algorithm. Using a little bit of mathematics we can see that they are actually the same in terms of runtime.

### Proof that Prim's Algorithm and Kruskal's Algorithm are time-complexity-wise the same in connected graphs:

We know that in all fully connected graphs the following statement is true:
$\frac{V}{2}\leq E \leq V^2$.

We can use this to prove that Kruskals algorithm and Prims algorithm with a Priorityqueue is the same runtime wise.

$\frac{V}{2}\leq E \leq V^2$.

$\frac{1}{2}\lg V \leq \lg E \leq 2\lg V$.

$\frac{1}{2}E\lg V \leq E \lg E \leq 2 E \lg V$.

$O(\tfrac{1}{2}E\lg V) \leq O(E \lg E) \leq O(2E\lg V)$

$O(E\lg V) \leq O(E \lg E) \leq O(E\lg V)$

$\dots\; O(E \lg V) = O(E \lg E)$. I.e. Prim's algorithm w/ priority queue has the same runtime as Kruskal's algorithm.

### Choosing Kruskal's Algorithm due to preference and learning.

Using this, we can decide between these two choices by our own preference, or learning wish.

I went with Kruskals Algorithm as it was something I had a wish to explore more, especially due to the DSU concepts I were not too familiar with, as well as carrying more cleanliness in implementation than Prim's algorithm.

To implement Kruskals Algorithm I added a static helper class for the Disjoint Union Set Operations; (this is done by setting the class as final to prevent hierarchy, and using private constructor to prevent instantiation).

Due to the abstraction with the DSU class, and the simplicity of Kruskals Algorithm there were no issues with implementation, except for one minor bit. That bit being the fact that `g.edges()` returns an Iterable, and not a list or hashset. This was quite easy to overcome with just `g.edges().forEach()`.

Pseudocode I implemented (Kruskal):

```
edges = list(g.edges())          // convert Iterable to list
sort edges by weight
for each vertex v: parent[v] = v; rank[v] = 0   // DSU init
mst = []
for edge in edges:
	if find(edge.a) != find(edge.b):
		mst.add(edge)
		union(edge.a, edge.b)
return mst
```

## Task 2 - lca

Before starting on this task I did some research to find out what the common ways to implement and solve this issue is. There seems to be quite a lot of different ways to implement it; one of the most efficient for many static-query scenarios is the DFS Euler-tour + RMQ approach.

### DFS Euler-tour + RMQ (LCA via DFS Euler tour and Range Minimum Query)

- Idea: run a DFS from the root and record every visit to a node in an `euler` list along with its depth in `depth`. Also record `first[v]`, the index of the first occurrence of vertex `v` in `euler`. The LCA of two nodes u and v is the node with minimum depth on the interval between `first[u]` and `first[v]` in the euler tour, so an RMQ over `depth[]` answers LCA queries.
  The benefit of DFS Euler-tour is that it allows for O(logV) queries after the DFS preprocessing search that is O(V) ( for trees, as is the case in this task).

The small issue occurs when the graph changes as then the preprocessing must be done again for the new graph.

Considering the limitation that the DFS preprocessing brings, I decided that there was no reason why not to use a simpler, but a little slower, algorithm that still runs in O(V) but doesnt require that much mathematics behind it.

### My approach:

- Idea: Run a DFS search from the root and downwards, storing the parent and its children nodes. Then we can iterate upwards from the first node, u, to the root, storing which nodes are in that path. Then we iterate from the other node, v, towards the root, until we find the first common node with the u path. As we are iterating from the bottom upwards towards the root, this is garantueed to be the lowest common factor of them.

Implementation:

- Creating a function in the static helper class DFS which allows for abstraction of the dfs search with parent storing.
- Use one while loop that loops upwards the parent map until parent node is null (i.e. we are the root).
- Use one while loop that loops upwards the parent map until parent node is null (i.e. we are the root), or the current node is in the path the first while loop took (i.e. we found the lca).

No issues or problems were encountered during implementation, and efficency is sufficient.

Pseudocode I Implemented (parent-map DFS LCA):

```
# implementation used in ProblemSolver
parentMap = DFS.dfsParentMap(g, root)
pathU = empty set
cur = u
while cur != null:
	pathU.add(cur)
	cur = parentMap.get(cur)
cur = v
while cur != null:
	if pathU.contains(cur): return cur
	cur = parentMap.get(cur)
return null
```

## Task 3 - addRedundant

The `addRedundant` task can be viewed as a min–max problem: choose a single new edge to add so that, after adding it, the removal of the most critical edge (the single edge whose removal disconnects the most nodes) leaves as few nodes disconnected as possible. In other words, we try to minimize the worst-case number of disconnected nodes after deleting the single most damaging edge. This was my initial interpretation of the task, although the final implementation uses a more direct greedy strategy described below.

Using this knowledge we can write up a quick naive way to do it, using the following plan:

### First Idea: Naive min–max

Idea: brute-force every possible undirected new edge (no self-loops, exclude existing edges); for each candidate, simulate adding it and compute the worst-case number of nodes disconnected when the single most damaging edge is removed. Pick the candidate minimizing that worst-case.

Pseudocode (undirected candidates):

```
bestEdge = null
bestWorst = +inf
for each unordered pair {a,b} with a != b and not edge(a,b):
	add edge {a,b}
	worst = 0
	for each edge e in graph:
		remove e
		disconnected = count_unreachable_from_root()
		worst = max(worst, disconnected)
		restore e
	if worst < bestWorst: bestWorst = worst; bestEdge = {a,b}
	remove {a,b}
return bestEdge
```

Number of candidate edges to try (undirected, no self-loops): $\frac{V(V-1)}{2}- \text{existingEdgesCount}$.

Here the problem is quite obvious. When we get to say 10_000 nodes in the tree, we have a huge amount of edges to check and simulating each one would incredibly time inefficient. This does garantuee the optimal solution no matter what graph structure, but this approach is just too inefficient.

Key idea: because the input is a tree, removing any edge cuts off exactly one subtree from the root. By connecting the two largest subtrees at their deepest nodes we create an alternate path between those large components, so even if an edge higher in one of those subtrees is removed the majority of nodes remain connected via the new link. We prioritise the largest subtrees (not the smaller ones) because they dominate the worst-case number of disconnected nodes. If the root only has one subtree, we the connect deepest node with the root as this ensures that there is no case where the entire tree is cut off, and we still prioritise the biggest subtree.

### Second idea: Connecting the deepest nodes in largest subtrees.

Idea (greedy): compute subtree sizes under the root, pick the two largest subtrees and connect their deepest nodes (found by repeatedly descending into the child with largest subtree). If the root has only one subtree (i.e. a single child), the goal is to connect the root to the deepest node inside that subtree — this gives the best protection because any edge removed higher in that subtree still leaves its nodes connected via the new deep link. This targets the deepest points of the largest components and reduces the size of the largest possible disconnected piece.

Pseudocode I implemented (greedy approach):

```
compute subtree_size[v] for all v
children = neighbours(root)
if children.size() == 0: return null
if children.size() == 1: return edge(root, deepest(children[0]))
sort children by subtree_size descending
a = deepest(children[0])
b = deepest(children[1])
return edge(a,b)

function deepest(node):
	while node has child (excluding parent) with maximum subtree_size:
		node = that child
	return node
```

# Runtime Analysis

For each method of the different strategies give a runtime analysis in Big-O notation and a description of why it has this runtime.

## Helper Methods:

## DFS (src/main/java/student/DFS.java)

### `dfs(Graph<V> g, V start, HashSet<V> visited)` — Entry point that initializes traversal and calls the internal recursive worker `visitNode`.

- **O(V+E)**: visits each node once and iterates all edges (Σdeg(v)=2E).

```java
public static <V> void dfs(Graph<V> g, V start, HashSet<V> visited) { // O(V+E)
    // single call to the recursive worker; the heavy work happens inside visitNode
    visitNode(g, start, visited); // recursive DFS over the component: total O(V+E)
}
```

### `visitNode(Graph<V> g, V node, HashSet<V> visited)` — Internal recursive worker used by `dfs`.

- **O(V+E)**: visits each node once and iterates all edges (Σdeg(v)=2E).

```java
private static <V> void visitNode(Graph<V> g, V node, HashSet<V> visited) { //O(V+E).
	if (visited.contains(node)) return; // O(1) once per node -> O(V)
	visited.add(node); // O(1) once per node -> O(V)
	for (V neighbor : g.neighbours(node)) { // iterates over edges: Σdeg(v)=2E -> O(E)).
		visitNode(g, neighbor, visited); // recursive DFS
	}
}
```

### `dfsParentMap(Graph<V> g, V start)` — Build a map from each node to its parent using DFS (root mapped to null).

- **O(V+E)**: Only calls `visitNodeStoreParent` which has **O(V+E).**

```java
public static <V> HashMap<V,V> dfsParentMap(Graph<V> g, V start) { // O(V+E)
  HashMap<V,V> parentMap = new HashMap<>(); // O(1)
  visitNodeStoreParent(g, start, null, parentMap); // O(V+E) full DFS
  return parentMap; // O(1)
}
```

### `visitNodeStoreParent(Graph<V> g, V node, V parent, HashMap<V, V> parentMap)` — Recursive helper that records parent pointers while DFSing.

- **O(V+E)**: performs a DFS and touches each vertex once and each edge twice (undirected).

```java
private static <V> void visitNodeStoreParent(Graph<V> g, V node, V parent, HashMap<V,V> parentMap) { // O(V+E)
  parentMap.put(node, parent); // O(1) once per node -> O(V)
  for (V neighbor : g.neighbours(node)) { // iterates over edges: Σdeg(v)=2E -> O(E)
    if (!neighbor.equals(parent)) {
      visitNodeStoreParent(g, neighbor, node, parentMap); // recursive DFS
    }
  }
}
```

### `reachable(Graph<V> g, V start)` — Convenience wrapper that returns the set of nodes reachable from `start`.

- **O(V+E)**: calls `dfs`, which does the full traversal.

```java
public static <V> HashSet<V> reachable(Graph<V> g, V start) { // O(V+E)
  HashSet<V> visited = new HashSet<>(); // O(1)
  dfs(g, start, visited); // O(V+E)
  return visited; // O(1)
}
```

## DSU (src/main/java/student/DSU.java)

### `find(V v, HashMap<V, V> parent)` — Find representative (root) of `v` with path compression.

- **Amortized nearly-constant time** — effectively constant per operation with path compression for all practical input sizes.

```java
public static <V> V find(V v, HashMap<V,V> parent) { // amortized O(1)
    V p = parent.get(v); // O(1)
    if (p == null) throw new IllegalArgumentException(...); // O(1)
    if (p.equals(v)) return v; // O(1) base case
    V root = find(p, parent); // recursive call on parent (amortized small depth)
    parent.put(v, root); // path compression: O(1)
    return root; // O(1)
}
```

### `unionRoots(V ra, V rb, HashMap<V, V> parent, HashMap<V, Integer> rank)` — Union two root nodes using union-by-rank.

- **Amortized nearly-constant time** — uses rank to attach the smaller tree under the larger; combined with `find` this is effectively constant for practical input sizes.

```java
public static <V> void unionRoots(V ra, V rb, HashMap<V,V> parent, HashMap<V,Integer> rank) { // amortized O(α(n))
    if (ra.equals(rb)) return; // O(1)
    int rA = rank.getOrDefault(ra,0); // O(1)
    int rB = rank.getOrDefault(rb,0); // O(1)
    if (rA < rB) parent.put(ra, rb); // O(1)
    else if (rA > rB) parent.put(rb, ra); // O(1)
    else { parent.put(rb, ra); rank.put(ra, rA+1); } // O(1)
}
```

## SubTreeHelper (src/main/java/student/SubTreeHelper.java)

### `findLowestNode(Graph<V> g, V node, V parent, HashMap<V, Integer> subtrees)` — Greedily descend into largest child repeatedly to find a deep node.

- **O(h + Σdeg(visited))** where Σdeg(visited) is the total number of neighbour checks made while descending (i.e., sum of degrees of visited nodes), and h is height (i.e. the number of layers down we iterate over); worst-case this is **O(V + E)=O(V+V-1)=O(2V)=O(V)** as **E=V-1** in trees**, but if degrees are bounded the cost is **O(h)\*\*.

```java
public static <V> V findLowestNode(Graph<V> g, V node, V parent, HashMap<V,Integer> subtrees) {
  V current = node; // O(1)
  while (true) {
    ArrayList<V> children = new ArrayList<>(); // O(1)
    for (V neighbor : g.neighbours(current)) { // scan neighbors: O(deg(current))
      if (!neighbor.equals(parent)) children.add(neighbor); // O(1) per neighbor
    }
    if (children.isEmpty()) break; // leaf reached
    // choose child with max subtree (iterate children list): O(#children)
    V next = children.get(0);
    for (V child : children) if (subtrees.get(child) > subtrees.get(next)) next = child;
    parent = current; // O(1)
    current = next; // O(1)
  }
  return current; // O(1)
}
```

### `computeSubtreeSizes(Graph<V> g, V node, V parent, HashMap<V, Integer> subtrees)` — Recursively compute subtree sizes and store them in the map.

- **O(V+E)**: recursively visits every node once and examines each edge once (tree: O(V)).

```java
public static <V> int computeSubtreeSizes(Graph<V> g, V node, V parent, HashMap<V,Integer> subtrees) { // O(V+E)
    int size = 1; // O(1)
    for (V neighbor : g.neighbours(node)) { // iterates edges -> O(E)
        if (neighbor.equals(parent)) continue; // O(1)
        size += computeSubtreeSizes(g, neighbor, node, subtrees); // recursive
    }
    subtrees.put(node, size); // O(1)
    return size; // O(1)
}
```

## Main Problem Solving Methods:

## `mst(WeightedGraph<T, E> g)`: Time O(E log E).

```java
	@Override
	public <V, E extends Comparable<E>> ArrayList<Edge<V>> mst(WeightedGraph<V, E> g) {
		// Result MST: stores the edges of the minimum spanning tree
		ArrayList<Edge<V>> results = new ArrayList<>(); // O(1)

		// Collect and sort edges in increasing order of weight
		ArrayList<Edge<V>> edgeList = new ArrayList<>();
		g.edges().forEach(edgeList::add); // O(E) to iterate all edges and add to list
		edgeList.sort(Comparator.comparing(g::getWeight)); // O(E log E) sort dominates

		// Initialize DSU parent and rank maps for all vertices
		HashMap<V, V> parent = new HashMap<>(); // O(1)
		HashMap<V, Integer> rank = new HashMap<>(); // O(1)
		for (V vertex : g.vertices()) { // O(V) to initialize maps
			parent.put(vertex, vertex); // O(1) per vertex
			rank.put(vertex, 0);        // O(1) per vertex
		}

		// Iterate through sorted edges and add to MST if they connect different components
		for (Edge<V> edge : edgeList) { // loop runs E times
			// Find the root of each endpoint of the edge
			V rootA = DSU.find(edge.a, parent); // amortized O(1)
			V rootB = DSU.find(edge.b, parent); // amortized O(1)
			// If the endpoints are in different trees (no cycle), add the edge and connect them
			if (!rootA.equals(rootB)) {
				results.add(edge); // O(1) to append
				DSU.unionRoots(rootA, rootB, parent, rank); // amortized O(1)
			}
		}
		// Return the list of MST edges
		return results; // total: O(E log E + V + E) => O(E log E), as O(V)=O(E).
	}
```

### Reasoning for runtime of `mst`

- Collecting edges: `g.edges().forEach(edgeList::add)` — O(E) to build the list from the Iterable.
- Sorting edges: `edgeList.sort(...)` — O(E log E), this dominates the runtime.
- DSU initialization: `for (vertex : g.vertices())` — O(V).
- Iterating sorted edges: E iterations, each performing amortized O(1) work `find`/`union`, so ≈ O(E).

Therefore the total cost is O(E log E + V + E) = O(E log E + V). For connected graphs we have O(V)=O(E), so the V term is lower order and is absorbed: O(E log E + V) = O(E log E). The DSU operations contribute only near-constant overhead per edge (amortized), so they don't change the leading term.

## `lca(Graph<T> g, T root, T u, T v)`: Time O(V + E) = O(V) per call.

However, as these are trees and not standard graphs: E=V-1, meaning
**O(V+E)=O(V+V-1)=O(2V)=O(V)**.

```java
		// Build parent map for all nodes using DFS from root.
		HashMap<V, V> parentMap =  DFS.dfsParentMap(g, root); // O(V+E) to build parent map via DFS

		// Walk up from u to root, storing all visited nodes in a set
		HashSet<V> pathU = new HashSet<>(); // O(1) init
		V current = u;
		while (current != null) { // O(height_u) ≤ O(V) ancestor steps
			pathU.add(current); // O(1) per node
			current = parentMap.get(current); // O(1)
		}

		// Walk up from v to root, return the first node also in u's path
		current = v;
		while (current != null) { // O(height_v) ≤ O(V)
			if (pathU.contains(current)) return current; // O(1) contains check
			current = parentMap.get(current); // O(1)
		}
		// Should not happen in a valid tree
		return null; // total: O(V+E) time dominated by DFS building parentMap
```

### Reasoning for runtime of `lca`

- Build parent map: `DFS.dfsParentMap(g, root)` does one DFS over the tree: O(V + E) = O(V) for trees.
- Collect ancestors of `u` into `pathU`: O(height(u)) ≤ O(V).
- Walk up from `v` until a match: O(height(v)) ≤ O(V).

Combined: O(V + height(u) + height(v)) = O(V) worst-case, so the method is O(V) per query (space O(V)).

## `addRedundant(Graph<T> g, T root)`: Time O(VlogV), θ(V).

However, as these are trees and not standard graphs: E=V-1, meaning
**O(V+E)=O(V+V-1)=O(2V)=O(V)**.

```java
	@Override
	public <V> Edge<V> addRedundant(Graph<V> g, V root) {
		// Compute subtree sizes for all nodes
		HashMap<V, Integer> subtrees = new HashMap<>();
		SubTreeHelper.computeSubtreeSizes(g, root, null, subtrees); // O(V+E) to compute sizes

		// Find the subtrees directly under the root
		ArrayList<V> children = new ArrayList<>();
		for (V neighbor : g.neighbours(root)) { // O(deg(root)) to gather children
			children.add(neighbor); // O(1) per neighbour
		}

		if (children.isEmpty()) return null; // O(1) no subtrees

		if (children.size() == 1) {
			// Only one subtree: connect its lowest node to the root.
			V lowest = SubTreeHelper.findLowestNode(g, children.get(0), root, subtrees); // O(h + Σdeg_visited) bounded by O(V+E)
			return new Edge<>(root, lowest); // O(1)
		}

		// Sort children by descending subtree size
		children.sort((a, b) -> Integer.compare(subtrees.get(b), subtrees.get(a))); // O(k log k), k = #children ≤ V

		// Otherwise, connect the lowest nodes in the two largest subtrees
		V firstSubtree = children.get(0); // O(1)
		V secondSubtree = children.get(1); // O(1)
		V lowestA = SubTreeHelper.findLowestNode(g, firstSubtree, root, subtrees); // O(h1 + Σdeg_visited1)
		V lowestB = SubTreeHelper.findLowestNode(g, secondSubtree, root, subtrees); // O(h2 + Σdeg_visited2)
		return new Edge<>(lowestA, lowestB); // total: dominated by initial DFS → O(V+E)
	}
```

### Reasoning for runtime of `addRedundant`

- `SubTreeHelper.computeSubtreeSizes(...)` — a single DFS over the tree: O(V + E) = O(V) for trees.
- collecting `children` (neighbours of `root`) — O(k) where k = deg(root).
- sorting `children` by subtree size — O(k log k).
- two calls to `findLowestNode` (one per chosen subtree) — each descends along a path and checks neighbours; together they are O(sum of degrees visited) ≤ O(V).

Combined cost: O(V + k log k + V) = O(2V + k log k) = O(V + k log k). Since k ≤ V the formal worst-case is O(V log V), but in typical trees the DFS and the two descents dominate and k is much smaller than V, so the practical running time is Θ(V).
