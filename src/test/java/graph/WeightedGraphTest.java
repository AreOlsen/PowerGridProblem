package graph;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeightedGraphTest {

	private WeightedGraph<Integer, Double> graph;

	@BeforeEach
	void setup() {
		graph = new WeightedGraph<>();
		addvertices(10);
	}

	void addvertices(int n) {
		for (int i = 0; i < n; i++)
			graph.addVertex(i);
	}

	@Test
	void canAddAndRemoveVerticesTest() {
		assertTrue(graph.hasVertex(3));
		graph.removeVertex(3);
		assertFalse(graph.hasVertex(3));
	}

	@Test
	void canAddAndRemoveEdges() {
		assertTrue(graph.addEdge(2, 5, 7.3));
		assertTrue(graph.adjacent(2, 5));
		assertTrue(graph.adjacent(5, 2));

		assertTrue(graph.addEdge(2, 6, 2.3));

		assertTrue(graph.removeEdge(5, 2));
		assertFalse(graph.adjacent(2, 5));
		assertFalse(graph.adjacent(5, 2));
	}

	@Test
	void canAddAndRemoveEdges2() {
		Edge<Integer> e = new Edge<Integer>(2, 5);
		assertTrue(graph.addEdge(e, 7.3));
		assertTrue(graph.adjacent(2, 5));
		assertTrue(graph.adjacent(5, 2));

		assertTrue(graph.addEdge(new Edge<Integer>(2, 6), 2.3));

		assertTrue(graph.removeEdge(e));
		assertFalse(graph.adjacent(2, 5));
		assertFalse(graph.adjacent(5, 2));
	}

	@Test
	void canSetWeight() {
		int a = 2, b = 5;
		assertTrue(graph.addEdge(a, b, 7.3));
		assertEquals(7.3, graph.getWeight(a, b));
		assertEquals(7.3, graph.getWeight(b, a));
		graph.setWeight(b, a, 3.6);
		assertEquals(3.6, graph.getWeight(a, b));
		assertEquals(3.6, graph.getWeight(b, a));
	}

	@Test
	void canNotAddLoop() {
		int a = 2, b = 5;
		assertTrue(graph.addEdge(a, b, 7.3));
		assertFalse(graph.addEdge(a, a,7.3));
		assertFalse(graph.addEdge(b, b,7.3));
		assertThrows(IllegalArgumentException.class,()->graph.setWeight(a, a,7.3));
	}
	
	@Test
	void setWeightOnLoop() {
		int a = 4, b = 7;
		assertTrue(graph.adjacent(a, a));
		assertThrows(IllegalArgumentException.class, () -> graph.setWeight(a, a, 7.3));
	}
	
	@Test
	void testRemoveLoop() {
		int a = 4;
		assertFalse(graph.removeEdge(a, a));
	}
}
