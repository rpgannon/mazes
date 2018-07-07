package de.amr.easy.graph.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.api.UndirectedEdge;
import de.amr.easy.graph.api.event.GraphTraversalObserver;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.DefaultGraph;
import de.amr.easy.graph.impl.traversal.DepthFirstTraversal;

public class LabeledGraphTests {

	private class DFSTracer implements GraphTraversalObserver {

		private final DefaultGraph<String, Integer> graph;
		private final List<Integer> trace = new ArrayList<>();

		public DFSTracer(DefaultGraph<String, Integer> graph) {
			this.graph = graph;
		}

		@Override
		public void vertexTraversed(int v, TraversalState oldState, TraversalState newState) {
			trace.add(v);
		}

		@Override
		public void edgeTraversed(int either, int other) {

		}

		public String getVertexTrace() {
			return trace.stream().map(String::valueOf).collect(Collectors.joining());
		}

		public String getVertexLabelTrace() {
			return trace.stream().map(graph::get).collect(Collectors.joining());
		}

	};

	private DefaultGraph<String, Integer> g;

	private DefaultGraph<String, Integer> createSampleGraph() {
		DefaultGraph<String, Integer> g = new DefaultGraph<>(UndirectedEdge::new);
		IntStream.range(0, 8).forEach(g::addVertex);
		g.set(0, "S");
		g.set(1, "A");
		g.set(2, "B");
		g.set(3, "C");
		g.set(4, "D");
		g.set(5, "E");
		g.set(6, "F");
		g.set(7, "G");
		g.addEdge(0, 1); // S-A
		g.addEdge(1, 2); // A-B
		g.addEdge(2, 3); // B-C
		g.addEdge(0, 4); // A-D
		g.addEdge(4, 1); // D-A
		g.addEdge(4, 5); // D-E
		g.addEdge(5, 2); // E-B
		g.addEdge(5, 6); // E-F
		g.addEdge(6, 7); // F-G
		return g;
	}

	@Before
	public void setUp() {
		g = new DefaultGraph<>(UndirectedEdge::new);
	}

	@Test(expected = IllegalStateException.class)
	public void testSetLabelOnNonexistingVertex() {
		g.set(0, "A");
	}

	@Test
	public void testSetLabel() {
		g.addVertex(0);
		g.set(0, "A");
		Assert.assertEquals("A", g.get(0));
	}

	@Test
	public void testDefaultVertexLabel() {
		g.setDefaultVertexLabel("42");
		g.addVertex(0);
		Assert.assertEquals("42", g.get(0));
		g.set(0, "43");
		Assert.assertNotEquals("42", g.get(0));
	}

	@Test
	public void testSampleGraph() {
		DefaultGraph<String, Integer> g = createSampleGraph();
		Assert.assertTrue(g.adjacent(0, 1));
		Assert.assertTrue(g.adjacent(1, 2));
		Assert.assertTrue(g.adjacent(2, 3));
		Assert.assertTrue(g.adjacent(0, 4));
		Assert.assertTrue(g.adjacent(4, 1));
		Assert.assertTrue(g.adjacent(4, 5));
		Assert.assertTrue(g.adjacent(5, 2));
		Assert.assertTrue(g.adjacent(5, 6));
		Assert.assertTrue(g.adjacent(6, 7));
	}

	@Test
	public void testSampleGraphDFS() {
		DefaultGraph<String, Integer> g = createSampleGraph();
		DepthFirstTraversal dfs = new DepthFirstTraversal(g);
		DFSTracer tracer = new DFSTracer(g);
		dfs.addObserver(tracer);
		dfs.traverseGraph(0);
		System.out.println(tracer.getVertexTrace());
		System.out.println(tracer.getVertexLabelTrace());
	}
}
