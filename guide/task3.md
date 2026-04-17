### Task 3
After many complaints the power company has realised that the cheapest solution is not always the best. In a new attempt to solve the problem of constructing a power grid they will be adding an extra cable (an extra edge) to the network (graph). The goal is to have an outage impact the least amount of houses. One faulty cable can cause a large number of houses to lose power. You are to find a pair of nodes which by adding a cable between them will minimize the number of houses affected if another cable fails.


![Image](../images/powerPlantGraph.png)

In the example above if the edge between the **power plant** and node **f** fails,then 6 nodes will lose access to electricity. If we add an edge between **e** and **j** this problem will be averted as all nodes would still be in the same connected component as the **power plant** if the edge between the **power plant** and node **f** were to fail. In this case (where an edge between the **power plant** and **f** has been added), the largest power outage that can occur (by removal of one edge) is an outage that affects 2 houses (this happens when the edge between **f** and **b** fails).

**TODO:** Implement ``IProblem::addRedundant``, and analyse the runtime of your method in [svar.md](svar.md).

By passing ``ProblemSolverREPTest`` you will get full points for functionality. To get full points for the task, your solution also has to be efficient and with correct runtie analysis.
