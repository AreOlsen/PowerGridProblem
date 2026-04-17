package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

public abstract class GraphTest2 {

    final int N_NODES = 1000;
    Random random = new Random();
    Graph<Integer> graph;

    /**
     * Adds edges of all nodes from 0 to N, i.e.:
     * (0 -> 1), (1 -> 2), ..., (N-1 -> N)
     * 
     * @param graph
     */
    public void createEmptyGraph() {
        for (int i = 0; i < N_NODES; i++) {
        	graph.addVertex(i);
        }
    }
    
    /**
     * Adds edges of all nodes from 0 to N, i.e.:
     * (0 -> 1), (1 -> 2), ..., (N-1 -> N)
     * 
     * @param graph
     */
    public void createConnectedGraph() {
    	graph.addVertex(0);
        for (int i = 0; i < N_NODES - 1; i++) {
        	graph.addVertex(i+1);
            graph.addEdge(i, i + 1);
        }
    }

    @Test
    public void addEdgeTest() {
        for (int i = 0; i < N_NODES; i++) {
            int u = random.nextInt(N_NODES);
            int v = random.nextInt(N_NODES);
            if(u==v)
            	continue;
            graph.addEdge(u, v);
            assertTrue(graph.adjacent(u, v), "Nodes " + u + " and " + v + " should have an edge between them.");
            assertTrue(graph.adjacent(v, u), "In an undirected graph " + u + " and " + v + " should have an edge both ways.");
        }
    }

    @Test
    public void hasEdgeSameAsNeighbourhood() {
        for (int i = 0; i < N_NODES; i++) {
            int u = random.nextInt(N_NODES);
            int v = random.nextInt(N_NODES);
            if(u==v)
            	continue;
            graph.addEdge(u, v);
            checkIsEdge(u,v);
        }
    }
    
    private void checkIsEdge(int u, int v) {
    	boolean isEdge = graph.adjacent(u, v);
    	assertEquals(isEdge,graph.adjacent(v, u),"hasEdge(u,v) should be equal to hasEdge(v,u)");
		assertEquals(isEdge, graph.neighbours(u).contains(v),"for hasEdge("+u+","+v+") = "+isEdge+" but "+v+" is not in the neighbourhood of "+u);
		assertEquals(isEdge, graph.neighbours(v).contains(u),"for hasEdge("+u+","+v+") = "+isEdge+" but "+u+" is not in the neighbourhood of "+v);
	}

	@Test
    public void removeEdgeTest() {
        for (int i = 0; i < N_NODES; i++) {
            int u = random.nextInt(N_NODES);
            int v = random.nextInt(N_NODES);
            if(u==v)
            	continue;
            graph.addEdge(u, v);
            assertTrue(graph.adjacent(u, v), "Nodes " + u + " and " + v + " should have an edge between them.");
            assertTrue(graph.adjacent(v, u), "In an undirected graph " + u + " and " + v + " should have an edge both ways.");
            graph.removeEdge(u, v);
            assertFalse(graph.adjacent(u, v), "Nodes " + u + " and " + v + " should not have an edge between them.");
            assertFalse(graph.adjacent(v, u), "Nodes " + v + " and " + u + " should not have an edge between them.");
        }
    }

    @Test
    public void getNeighbourhoodTest() {
        int u = random.nextInt(N_NODES);
        Set<Integer> randomNodes = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            int v = random.nextInt(N_NODES);
            if(v!=u) {
            	graph.addEdge(u, v);
            	randomNodes.add(v);
            }
        }

        for (Integer w : graph.neighbours(u)) {
            if (!randomNodes.contains(w))
                fail("A neighbour of u was not in the list of neigbhours");
        }
        for (Integer w : graph.neighbours(u)) {
            if (!randomNodes.contains(w))
                fail("A neighbour of u was twice in the list of neigbhours");
            else
            	randomNodes.remove(w);
        }
    }

    @Test
    public void addNodeTest() {
        int extraNode = N_NODES + 1;
        assertEquals(N_NODES, graph.size(), "Graph should have " + N_NODES + " nodes.");
        assertFalse(graph.hasVertex(extraNode), "Graph should not have node " + extraNode);
        int u = extraNode;
        graph.addVertex(u);
        assertTrue(graph.hasVertex(u), "Graph should have node " + u);
        assertEquals(extraNode, graph.size(), "Graph should have " + extraNode + " nodes.");
    }

    @Test
    public void removeNodeTest() {
        graph.removeVertex(0);
        assertFalse(graph.hasVertex(0), "Graph should not have node " + 0);
        assertEquals(N_NODES-1, graph.size(), "Graph should have " + N_NODES + " nodes.");
    }

    @Test
    public void addEdgeToNodeNotInGraph() {
        int u = N_NODES + 1;
        int v = random.nextInt(N_NODES);
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(u, v),
                "You can't add an edge to a node that doesn't exist on the graph.");
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(v, u),
                "You can't add an edge to a node that doesn't exist on the graph.");
        assertFalse(graph.adjacent(u, v));
        assertFalse(graph.adjacent(v, u));
    }

    @Test
    public void duplicateNodeTest() {
        int node = N_NODES - 2;
        graph.addEdge(node, node + 1);
        graph.addVertex(node);
        assertTrue(graph.adjacent(node, node + 1),
                "Trying to add a duplicate node should not remove the egdes from the original node");
    }

    @Test
    public void removeEgdesWithNodeTest() {
    	//select 3 different indices
        int u = random.nextInt(N_NODES);
        int v = random.nextInt(N_NODES-1);
        if(v>=u)
        	v++;
        int z = random.nextInt(N_NODES-2);
        if(z<=Math.min(u, v))
        	z++;
        if(z<=Math.max(u, v))
        	z++;
        assertTrue(graph.addEdge(u, v));
        assertTrue(graph.addEdge(u, z));
        int numEdges = graph.numEdges();
        assertTrue(graph.neighbours(u).size() == 2);
        assertTrue(graph.removeVertex(u));
        assertThrows(IllegalArgumentException.class, () -> graph.neighbours(u),
                "Should not be able to get neighbours from removed node");
        assertEquals(numEdges-2, graph.numEdges());
        assertFalse(graph.adjacent(u, v));
        assertFalse(graph.adjacent(v, u));
        assertFalse(graph.adjacent(u, z));
        assertFalse(graph.adjacent(z, u));
    }
}
