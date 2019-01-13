package de.amr.mazes.simple;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import de.amr.easy.data.Partition;
import de.amr.mazes.simple.graph.Dir;
import de.amr.mazes.simple.graph.Edge;
import de.amr.mazes.simple.graph.GridGraph;

/**
 * Collection of maze generation algorithms.
 * 
 * @author Armin Reichert
 */
public class MazeAlgorithms {

	// Random Depth-First-Search (recursive)

	public static void createMazeByDFSRecursive(GridGraph grid, int vertex, BitSet visited) {
		visited.set(vertex);
		for (Dir dir : Dir.shuffled()) {
			int neighbor = grid.neighbor(vertex, dir);
			if (neighbor != -1 && !visited.get(neighbor)) {
				grid.connect(vertex, dir);
				createMazeByDFSRecursive(grid, neighbor, visited);
			}
		}
	}

	// Random Depth-First-Search (non-recursive)

	public static void createMazeByDFS(GridGraph grid, int startVertex) {
		BitSet visited = new BitSet();
		Deque<Integer> stack = new ArrayDeque<>();
		visited.set(startVertex);
		stack.push(startVertex);
		while (!stack.isEmpty()) {
			int vertex = stack.pop();
			for (Dir dir : Dir.shuffled()) {
				int neighbor = grid.neighbor(vertex, dir);
				if (neighbor != -1 && !visited.get(neighbor)) {
					grid.connect(vertex, dir);
					visited.set(neighbor);
					stack.push(neighbor);
				}
			}
		}
	}

	// Random Breadth-First-Search

	public static void createMazeByBFS(GridGraph grid, int startVertex) {
		BitSet visited = new BitSet();
		List<Integer> frontier = new ArrayList<>();
		Random rnd = new Random();
		visited.set(startVertex);
		frontier.add(startVertex);
		while (!frontier.isEmpty()) {
			int vertex = frontier.remove(rnd.nextInt(frontier.size()));
			for (Dir dir : Dir.shuffled()) {
				int neighbor = grid.neighbor(vertex, dir);
				if (neighbor != -1 && !visited.get(neighbor)) {
					grid.connect(vertex, dir);
					visited.set(neighbor);
					frontier.add(neighbor);
				}
			}
		}
	}

	// Kruskal's MST algorithm

	public static void createMazeByKruskal(GridGraph grid) {
		List<Edge> edges = new ArrayList<>();
		for (int row = 0; row < grid.rows; ++row) {
			for (int col = 0; col < grid.cols; ++col) {
				int vertex = grid.vertex(row, col);
				if (row > 0) {
					edges.add(new Edge(grid, vertex, grid.neighbor(vertex, Dir.N)));
				}
				if (col > 0) {
					edges.add(new Edge(grid, vertex, grid.neighbor(vertex, Dir.W)));
				}
			}
		}
		Collections.shuffle(edges);
		Partition<Integer> forest = new Partition<>();
		for (Edge edge : edges) {
			int u = edge.either, v = edge.other;
			if (forest.find(u) != forest.find(v)) {
				grid.connect(u, v);
				forest.union(u, v);
			}
		}
	}

	// Prim's MST algorithm

	public static void createMazeByPrim(GridGraph grid, int startVertex) {
		BitSet visited = new BitSet();
		PriorityQueue<Edge> cut = new PriorityQueue<>();
		Random rnd = new Random();
		expand(grid, startVertex, cut, visited, rnd);
		while (!cut.isEmpty()) {
			Edge edge = cut.poll();
			int u = edge.either, v = edge.other;
			if (!visited.get(u) || !visited.get(v)) {
				grid.connect(u, v);
				expand(grid, !visited.get(u) ? u : v, cut, visited, rnd);
			}
		}
	}

	private static void expand(GridGraph grid, int vertex, PriorityQueue<Edge> cut, BitSet visited,
			Random rnd) {
		visited.set(vertex);
		for (Dir dir : Dir.values()) {
			int neighbor = grid.neighbor(vertex, dir);
			if (neighbor != -1 && !visited.get(neighbor)) {
				Edge edge = new Edge(grid, vertex, neighbor, rnd.nextInt());
				cut.add(edge);
			}
		}
	}

	// Binary tree algorithm

	public static void createMazeByBinaryTree(GridGraph grid) {
		Random rnd = new Random();
		Dir[] dirs = { Dir.E, Dir.S };
		for (int vertex = 0; vertex < grid.numVertices(); ++vertex) {
			int choice = rnd.nextInt(2);
			int neighbor = grid.neighbor(vertex, dirs[choice]);
			if (neighbor != -1) {
				grid.connect(vertex, dirs[choice]);
			} else {
				neighbor = grid.neighbor(vertex, dirs[1 - choice]);
				if (neighbor != -1) {
					grid.connect(vertex, dirs[1 - choice]);
				}
			}
		}
	}

	// Growing tree algorithm

	public static void createMazeByGrowingTree(GridGraph grid, int startVertex) {
		Random rnd = new Random();
		BitSet visited = new BitSet();
		List<Integer> vertices = new ArrayList<>();
		vertices.add(startVertex);
		do {
			int index = rnd.nextBoolean() ? vertices.size() - 1 : rnd.nextInt(vertices.size());
			int vertex = vertices.remove(index);
			for (Dir dir : Dir.shuffled()) {
				int neighbor = grid.neighbor(vertex, dir);
				if (neighbor != -1 && !visited.get(neighbor)) {
					grid.connect(vertex, dir);
					vertices.add(neighbor);
					visited.set(vertex);
					visited.set(neighbor);
				}
			}
		} while (!vertices.isEmpty());
	}

	// Aldous/Broder algorithm

	public static void createMazeByAldousBroder(GridGraph grid, int startVertex) {
		Random rnd = new Random();
		BitSet visited = new BitSet();
		int vertex = startVertex;
		visited.set(vertex);
		while (visited.cardinality() < grid.numVertices()) {
			Dir dir = Dir.values()[rnd.nextInt(4)];
			int neighbor = grid.neighbor(vertex, dir);
			if (neighbor != -1) {
				if (!visited.get(neighbor)) {
					grid.connect(vertex, dir);
					visited.set(neighbor);
				}
				vertex = neighbor;
			}
		}
	}
}