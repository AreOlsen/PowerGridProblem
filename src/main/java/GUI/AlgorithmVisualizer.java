package GUI;

import graph.Edge;
import graph.WeightedGraph;
import java.util.List;
import org.graphstream.graph.Graph;
import student.IProblem;
import student.ProblemSolver;

public class AlgorithmVisualizer<V, E extends Comparable<E>> {
  private final IProblem problemSolver = new ProblemSolver();

  public boolean mst(Graph visualGraph, WeightedGraph<V, E> graph) {
    List<Edge<V>> edgeList = problemSolver.mst(graph);

    if (edgeList != null) {
      for (Edge<V> edge : edgeList) {
        String aId = edge.a.toString();
        String bId = edge.b.toString();

        // The edge is non-directional, and could therefor be swapped.
        if (visualGraph.getEdge(aId + " -- " + bId) == null) {
          aId = edge.b.toString();
          bId = edge.a.toString();
        }

        visualGraph
            .getEdge(aId + " -- " + bId)
            .setAttribute(
                "ui.style",
                "fill-color: blue; text-offset: 10px, 10px; text-size: 15px; text-color: blue;");
        visualGraph
                .getNode(aId)
                .setAttribute("ui.style", "text-size: 15px; size: 25px; fill-color: blue;");
        visualGraph
                .getNode(bId)
                .setAttribute("ui.style", "text-size: 15px; size: 25px; fill-color: blue;");
      }
      System.out.println("MST applied.");
      return true;
    } else {
      System.out.println("MST returned null.");
      return false;
    }
  }

  public boolean lca(Graph visualGraph, graph.Graph<V> graph, Object a, Object b) {
    V u = null;
    V v = null;
    for (V node : graph.vertices()) {
      if (node.toString().equals(a)) {
        u = node;
      }
      if (node.toString().equals(b)) {
        v = node;
      }
    }
    if (u == null || v == null) {
      throw new IllegalArgumentException("Could not find the nodes specified.");
    }

    visualGraph
        .getNode(u.toString())
        .setAttribute(
            "ui.style", "fill-color: orange; text-size: 15px; size: 25px; text-color: black;");
    visualGraph
        .getNode(v.toString())
        .setAttribute(
            "ui.style", "fill-color: orange; text-size: 15px; size: 25px; text-color: black;");

    V node = problemSolver.lca(graph, graph.getFirstNode(), u, v);
    if (node != null) {
      visualGraph
          .getNode(node.toString())
          .setAttribute(
              "ui.style", "fill-color: blue; text-size: 15px; size: 25px; text-color: white;");
      System.out.println("LCA applied.");
      return true;
    } else {
      System.out.println("LCA returned null.");
      return false;
    }
  }

  public String addRedundant(Graph visualGraph, graph.Graph<V> graph) {
    String redundantEdge = "";
    Edge<V> edge = problemSolver.addRedundant(graph, graph.getFirstNode());

    if (edge != null) {

      String aId = edge.a.toString();
      String bId = edge.b.toString();

      redundantEdge = edge.toString();
      visualGraph.addEdge(redundantEdge, aId, bId).setAttribute("ui.style", "fill-color: blue;");
      visualGraph
          .getNode(aId)
          .setAttribute("ui.style", "text-size: 15px; size: 25px; fill-color: blue;");
      visualGraph
          .getNode(bId)
          .setAttribute("ui.style", "text-size: 15px; size: 25px; fill-color: blue;");
      System.out.println("Add Redundant applied.");
    } else {
      System.out.println("Add Redundant returned null.");
    }
    return redundantEdge;
  }
}
