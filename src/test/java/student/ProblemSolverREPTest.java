package student;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import graph.*;

class ProblemSolverREPTest {

	IProblem problemSolver;
	Graph<Integer> g;
	Edge<Integer> answer;

	@BeforeEach
	void getProblemSolver() {
		problemSolver = new ProblemSolver();
	}

	private void generateTestCase(int i) throws IOException {
		g = new GraphBuilder().readTreeFromFile("input/REP" + i + ".in");
		Scanner sc = new Scanner(new FileReader(new File("input/REP" + i + ".ans")));
		sc.useLocale(Locale.US);
		answer = new Edge<Integer>(sc.nextInt(), sc.nextInt());
		sc.close();
	}

	@Test
	void test1() throws IOException {
		generateTestCase(0);
		assertEquals(new PowerOutage<Integer>().largestPowerBreak(g, answer, 0),
				new PowerOutage<Integer>().largestPowerBreak(g, problemSolver.addRedundant(g, 0), 0));
	}

	@Test
	void test2() throws IOException {
		generateTestCase(1);
		assertEquals(new PowerOutage<Integer>().largestPowerBreak(g, answer, 0),
				new PowerOutage<Integer>().largestPowerBreak(g, problemSolver.addRedundant(g, 0), 0));
	}

	@Test
	void test3() throws IOException {
		generateTestCase(2);
		assertEquals(new PowerOutage<Integer>().largestPowerBreak(g, answer, 0),
				new PowerOutage<Integer>().largestPowerBreak(g, problemSolver.addRedundant(g, 0), 0));
	}

	@Test
	void test4() throws IOException {
		generateTestCase(3);
		assertEquals(new PowerOutage<Integer>().largestPowerBreak(g, answer, 0),
				new PowerOutage<Integer>().largestPowerBreak(g, problemSolver.addRedundant(g, 0), 0));
	}

	@Test
	void test5() throws IOException {
		generateTestCase(4);
		assertEquals(new PowerOutage<Integer>().largestPowerBreak(g, answer, 0),
				new PowerOutage<Integer>().largestPowerBreak(g, problemSolver.addRedundant(g, 0), 0));
	}
}
