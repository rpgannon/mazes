package de.amr.easy.maze.algorithms;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;

public class BinaryTreeRandom extends BinaryTree {

	private final List<Integer> cellsInRandomOrder;

	public BinaryTreeRandom(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		super(grid);
		cellsInRandomOrder = grid.vertexStream().collect(Collectors.toList());
		Collections.shuffle(cellsInRandomOrder);
	}

	@Override
	protected Stream<Integer> getCells() {
		return cellsInRandomOrder.stream();
	}
}