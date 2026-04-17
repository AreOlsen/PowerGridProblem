package GUI;

import graph.Edge;
import graph.WeightedGraph;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.Viewer;
import student.ProblemSolver;

public class GraphVisualizer<V, E extends Comparable<E>> extends JFrame {
  public static final String TITLE = "INF102 Power Grid";

  private final List<Graph> mstDisplayExamples = new ArrayList<>();
  private final List<WeightedGraph<V, E>> mstExamples;
  private final List<Graph> lcaDisplayExamples = new ArrayList<>();

  private final List<graph.Graph<V>> lcaExamples;
  private final JPanel lcaChoicesPanel = new JPanel();
  private JComboBox<String> lcaA;
  private JComboBox<String> lcaB;
  private final List<Graph> addRedundantDisplayExamples = new ArrayList<>();

  private final List<graph.Graph<V>> addRedundantExamples;
  private int task = 1;
  private int exampleNumber = 0;
  private Viewer viewer;
  private Graph currentGraph;
  private final JPanel exampleButtonsPanel = new JPanel();
  private JButton runAlgorithmButton;
  private JButton resetAlgorithmButton;
  private boolean run = false;
  private boolean removeRedundantEdge = false;
  private String redundantEdge;
  private final JLabel currentTaskLabel = new JLabel();
  private final JLabel currentExampleLabel = new JLabel();
  private final AlgorithmVisualizer<V, E> algorithmVisualizer = new AlgorithmVisualizer<>();
  private final JTextArea statusMessage = new JTextArea();

  /**
   * Creates a visualization of the given {@link graph.Graph} examples and gives
   * the user the
   * possibility to apply {@link ProblemSolver#mst(WeightedGraph) mst}, {@link
   * ProblemSolver#lca(graph.Graph, Object, Object, Object) lca}, and {@link
   * ProblemSolver#addRedundant(graph.Graph, Object) addRedundant} as implemented
   * in {@link
   * ProblemSolver} to the examples in each category.
   *
   * @param mstExamples          a list of {@link WeightedGraph WeightedGraphs} as
   *                             examples to apply mst to
   * @param lcaExamples          a list of {@link graph.Graph Graphs} as examples
   *                             to apply lca to
   * @param addRedundantExamples a list of {@link graph.Graph Graphs} as examples
   *                             to apply
   *                             addRedundant to
   */
  public GraphVisualizer(
      List<WeightedGraph<V, E>> mstExamples,
      List<graph.Graph<V>> lcaExamples,
      List<graph.Graph<V>> addRedundantExamples) {

    this.mstExamples = mstExamples;
    this.lcaExamples = lcaExamples;
    this.addRedundantExamples = addRedundantExamples;

    setTitle(TITLE);
    setSize(1000, 800);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    System.setProperty("org.graphstream.ui", "swing");
    convertGraphsToGraphStreams(mstExamples, lcaExamples, addRedundantExamples);

    addPanelsForTasksAndExplanation();
    addExampleChoosingPanel();
    addPanelForRunningAndResettingAlgorithm();
    updateDisplay();

    add(lcaChoicesPanel, BorderLayout.EAST);
    updatePanelForChoosingNodesLca();
    lcaChoicesPanel.setVisible(false);
    add(exampleButtonsPanel, BorderLayout.NORTH);

    setVisible(true);
  }

  private void convertGraphsToGraphStreams(
      List<WeightedGraph<V, E>> mstExamples,
      List<graph.Graph<V>> lcaExamples,
      List<graph.Graph<V>> addRedundantExamples) {
    Graph g;
    for (int i = 0; i < mstExamples.size(); i++) {
      g = convertWeightedGraph(mstExamples.get(i), "mstExample", i);
      this.mstDisplayExamples.add(g);
    }
    for (int i = 0; i < lcaExamples.size(); i++) {
      g = convertGraph(lcaExamples.get(i), "lcaExample", i);
      this.lcaDisplayExamples.add(g);
    }
    for (int i = 0; i < addRedundantExamples.size(); i++) {
      g = convertGraph(addRedundantExamples.get(i), "addRedundantExample", i);
      this.addRedundantDisplayExamples.add(g);
    }
  }

  private Graph convertWeightedGraph(WeightedGraph<V, E> graph, String exampleType, int id) {
    Graph initGraph = new SingleGraph(exampleType + id);
    initGraph.setStrict(false);
    initGraph.setAutoCreate(true);

    for (Edge<V> edge : graph.edges()) {
      String aId = edge.a.toString();
      String bId = edge.b.toString();

      initGraph
          .addEdge(aId + " -- " + bId, aId, bId, true)
          .setAttribute("ui.label", graph.getWeight(edge));
    }

    styleGraph(initGraph);

    return initGraph;
  }

  private Graph convertGraph(graph.Graph<V> graph, String exampleType, int id) {
    Graph initGraph = new SingleGraph(exampleType + id);
    initGraph.setStrict(false);
    initGraph.setAutoCreate(true);

    for (Edge<V> edge : graph.edges()) {
      String aId = edge.a.toString();
      String bId = edge.b.toString();

      initGraph.addEdge(aId + " -- " + bId, aId, bId, true);
    }

    styleGraph(initGraph);

    return initGraph;
  }

  private void styleGraph(Graph graph) {
    graph.setAttribute("ui.stylesheet", "edge { arrow-shape: none; }");

    graph
        .edges()
        .forEach(
            edge -> {
              edge.setAttribute("ui.style", "text-offset: 10px, 10px; text-size: 15px;");
            });

    graph
        .nodes()
        .forEach(
            node -> {
              node.setAttribute("ui.label", node.getId());
              node.setAttribute("ui.style", "text-size: 15px; size: 25px; text-color: white;");
            });
  }

  private void activateTreeView() {
    viewer.disableAutoLayout();
    TreeLayout treeView = new TreeLayout(currentGraph);
    treeView.displayGraphAsTree();
  }

  private void activateDefaultView() {
    viewer.enableAutoLayout();
  }

  private void addPanelsForTasksAndExplanation() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridx = 0;
    gbc.gridy = GridBagConstraints.RELATIVE;
    gbc.anchor = GridBagConstraints.WEST;

    JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    split.setTopComponent(getExplanationPanel(gbc));

    JSplitPane splitB = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    gbc.anchor = GridBagConstraints.CENTER;
    splitB.setTopComponent(getTaskChoosingPanel(gbc));
    splitB.setBottomComponent(getStatusPanel(gbc));

    split.setBottomComponent(splitB);
    add(split, BorderLayout.WEST);
  }

  private JScrollPane getStatusPanel(GridBagConstraints gbc) {
    statusMessage.setLineWrap(true);
    statusMessage.setWrapStyleWord(true);

    JScrollPane terminal = new JScrollPane(statusMessage);
    terminal.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    terminal.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    statusMessage.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        scrollToBottom();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        scrollToBottom();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        scrollToBottom();
      }

      private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
          JScrollBar verticalBar = terminal.getVerticalScrollBar();
          verticalBar.setValue(verticalBar.getMaximum());
        });
      }
    });

    statusMessage.append("Running visualization.\n");

    return terminal;
  }

  private JPanel getTaskChoosingPanel(GridBagConstraints gbc) {
    JButton mst = new JButton("MST");
    JButton lca = new JButton("LCA");
    JButton ar = new JButton("Add Redundant");

    mst.addActionListener(
        l -> {
          task = 1; // matches task number in README.md
          exampleNumber = 0;
          run = false;
          lcaChoicesPanel.setVisible(false);
          updateDisplay();
        });

    lca.addActionListener(
        l -> {
          task = 2; // matches task number in README.md
          exampleNumber = 0;
          run = false;
          lcaChoicesPanel.setVisible(true);
          updateDisplay();
        });

    ar.addActionListener(
        l -> {
          task = 3; // matches task number in README.md
          exampleNumber = 0;
          run = false;
          lcaChoicesPanel.setVisible(false);
          updateDisplay();
        });

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    panel.add(mst, gbc);
    panel.add(lca, gbc);
    panel.add(ar, gbc);
    panel.add(currentTaskLabel, gbc);
    panel.add(currentExampleLabel, gbc);
    panel.setPreferredSize(new Dimension(this.getWidth() / 4, this.getHeight() / 3));
    return panel;
  }

  private JPanel getExplanationPanel(GridBagConstraints gbc) {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());

    panel.add(new JLabel("HOW TO USE:"), gbc);
    panel.add(new JLabel("1. Choose an algorithm in the left panel."), gbc);
    panel.add(new JLabel("2. Choose an example graph in the top panel."), gbc);
    panel.add(new JLabel("(For lca, choose two nodes to apply to in right panel.)"), gbc);
    panel.add(new JLabel("3. Press \"Run Algorithm\"."), gbc);
    panel.add(new JLabel("The result is highlighted in blue."), gbc);
    return panel;
  }

  private void updatePanelForChoosingNodesLca() {
    lcaChoicesPanel.removeAll();
    JLabel labelA = new JLabel("Node A:");
    JLabel labelB = new JLabel("Node B:");

    // Let user choose nodes before running algorithm
    int size = (int) currentGraph.nodes().count();
    String[] choices = new String[size];
    currentGraph
        .nodes()
        .forEach(
            node -> {
              choices[node.getIndex()] = node.getId();
            });

    lcaA = new JComboBox<>(choices);
    lcaB = new JComboBox<>(choices);

    lcaChoicesPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.gridx = 0;
    gbc.gridy = 0;
    lcaChoicesPanel.add(labelA, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    lcaChoicesPanel.add(lcaA, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    lcaChoicesPanel.add(labelB, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    lcaChoicesPanel.add(lcaB, gbc);
  }

  private void addExampleChoosingPanel() {
    exampleButtonsPanel.setVisible(false);
    exampleButtonsPanel.removeAll();
    for (int i = 0; i < getCurrentDisplayExampleList().size(); i++) {
      JButton button = new JButton("Example " + i);
      int index = i;
      button.addActionListener(
          e -> {
            this.exampleNumber = index;
            run = false;
            updateDisplay();
          });
      exampleButtonsPanel.add(button);
    }
    exampleButtonsPanel.setVisible(true);

  }

  private void addPanelForRunningAndResettingAlgorithm() {
    runAlgorithmButton = new JButton("Run algorithm");
    resetAlgorithmButton = new JButton("Reset graph");
    resetAlgorithmButton.setVisible(false);

    runAlgorithmButton.addActionListener(
        e -> {
          run = true;
          updateDisplay();
        });

    resetAlgorithmButton.addActionListener(
        e -> {
          run = false;
          updateDisplay();
        });

    JPanel panel = new JPanel();
    panel.add(runAlgorithmButton);
    panel.add(resetAlgorithmButton);
    add(panel, BorderLayout.SOUTH);
  }

  private List<Graph> getCurrentDisplayExampleList() {
    if (task == 1) {
      return mstDisplayExamples;
    } else if (task == 2) {
      return lcaDisplayExamples;
    }
    return addRedundantDisplayExamples;
  }

  private void activateCorrectView() {
    if (task == 1) {
      activateDefaultView();
    } else {
      activateTreeView();
    }
  }

  private void updateCurrentGraphForDisplay() {
    currentGraph = getCurrentDisplayExampleList().get(exampleNumber);
    styleGraph(currentGraph);
  }

  private boolean runAlgorithmOnCurrentGraph() {
    if (task == 1) {
      return algorithmVisualizer.mst(currentGraph, mstExamples.get(exampleNumber));
    } else if (task == 2) {
      return algorithmVisualizer.lca(currentGraph, lcaExamples.get(exampleNumber), lcaA.getSelectedItem(),
          lcaB.getSelectedItem());
    } else {
      redundantEdge = algorithmVisualizer.addRedundant(currentGraph, addRedundantExamples.get(exampleNumber));
      if (!redundantEdge.isBlank()) {
        removeRedundantEdge = true;
        return true;
      }
      return false;
    }
  }

  private String getCurrentTask() {
    if (task == 1) {
      return "MST";
    } else if (task == 2) {
      return "LCA";
    } else if (task == 3) {
      return "Add Redundant";
    }
    throw new IllegalArgumentException("No task matching " + task);
  }

  private void updateCurrentTaskAndExampleLabel() {
    currentExampleLabel.setText("Current example: " + exampleNumber);
    currentTaskLabel.setText("Current task: " + getCurrentTask());
  }

  private void updateDisplay() {
    if (viewer != null && !run) {
      remove((Component) viewer.addDefaultView(false));
    }

    if (removeRedundantEdge) {
      currentGraph.removeEdge(redundantEdge);
    }

    addExampleChoosingPanel();
    updateCurrentGraphForDisplay();
    updateCurrentTaskAndExampleLabel();

    if (run) {
      runAlgorithmButton.setVisible(false);
      resetAlgorithmButton.setVisible(true);
      if (runAlgorithmOnCurrentGraph()) {
        statusMessage.append(getCurrentTask() + " applied.\n");
      } else {
        statusMessage.append(getCurrentTask() + " returned null.\n");
      }
    } else {
      runAlgorithmButton.setVisible(true);
      resetAlgorithmButton.setVisible(false);
    }

    if (task == 2) {
      if (run) {
        lcaA.setEnabled(false);
        lcaB.setEnabled(false);
      } else {
        updatePanelForChoosingNodesLca();
      }
    }

    if (!run) {
      viewer = new SwingViewer(currentGraph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
      add((Component) viewer.addDefaultView(false), BorderLayout.CENTER);
      activateCorrectView();
    }
  }
}
