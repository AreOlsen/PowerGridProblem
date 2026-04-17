package GUI;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreeLayoutTest {
    protected org.graphstream.graph.Graph graph;
    TreeLayout graphLayout;
    @BeforeEach
    void init() {
        graph = new SingleGraph("Test");
        for(int i = 0; i < 12; i++) {
            graph.addNode("" + i);
        }
        graph.addEdge("0-1", 0, 1);
        graph.addEdge("0-2", 0, 2);
        graph.addEdge("1-3", 1, 3);
        graph.addEdge("1-4", 1, 4);
        graph.addEdge("2-5",2, 5);
        graph.addEdge("2-6", 2, 6);
        graph.addEdge("3-7", 3, 7);
        graph.addEdge("4-8", 4, 8);
        graph.addEdge("4-9", 4, 9);
        graph.addEdge("5-10", 5, 10);
        graph.addEdge("10-11", 10, 11);



        graphLayout = new TreeLayout(graph);
    }
    @Test
    void findsDepthCorrectly0() {
        int expected = 0;
        int actual = graphLayout.findDepth(graph, graph.getNode(0));

        assertEquals(expected, actual);
    }

    @Test
    void findsDepthCorrectly1() {
        int expected = 1;
        int actual1 = graphLayout.findDepth(graph, graph.getNode(1));
        int actual2 = graphLayout.findDepth(graph, graph.getNode(2));

        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
    }
    
    @Test
    void findsDepthCorrectly2() {
        int expected = 2;
        int actual1 = graphLayout.findDepth(graph, graph.getNode(3));
        int actual2 = graphLayout.findDepth(graph, graph.getNode(4));
        int actual3 = graphLayout.findDepth(graph, graph.getNode(5));
        int actual4 = graphLayout.findDepth(graph, graph.getNode(6));

        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
        assertEquals(expected, actual3);
        assertEquals(expected, actual4);
    }

    @Test
    void findsDepthCorrectly3() {
        int expected = 3;
        int actual1 = graphLayout.findDepth(graph, graph.getNode(7));
        int actual2 = graphLayout.findDepth(graph, graph.getNode(8));
        int actual3 = graphLayout.findDepth(graph, graph.getNode(9));
        int actual4 = graphLayout.findDepth(graph, graph.getNode(10));

        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
        assertEquals(expected, actual3);
        assertEquals(expected, actual4);
    }

    @Test
    void findsDepthCorrectly4() {
        int expected = 4;
        int actual = graphLayout.findDepth(graph, graph.getNode(11));

        assertEquals(expected, actual);
    }

    @Test
    void placesXCoordinateCorrectly() {
        //Run the program
        //Make sure the tree view is active
        //Check that it looks nice with even space between nodes
    }

}
