package graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class GraphTest {

	Graph<String> g;

	/**
	 * f - e - g / | a - b | \ | c - d
	 */
	void makeSampleGraph() {
		g.addVertex("a");
		g.addVertex("b");
		g.addVertex("c");
		g.addVertex("d");
		g.addVertex("e");
		g.addVertex("f");
		g.addVertex("g");
		g.addEdge("a", "b");
		g.addEdge("b", "c");
		g.addEdge("b", "f");
		g.addEdge("c", "d");
		g.addEdge("d", "e");
		g.addEdge("e", "f");
		g.addEdge("e", "g");
	}

	@BeforeEach
	void setUp() throws Exception {
		g = new Graph<String>();
	}

	@SuppressWarnings("unused")
	@Test
	void testGraph() {
		for (String v : g.vertices()) {
			fail("Initial graph should not have any vertices");
		}
	}

	@Test
	void testAdjacent() {
		makeSampleGraph();
		assertTrue(g.adjacent("a", "b"));
		assertTrue(g.adjacent("d", "c"));
		assertTrue(g.adjacent("e", "g"));
		assertFalse(g.adjacent("a", "c"));
		assertFalse(g.adjacent("f", "d"));
		assertFalse(g.adjacent("g", "d"));
	}

	@Test
	void testNeighbours() {
		makeSampleGraph();
		HashSet<String> found = new HashSet<String>();
		for (String s : g.neighbours("b")) {
			found.add(s);
		}
		HashSet<String> expected = new HashSet<String>();
		expected.add("a");
		expected.add("c");
		expected.add("f");
		assertEquals(expected, found);
	}

	@Test
	void testDegree() {
		makeSampleGraph();
		assertEquals(1, g.degree("g"));
		assertEquals(2, g.degree("f"));
		assertEquals(3, g.degree("b"));
	}

	@Test
	void testAddVertex() {
		HashSet<String> set = new HashSet<String>();
		set.add("a");
		set.add("b");
		for (String v : set) {
			assertTrue(g.addVertex(v), "Should return true when adding a new vertex");
		}

		HashSet<String> found = new HashSet<String>();
		for (String v : g.vertices()) {
			found.add(v);
		}

		assertTrue(set.containsAll(found), "Graph contains node that was not added.");
		assertTrue(found.containsAll(set), "Graph is missing node that was added.");

		assertFalse(g.addVertex("a"), "Should returne false when trying to add an existing vertex");
	}

	@Test
	void testAddOneEdge() {
		HashSet<String> nodes = new HashSet<String>();
		nodes.add("s");
		nodes.add("t");

		for (String v : nodes) {
			g.addVertex(v);
		}
		g.addEdge("s", "t");

		for (Edge<String> e : g.edges()) {
			assertTrue(nodes.contains(e.a));
			assertTrue(nodes.contains(e.b));
			for (String v : nodes) {
				assertTrue(v.equals(e.a) || v.equals(e.b));
				assertTrue(v.equals(e.a) || v.equals(e.b));
			}
		}
	}

	@Test
	void testEdgeCount() {
		g.addVertex("s");
		g.addVertex("t");
		g.addEdge("s", "t");
		int edgeCount = 0;
		for (Edge<String> e : g.edges()) {
			if (e == null)
				fail("Edges should not be null");
			else
				edgeCount++;
		}
		assertEquals(1, edgeCount);
	}

	@Test
	void testRemoveEdge() {
		makeSampleGraph();
		assertTrue(g.removeEdge("c", "d"));
		assertFalse(g.adjacent("c", "d"));
		assertFalse(g.adjacent("d", "s"));
		assertFalse(g.removeEdge("c", "d"));

		assertFalse(g.removeEdge("c", "h"));

	}

	@Test
	void protectedIterable() {
		makeSampleGraph();
		Iterable<String> iter = g.vertices();
		try {
			HashSet<String> set = (HashSet<String>) iter;
			set.remove("a");
		} catch (Exception e) {
			// This should happen
			return;
		}
		fail("We were able to delete a vertex from the Iterable");
	}

	@Test
	void protectedIterator() {
		makeSampleGraph();
		Iterator<String> iterator = g.vertices().iterator();
		String first = iterator.next();

		try {
			iterator.remove();
		} catch (Exception e) {

			return;
		}
		iterator = g.vertices().iterator();
		assertEquals(first, iterator.next());
	}

	void testNum() {
		makeSampleGraph();
		assertEquals(7, g.numVertices());
		assertEquals(7, g.numEdges());
	}

	@Test
	void testIteratePairs() {
		makeSampleGraph();
		int numPairs = 0;
		int numEdges = 0;
		for (String a : g.vertices()) {
			for (String b : g.vertices()) {
				numPairs++;
				if (!a.equals(b) && g.adjacent(a, b))
					numEdges++;
			}
		}
		assertEquals(49, numPairs);
		assertEquals(2 * g.numEdges(), numEdges);
	}

	@Test
	void testAddLoop() {
		g.addVertex("a");
		assertFalse(g.addEdge("a", "a"),"Trying to add a loop to the graph should give IllegalArgumentException.");
		assertTrue(g.numEdges() <= 1);
	}

	@Test
	void testAddExistingVertex() {
		g.addVertex("a");
		g.addVertex("b");
		g.addEdge("a", "b");
		assertFalse(g.addVertex("a"));
		assertTrue(g.adjacent("a", "b"));
		assertTrue(g.adjacent("b", "a"));
		assertFalse(g.addEdge("a", "b"));
	}
	
	@Test
	void testRemoveLoop() {
		assertFalse(g.removeEdge("a", "a"));
	}
}
