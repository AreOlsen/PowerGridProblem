package GUI;

import java.util.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/** A layout to display a tree graph. Positions the node in relation to the root.
 * */
public class TreeLayout {
  private final Graph graph;
  private final Map<Node, Node> childToParentMap = new HashMap<>();
  private final Map<Node, List<Node>> parentToChildrenMap = new HashMap<>();
  private final Double BREADTH = 4.0;

  public TreeLayout(Graph graph) {
    this.graph = graph;
    mapChildrenAndParents(graph);
  }

  /**
   * Positions the nodes in the graph to neatly form a tree shape.
   */
  public void displayGraphAsTree() {
    findNodeYCoordinates(graph);
    findNodeXCoordinates(graph);
  }

  private void mapChildrenAndParents(Graph graph) {
    HashSet<Node> found = new HashSet<>();
    graph.nodes().forEach(node -> {
        found.add(node);
        node.neighborNodes().forEach(neighbour -> {
            if (!found.contains(neighbour)) {
              childToParentMap.put(neighbour, node);
              List<Node> children = parentToChildrenMap.getOrDefault(node, new ArrayList<>());
              children.add(neighbour);
              parentToChildrenMap.put(node, children);
            }
        });
    });
  }

  /**
   * Finds and attributes the y-coordinate to each node in a given graph.
   * The y-coordinate is calculated by finding the depth of the node in the graph.
   * @param graph for which to place the nodes
   */
  public void findNodeYCoordinates(Graph graph) {
    graph.nodes().forEach(node -> {
      node.setAttribute("y", findDepth(graph, node)*-1);
    });
  }

  public int findDepth(Graph graph, Node node) {
    Node root = graph.getNode(0);
    Node next = node;
    int y = 0;

    while (!next.equals(root)) {
      next = childToParentMap.get(next);
      y += 1;
    }

    return y;
  }

  /**
   * Finds and attributes the x-coordinate to each node in a given graph.
   * The x-coordinate is calculated by taking the space associated with the parent node (the range), and dividing it
   * by the children. Each node is placed in the middle of its associated space.
   * @param graph for which to place the nodes
   */
  public void findNodeXCoordinates(Graph graph) {
    Map<Node, Map<String, Double>> rangeMap = new HashMap<>();
    double displacement = BREADTH/2;
    rangeMap.put(graph.getNode(0), Map.of("start", 0-displacement, "stop", BREADTH-displacement));
    graph.getNode(0).setAttribute("x", 0);
    graph
        .nodes()
        .forEach(
            node -> {
              List<Node> children = parentToChildrenMap.get(node);
              if (children != null) {
                int numChildren = children.size();
                double parentStart = rangeMap.get(node).get("start");
                double parentStop = rangeMap.get(node).get("stop");
                double parentRange = parentStop - parentStart;
                double rangePerChild = parentRange / numChildren;
                for (int i = 0; i < children.size(); i++) {
                  double start = parentStart + rangePerChild * i;
                  double stop = parentStart + rangePerChild * (i + 1);
                  rangeMap.put(children.get(i), Map.of("start", start, "stop", stop));
                  children.get(i).setAttribute("x", start + rangePerChild / 2);
                }
              }
            });
  }
}
