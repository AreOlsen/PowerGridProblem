package student;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import graph.Edge;
import graph.Graph;

class ProblemSolverSimpleTest {

	Graph<String> tree;
	IProblem solver;
	
	@BeforeEach
	void setUp() throws Exception {
		makeSampleTree();
		solver = new ProblemSolver();
	}

	/**
	 *       f - e - g
	 *      /    
	 * a - b   h  
	 *      \ /  
	 *       c - d 
	 */
	void makeSampleTree() {
		tree = new Graph<String>();
		tree.addVertex("a");
		tree.addVertex("b");
		tree.addVertex("c");
		tree.addVertex("d");
		tree.addVertex("e");
		tree.addVertex("f");
		tree.addVertex("g");
		tree.addVertex("h");
		tree.addEdge("a", "b");
		tree.addEdge("b", "c");
		tree.addEdge("b", "f");
		tree.addEdge("c", "d");
		tree.addEdge("c", "h");
		tree.addEdge("e", "f");
		tree.addEdge("e", "g");
	}

	@Test
	void testRedundantEdge() {
		Edge<String> e = solver.addRedundant(tree, "a");
		PowerOutage<String> po = new PowerOutage<String>();
		int outageSize = po.largestPowerBreak(tree, e,"a");
		assertEquals(3, outageSize);

		e = solver.addRedundant(tree, "b");
		outageSize = po.largestPowerBreak(tree,e, "b");
		assertEquals(1, outageSize);
	}
	
	@Test 
	void testLCA(){
		assertEquals("b", solver.lca(tree, "a", "d", "e"));
		assertEquals("b", solver.lca(tree, "a", "b", "g"));
		assertEquals("c", solver.lca(tree, "a", "d", "h"));
	}
}
