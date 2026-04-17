import GUI.GraphVisualizer;
import graph.Graph;
import graph.GraphBuilder;
import graph.WeightedGraph;
import student.ProblemSolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
    static GraphBuilder graphBuilder = new GraphBuilder();
    static int numberOfMSTExamples = 3; // Change this if you add more examples
    static int numberOfLCAExamples = 2; // Change this if you add more examples
    static int numberOfAddRedundantExamples = 2; // Change this if you add more examples

    public static void main(String[] args) throws IOException {
        new ProblemSolver();
        new GraphVisualizer<Integer, Integer>(readMSTExamplesFromFile(), readLCAExamplesFromFile(), readAddRedundantExamplesFromFile());
    }

    /**
     * Reads in all MST examples from the input/visualization folder.
     * @return a list of {@link graph.WeightedGraph<>}s that serve as examples for the MST algorithm.
     * @throws IOException if file is not found
     */
    public static List<WeightedGraph<Integer, Integer>> readMSTExamplesFromFile() throws IOException {
        List<WeightedGraph<Integer, Integer>> mstExamples = new ArrayList<>();
        for(int i = 0; i < numberOfMSTExamples; i++) {
            mstExamples.add(graphBuilder.readWeightedGraphFromFile("input/visualization/MST" + i + ".txt"));
        }
        return mstExamples;
    }

    /**
     * Reads in all LCA examples from the input/visualization folder.
     * @return a list of {@link graph.Graph<>}s that serve as examples for the LCA algorithm.
     * @throws IOException if file is not found
     */
    public static List<Graph<Integer>> readLCAExamplesFromFile() throws IOException {
        List<Graph<Integer>> lcaExamples = new ArrayList<>();
        for(int i = 0; i < numberOfLCAExamples; i++) {
            lcaExamples.add(graphBuilder.readTreeFromFile("input/visualization/LCA" + i + ".txt"));
        }
        return lcaExamples;
    }

    /**
     * Reads in all AddRedundant examples from the input/visualization folder.
     * @return a list of {@link graph.Graph<>}s that serve as examples for the AddRedundant algorithm.
     * @throws IOException if file is not found
     */
    public static List<Graph<Integer>> readAddRedundantExamplesFromFile() throws IOException {
        List<Graph<Integer>> addRedundantExamples = new ArrayList<>();
        for(int i = 0; i < numberOfAddRedundantExamples; i++) {
            addRedundantExamples.add(graphBuilder.readTreeFromFile("input/visualization/REP" + i + ".txt"));
        }
        return addRedundantExamples;
    }
}
