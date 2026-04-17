package student;

import java.util.HashMap;
import java.util.HashSet;

import graph.Edge;
import graph.Graph;

public class PowerOutage<V> {

	private HashSet<V> visited;
	private HashMap<V, Integer> size;
	private HashSet<V> onCycle;

	public int largestPowerBreak(Graph<V> g, Edge<V> newEdge, V root) {
		// compute sizes of subtrees
		visited = new HashSet<>();
		size = new HashMap<>();
		size(g, root);

		// find the vertices on the cycle
		visited = new HashSet<>();
		onCycle = new HashSet<>();
		g.addEdge(newEdge);
		findCycle(g, root, null);
		g.removeEdge(newEdge);

		int max = 0;
		for (Edge<V> edge : g.edges()) {
			if (onCycle.contains(edge.a) && onCycle.contains(edge.b))
				continue;
			max = Math.max(max, Math.min(size.get(edge.a), size.get(edge.b)));
		}
		return max;
	}

	private int size(Graph<V> g, V node) {
		if (visited.contains(node))
			return 0;
		visited.add(node);
		int sz = 1;
		for (V child : g.neighbours(node))
			sz += size(g, child);
		size.put(node, sz);
		return sz;
	}

	private CycleReturn findCycle(Graph<V> g, V node, V parent) {
		if (visited.contains(node)) {
			onCycle.add(node);
			return CycleReturn.CYCLEACTIVE;
		}
		visited.add(node);
		for (V child : g.neighbours(node)) {
			if (child.equals(parent))
				continue;
			CycleReturn cr = findCycle(g, child, node);
			if (cr == CycleReturn.NOCYCLE)
				continue;
			if (cr == CycleReturn.CYCLECOMPLETED)
				return cr;
			if (onCycle.contains(node))
				return CycleReturn.CYCLECOMPLETED;
			onCycle.add(node);
			return cr;
		}

		return CycleReturn.NOCYCLE;
	}

	enum CycleReturn {
		NOCYCLE, CYCLEACTIVE, CYCLECOMPLETED
	}
}