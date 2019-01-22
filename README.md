## Maze generation algorithms

This project provides Java implementations of more than 35 algorithms for generating so called "perfect mazes" (which are just spanning trees of undirected graphs).

<img style="width:100%; height=auto" src="https://github.com/armin-reichert/mazes/blob/master/Demos/images/mazedemoapp.png">

On the web, many maze generation implementations in all possible programming languages can be found. The popularity of these algorithms probably comes from the fact that mazes and their creation processes are visually appealing and not really difficult to implement. The most popular algorithm seems to be "recursive backtracking" which is random depth-first traversal of a graph. 

On the other side, there are not so many sites where the whole spectrum of maze creation algorithms is investigated. One exception is [this blog](http://weblog.jamisbuck.org/2011/2/7/maze-generation-algorithm-recap) where Jamis Buck presents the most popular maze algorithms with Ruby and animated Javascript implementations. Reading his blog led myself to investigate this topic too.

Initially I intended to implement some of these algorithms in Java to learn about the new Java 8 features (streams, lambda expressions). I also wanted to implement the needed data structures (graph, grid graph, union-find, ...) not just in an "ad-hoc" fashion. The maze algorithm implementations should become pure graph algorithms without any UI or animation related parts. The underlying graph algorithms, for example minimum-spanning tree algorithms, should still be clearly recognizable in the maze generator code. Avoiding dependencies to UI frameworks should make the maze generators more reusable. For example, the animated GIF images below have been created using a grid observer which takes snapshots of the maze while being created. The maze generator code is not affected.

In the end, all of the algorithms presented in Jamis Buck's blog and even some new algorithms have been implemented. One new algorithm is a modification of Eller's algorithm that in contrast to the original doesn't generate the maze row-wise but from the center of the grid towards the outer borders. The resulting maze however is heavily biased. Other new algorithms are variations of Wilson's uniform spanning tree algorithm. They result from the different possibilities for selecting the random walk start cells. 

As the order in which the random walk start cells are selected is arbitrary, we have a number of interesting choices. For example, you can start the random walks in the order defined by a space-filling curves like [Hilbert](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/grid/curves/HilbertCurve.java), [Peano](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/grid/curves/PeanoCurve.java) or [Moore](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/grid/curves/MooreLCurve.java) curves. You can also use other interesting patterns of filling a grid. In any case you will get visually appealing maze creation processes. 

Also implemented in this project are path finding algorithms for "solving" the generated mazes: "Breadth-First-Search" and "Depth-First-Search" together with their informed variants "Best-First-Search" and "Hill Climbing". For completeness, the A* and Dijkstra algorithms are also included. The Dijkstra algorithm however does not provide additional value because the graphs of the mazes have uniform edge cost.

The included [demo application](https://github.com/armin-reichert/mazes/releases/) demonstrates all implemented maze generators and path finders. Using a control panel you can interactively select the generation algorithm, path finder, grid resolution and rendering style ("walls", "passages").

To achieve the mentioned goals, I implemented
- an API for [graph](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/core/api/Graph.java) and [2D-grid](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/grid/api/GridGraph2D.java) data structures 
- a space-efficient implementation of a [2D-grid](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/grid/impl/GridGraph.java) with ability to store cell and edge content
- a publish-subscribe mechanism for observing graph/grid operations and different path finding algorithms.

This is the maze generator derived from Kruskal's minimum spanning tree algorithm:

```java
public class KruskalMST implements MazeGenerator<OrthogonalGrid> {

	private OrthogonalGrid grid;

	public KruskalMST(int numCols, int numRows) {
		grid = emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		Partition<Integer> forest = new Partition<>();
		permute(fullGrid(grid.numCols(), grid.numRows(), UNVISITED).edges()).forEach(edge -> {
			int u = edge.either(), v = edge.other();
			if (forest.find(u) != forest.find(v)) {
				grid.addEdge(u, v);
				grid.set(u, COMPLETED);
				grid.set(v, COMPLETED);
				forest.union(u, v);
			}
		});
		return grid;
	}
}
```
Anybody familiar with the Kruskal algorithm will immediately recognize it in this code. The difference is that in the maze generator the edges of a (full) grid are selected in random  order where the original MST algorithm greedily selects the minimum cost edge in each step.

Implemented maze generation algorithms:

### Graph Traversal:

#### [Random Breadth-First-Search](EasyMaze/src/main/java/de/amr/easy/maze/alg/traversal/RandomBFS.java)

![](Demos/images/gen/maze_80x60_RandomBFS.gif)

#### [Random Depth-First-Search, iterative](EasyMaze/src/main/java/de/amr/easy/maze/alg/traversal/IterativeDFS.java)

![](Demos/images/gen/maze_80x60_IterativeDFS.gif)

#### [Random Depth-First-Search, recursive](EasyMaze/src/main/java/de/amr/easy/maze/alg/traversal/RecursiveDFS.java)

![](Demos/images/gen/maze_40x30_RecursiveDFS.gif)

### Minimum Spanning Tree: 
#### [Boruvka](EasyMaze/src/main/java/de/amr/easy/maze/alg/mst/BoruvkaMST.java)

![](Demos/images/gen/maze_80x60_BoruvkaMST.gif)

#### [Kruskal](EasyMaze/src/main/java/de/amr/easy/maze/alg/mst/KruskalMST.java)

![](Demos/images/gen/maze_80x60_KruskalMST.gif)

#### [Prim](EasyMaze/src/main/java/de/amr/easy/maze/alg/mst/PrimMST.java)

![](Demos/images/gen/maze_80x60_PrimMST.gif)

#### [Reverse-Delete, base algorithm](EasyMaze/src/main/java/de/amr/easy/maze/alg/mst/ReverseDeleteMST.java)

  - [Reverse-Delete, DFS variant](EasyMaze/src/main/java/de/amr/easy/maze/alg/mst/ReverseDeleteMST_DFS.java)

  - [Reverse-Delete, BFS variant](EasyMaze/src/main/java/de/amr/easy/maze/alg/mst/ReverseDeleteMST_BFS.java)

  - [Reverse-Delete, Best FS variant](EasyMaze/src/main/java/de/amr/easy/maze/alg/mst/ReverseDeleteMST_BestFS.java)

  - [Reverse-Delete, Hill Climbing variant](EasyMaze/src/main/java/de/amr/easy/maze/alg/mst/ReverseDeleteMST_HillClimbing.java)

![](Demos/images/gen/maze_40x25_ReverseDeleteMST.gif)

### Uniform Spanning Tree:

#### [Aldous-Broder](EasyMaze/src/main/java/de/amr/easy/maze/alg/ust/AldousBroderUST.java)

![](Demos/images/gen/maze_8x8_AldousBroderUST.gif)

#### [Houston](EasyMaze/src/main/java/de/amr/easy/maze/alg/ust/AldousBroderWilsonUST.java)

#### [Wilson's algorithm](EasyMaze/src/main/java/de/amr/easy/maze/alg/ust) (16 different variants)

![](Demos/images/gen/maze_80x60_WilsonUSTRandomCell.gif)

![](Demos/images/gen/maze_80x60_WilsonUSTCollapsingCircle.gif)

![](Demos/images/gen/maze_80x60_WilsonUSTRecursiveCrosses.gif)

### Other algorithms:

#### [Binary Tree, top-to-bottom](EasyMaze/src/main/java/de/amr/easy/maze/alg/BinaryTree.java)

![](Demos/images/gen/maze_80x60_BinaryTree.gif)

#### [Binary Tree, random](EasyMaze/src/main/java/de/amr/easy/maze/alg/BinaryTreeRandom.java)

![](Demos/images/gen/maze_80x60_BinaryTreeRandom.gif)

#### [Eller's algorithm](EasyMaze/src/main/java/de/amr/easy/maze/alg/Eller.java)

![](Demos/images/gen/maze_80x60_Eller.gif)

#### [Armin's algorithm](EasyMaze/src/main/java/de/amr/easy/maze/alg/EllerInsideOut.java) (like Eller's but growing the maze inside-out)

![](Demos/images/gen/maze_80x60_EllerInsideOut.gif)

#### [Sidewinder](EasyMaze/src/main/java/de/amr/easy/maze/alg/Sidewinder.java)

![](Demos/images/gen/maze_80x60_Sidewinder.gif)

#### [Growing Tree](EasyMaze/src/main/java/de/amr/easy/maze/alg/GrowingTree.java)

![](Demos/images/gen/maze_80x60_GrowingTree.gif)

#### [Hunt-And-Kill, top-to-bottom](EasyMaze/src/main/java/de/amr/easy/maze/alg/HuntAndKill.java)

![](Demos/images/gen/maze_80x60_HuntAndKill.gif)

#### [Hunt-And-Kill, random](EasyMaze/src/main/java/de/amr/easy/maze/alg/HuntAndKillRandom.java)

![](Demos/images/gen/maze_80x60_HuntAndKillRandom.gif)

#### [Recursive division](EasyMaze/src/main/java/de/amr/easy/maze/alg/RecursiveDivision.java)

![](Demos/images/gen/maze_80x60_RecursiveDivision.gif)

### Path finding algorithms:
The [EasyGraph](https://github.com/armin-reichert/graph) library contains the following path finder implementations:
- [Breadth-First-Search](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/pathfinder/impl/traversal/BreadthFirstTraversal.java)
- [Depth-First-Search](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/pathfinder/impl/traversal/DepthFirstTraversal.java)
- [(Greedy) Best-First-Search](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/pathfinder/impl/traversal/BestFirstTraversal.java). Can be used with Euclidean, Manhattan and Chebyshev distance heuristics.
- [Hill Climbing](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/pathfinder/impl/traversal/HillClimbing.java). Can be used with Euclidean, Manhattan and Chebyshev distance heuristics.
- [A* Search](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/pathfinder/impl/traversal/AStarTraversal.java).
- [Dijkstra](https://github.com/armin-reichert/graph/tree/master/EasyGraph/src/main/java/de/amr/easy/graph/pathfinder/impl/traversal/DijkstraTraversal.java).
