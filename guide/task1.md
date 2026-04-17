# Task 1
The power company has gathered information on all the houses which needs to be connected to the grid. In addition, they have mapped for each pair of connected houses how expensive the cable between them will be. This has been modelled as a weighted graph where each node is a house and a cable between houses is an edge with a weight equal to the cost of that cable. The company wants to construct the power grid with the smallest cost possible while supplying all houses with electricity. Which cables should be used in the network?

This problem in literature is known as *Minimum Spanning Tree*, see chapter 4.3 in *Algorithms*.

In the example below the minimum spanning tree for the given graph is highlighted in bold black lines.
![Image](../images/mstExample.png)

**TODO:** Implement ``IProblem::mst``, and analyse the runtime of your method in [svar.md](svar.md).

By passing ``ProblemSolverMSTTest`` you will get full points for functionality. To get full points for the task, your solution also has to be efficient and with correct runtime analysis.
