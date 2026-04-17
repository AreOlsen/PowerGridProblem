package graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdjacencySetTest extends GraphTest2 {
    
    @BeforeEach
    public void setup() {
        graph = new Graph<Integer>();
        createEmptyGraph();
        //createConnectedGraph();
    }
}
