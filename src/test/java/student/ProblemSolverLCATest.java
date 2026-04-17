package student;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import graph.*;

class ProblemSolverLCATest {

	IProblem problemSolver;
	Graph<Integer> g;
	ArrayList<Edge<Integer>> queries;
	ArrayList<Integer> answers;

	@BeforeEach
	void getProblemSolver() {
		problemSolver = new ProblemSolver();
	}

	private void generateTestCase(int i) throws IOException {
		queries = new ArrayList<Edge<Integer>>();
		g = new GraphBuilder().readLCAInstance("input/LCA" + i + ".in", queries);
		answers = new ArrayList<Integer>();
		Scanner sc = new Scanner(new FileReader(new File("input/LCA" + i + ".ans")));
		sc.useLocale(Locale.US);
		for (int a = 0; a < queries.size(); a++)
			answers.add(sc.nextInt());
	}

	@Test
	void test1() throws IOException {
		generateTestCase(0);
		for (int i = 0; i < queries.size(); i++) {
			int ans = problemSolver.lca(g, 0, queries.get(i).a, queries.get(i).b);
			assertEquals(answers.get(i), ans);
		}
	}

	@Test
	void test2() throws IOException {
		generateTestCase(1);
		for (int i = 0; i < queries.size(); i++) {
			int ans = problemSolver.lca(g, 0, queries.get(i).a, queries.get(i).b);
			assertEquals(answers.get(i), ans);
		}
	}

	@Test
	void test3() throws IOException {
		generateTestCase(2);
		for (int i = 0; i < queries.size(); i++) {
			int ans = problemSolver.lca(g, 0, queries.get(i).a, queries.get(i).b);
			assertEquals(answers.get(i), ans);
		}
	}

	@Test
	void test4() throws IOException {
		generateTestCase(3);
		for (int i = 0; i < queries.size(); i++) {
			int ans = problemSolver.lca(g, 0, queries.get(i).a, queries.get(i).b);
			assertEquals(answers.get(i), ans);
		}
	}

	@Test
	void test5() throws IOException {
		generateTestCase(4);
		for (int i = 0; i < queries.size(); i++) {
			int ans = problemSolver.lca(g, 0, queries.get(i).a, queries.get(i).b);
			assertEquals(answers.get(i), ans);
		}
	}
}
