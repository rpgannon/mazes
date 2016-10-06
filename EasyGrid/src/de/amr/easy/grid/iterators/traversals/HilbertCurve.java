package de.amr.easy.grid.iterators.traversals;

import static de.amr.easy.grid.api.Direction.E;
import static de.amr.easy.grid.api.Direction.N;
import static de.amr.easy.grid.api.Direction.S;
import static de.amr.easy.grid.api.Direction.W;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.amr.easy.grid.api.Direction;

/**
 * Computes a Hilbert curve as a list of directions.
 *
 * @author Armin Reichert
 */
public class HilbertCurve implements Iterable<Direction> {

	private final List<Direction> curve = new ArrayList<>();

	public HilbertCurve(int depth) {
		hilbert(depth, N, E, S, W);
	}

	public HilbertCurve(int depth, Direction n, Direction e, Direction s, Direction w) {
		hilbert(depth, n, e, s, w);
	}

	private void hilbert(int depth, Direction n, Direction e, Direction s, Direction w) {
		if (depth > 0) {
			hilbert(depth - 1, e, n, w, s);
			curve.add(w);
			hilbert(depth - 1, n, e, s, w);
			curve.add(s);
			hilbert(depth - 1, n, e, s, w);
			curve.add(e);
			hilbert(depth - 1, w, s, e, n);
		}
	}

	@Override
	public Iterator<Direction> iterator() {
		return curve.iterator();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Direction dir : curve) {
			s.append(dir.name()).append(" ");
		}
		return s.toString();
	}
}