package graph;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EdgeTest {

	@Test
	void testEdge() {
		Edge<String> e = new Edge<String>("a", "b");
		assertEquals("a", e.a);
		assertEquals("b", e.b);
	}

	@Test
	void testEqualsSame() {
		Edge<String> e1 = new Edge<String>("a", "b");
		Edge<String> e2 = new Edge<String>("a", "b");
		Edge<String> e3 = new Edge<String>("a", "c");
		assertEquals(e1, e2);
		
		assertNotEquals(e1, e3);
	}

	@Test
	void testEqualsReversed() {
		Edge<String> e1 = new Edge<String>("a", "b");
		Edge<String> e2 = new Edge<String>("b", "a");
		assertEquals(e1, e2);
	}

	@Test
	void testConstructNull() {
		
		try {
			@SuppressWarnings("unused")
			Edge<String> e = new Edge<String>("b", null);
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("No Exception caught");
	}

	@Test
	void testConstructLoop(){
		try {
			@SuppressWarnings("unused")
			Edge<String> e = new Edge<String>("a", "a");
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("No Exception caught");		
	}
}
