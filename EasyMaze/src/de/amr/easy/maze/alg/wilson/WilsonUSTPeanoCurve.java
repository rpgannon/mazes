package de.amr.easy.maze.alg.wilson;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.maze.misc.Utils.log;
import static de.amr.easy.maze.misc.Utils.nextPow;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.NakedGrid;
import de.amr.easy.grid.iterators.curves.PeanoCurve;

/**
 * Wilson's algorithm where the vertices are selected from a Peano-curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTPeanoCurve extends WilsonUST {

	private final List<Integer> path = new ArrayList<>();

	public WilsonUSTPeanoCurve(Grid2D<TraversalState,Integer> grid) {
		super(grid);
	}

	@Override
	protected Stream<Integer> cellStream() {
		int n = nextPow(3, max(grid.numCols(), grid.numRows()));
		PeanoCurve peano = new PeanoCurve(log(3, n));
		NakedGrid<?> square = new NakedGrid<>(n, n);
		Integer cell = square.cell(BOTTOM_LEFT);
		addCellToPath(square.col(cell), square.row(cell));
		for (Direction dir : peano) {
			cell = square.neighbor(cell, dir).get();
			addCellToPath(square.col(cell), square.row(cell));
		}
		return path.stream();
	}

	private void addCellToPath(int col, int row) {
		if (grid.isValidCol(col) && grid.isValidRow(row)) {
			path.add(grid.cell(col, row));
		}
	}
}