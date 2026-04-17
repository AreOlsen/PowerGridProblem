package student;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import graph.*;

class ProblemSolverMSTTest {
	private WeightedGraph<Integer, Integer> g;
	private int mstWeight;
	private IProblem problemSolver;

	@BeforeEach
	void getProblemSolver() {
		problemSolver = new ProblemSolver();
	}

	private void generateTestCase(int i) throws IOException {
		g = new GraphBuilder().readWeightedGraphFromFile("input/MST" + i + ".in");
		Scanner sc = new Scanner(new FileReader(new File("input/MST" + i + ".ans")));
		sc.useLocale(Locale.US);
		mstWeight = sc.nextInt();
	}

	private boolean isConnected(List<Edge<Integer>> edges) {
		Graph<Integer> tmp = new Graph<Integer>();
		for (Integer vertex : g.vertices())
			tmp.addVertex(vertex);
		for (Edge<Integer> edge : edges)
			tmp.addEdge(edge.a, edge.b);
		HashSet<Integer> visited = new HashSet<>();
		Queue<Integer> queue = new LinkedList<>();
		queue.add(0);
		while (!queue.isEmpty()) {
			Integer u = queue.poll();
			if (visited.contains(u))
				continue;
			visited.add(u);
			for (Integer v : tmp.neighbours(u)) {
				queue.add(v);
			}
		}
		for (Integer u : g.vertices()) {
			if (!visited.contains(u)) {
				return false;
			}
		}
		return true;
	}

	private int weightOfTree(List<Edge<Integer>> edges) {
		int sum = 0;
		for (Edge<Integer> edge : edges) {
			sum += g.getWeight(edge.a, edge.b);
		}
		return sum;
	}

	@Test
	void test1() throws IOException {
		generateTestCase(0);
		List<Edge<Integer>> edges = problemSolver.mst(g);
		assertTrue(isConnected(edges), "Your Tree is not connected");
		assertEquals(mstWeight, weightOfTree(edges), "Your tree is too heavy");
	}

	@Test
	void test2() throws IOException {
		generateTestCase(1);
		List<Edge<Integer>> edges = problemSolver.mst(g);
		assertTrue(isConnected(edges), "Your Tree is not connected");
		assertEquals(mstWeight, weightOfTree(edges), "Your tree is too heavy");
	}

	@Test
	void test3() throws IOException {
		generateTestCase(2);
		List<Edge<Integer>> edges = problemSolver.mst(g);
		assertTrue(isConnected(edges), "Your Tree is not connected");
		assertEquals(mstWeight, weightOfTree(edges), "Your tree is too heavy");
	}

	@Test
	void test4() throws IOException {
		generateTestCase(3);
		List<Edge<Integer>> edges = problemSolver.mst(g);
		assertTrue(isConnected(edges), "Your Tree is not connected");
		assertEquals(mstWeight, weightOfTree(edges), "Your tree is too heavy");
	}

	@Test
	void test5() throws IOException {
		generateTestCase(4);
		List<Edge<Integer>> edges = problemSolver.mst(g);
		assertTrue(isConnected(edges), "Your Tree is not connected");
		assertEquals(mstWeight, weightOfTree(edges), "Your tree is too heavy");
	}
}