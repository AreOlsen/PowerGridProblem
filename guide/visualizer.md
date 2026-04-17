# Graph Visualizer

When you run the main file of this project, this window will appear.

![Image](../images/GUI.jpg)

## Explanations

1. Instructions for how to use the interface.
2. A panel for choosing which algorithm to test. The text below the buttons lets you know which task and what example
   you are currently viewing.
3. A status panel. Tells you if the algorithm was applied or if the algorithm returned null.
4. A panel for choosing between example graphs for the current task. Each task has a unique set of example graphs.
5. The current example graph visualized.
6. When pressing this button, the algorithm is applied to the currently displayed graph.

For the LCA examples, another panel will appear on the right of the screen where you choose what nodes to find the
least common ancestor for:

![Image](../images/LCA.jpg)

## How to interpret the results

### MST

The minimum spanning tree nodes and edges will be highlighted in blue.
The weights of each edge will also be highlighted blue, as to easily pair weight and edge.

### LCA

The two nodes you chose will be highlighted in orange.
The node returned by your algorithm will be highlighted in blue.

### Add Redundant

An additional edge should be added to the graph. This edge will be highlighted blue.
The two nodes this edge is between will also be highlighted blue.

## Add your own example graphs

To add your own graphs as examples, see this guide: [add examples guide](./addExamples.md)