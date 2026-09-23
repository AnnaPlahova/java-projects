package org.example.graphs.s21_graph_algorithms;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.graphs.s21_graph.Graph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GraphAlgorithmsTest {

  private final GraphAlgorithms algorithms = new GraphAlgorithms();

  @Test
  @DisplayName("Dijkstra 1")
  void testSimplePath() {
    Graph g = new Graph(3, false); // неориентированный
    g.setWeight(0, 1, 5); // 1--2 вес 5
    g.setWeight(1, 2, 3); // 2--3 вес 3

    assertEquals(8, algorithms.getShortestPathBetweenVertices(g, 1, 3));
    assertEquals(5, algorithms.getShortestPathBetweenVertices(g, 1, 2));
    assertEquals(3, algorithms.getShortestPathBetweenVertices(g, 2, 3));
  }

  @Test
  @DisplayName("Dijkstra 2")
  void testMultiStepImprovement() {
    Graph g = new Graph(4, false);
    g.setWeight(0, 1, 5); // 1--2 (5)
    g.setWeight(0, 2, 2); // 1--3 (2)
    g.setWeight(2, 1, 1); // 3--2 (1)
    g.setWeight(1, 3, 1); // 2--4 (1)
    g.setWeight(2, 3, 4); // 3--4 (4)

    // Ожидаем 1->3->2->4 = 2+1+1 = 4
    assertEquals(4, algorithms.getShortestPathBetweenVertices(g, 1, 4));
  }

  @Test
  @DisplayName("Dijkstra 3")
  void testNoPath() {
    Graph g = new Graph(3, true); // слабосвязный ориентированный граф
    g.setWeight(0, 1, 5); // 1--2 вес 5
    g.setWeight(1, 2, 3); // 2--3 вес 3

    assertEquals(8, algorithms.getShortestPathBetweenVertices(g, 1, 3));
    assertEquals(-1, algorithms.getShortestPathBetweenVertices(g, 3, 1));
  }

  @Test
  @DisplayName("Dijkstra 4")
  void testSameVertex() {
    Graph g = new Graph(2, false);
    g.setWeight(0, 1, 10);

    assertEquals(0, algorithms.getShortestPathBetweenVertices(g, 1, 1));
    assertEquals(0, algorithms.getShortestPathBetweenVertices(g, 2, 2));
  }

  @Test
  @DisplayName("Dijkstra 5")
  void testInvalidVertexLessThanOne() {
    Graph g = new Graph(3, false);

    assertThrows(
        IllegalArgumentException.class, () -> algorithms.getShortestPathBetweenVertices(g, 0, 1));
  }

  @Test
  @DisplayName("Dijkstra 6")
  void testInvalidVertexOutOfRange() {
    Graph g = new Graph(3, false);

    assertThrows(
        IllegalArgumentException.class, () -> algorithms.getShortestPathBetweenVertices(g, 1, 5));
  }

  @Test
  @DisplayName("Dijkstra 7")
  void testCycleAndShortcut() {
    // Прямой путь 1->3 = 10, но 1->2->3 = 2+2 = 4
    Graph g = new Graph(3, false);
    g.setWeight(0, 2, 10); // 1--3
    g.setWeight(0, 1, 2); // 1--2
    g.setWeight(1, 2, 2); // 2--3

    assertEquals(4, algorithms.getShortestPathBetweenVertices(g, 1, 3));
  }

  @Test
  @DisplayName("Dijkstra 8")
  void testUndirectedNoPath() {
    Graph g = disconnectedGraph();

    assertEquals(-1, algorithms.getShortestPathBetweenVertices(g, 1, 3));
    assertEquals(-1, algorithms.getShortestPathBetweenVertices(g, 3, 2));
    assertEquals(1, algorithms.getShortestPathBetweenVertices(g, 1, 2));
  }

  @Test
  @DisplayName("Floyd 1")
  void testFloydSimplePath() {
    Graph g = new Graph(3, false);
    g.setWeight(0, 1, 5);
    g.setWeight(1, 2, 3);

    int[][] dist = algorithms.getShortestPathsBetweenAllVertices(g);

    assertArrayEquals(new int[] {0, 5, 8}, dist[0]);
    assertArrayEquals(new int[] {5, 0, 3}, dist[1]);
    assertArrayEquals(new int[] {8, 3, 0}, dist[2]);
  }

  @Test
  @DisplayName("Floyd 2")
  void testFloydImprovesThroughIntermediate() {
    Graph g = new Graph(3, false);
    g.setWeight(0, 2, 10);
    g.setWeight(0, 1, 2);
    g.setWeight(1, 2, 2);

    int[][] dist = algorithms.getShortestPathsBetweenAllVertices(g);

    assertArrayEquals(new int[] {0, 2, 4}, dist[0]);
    assertArrayEquals(new int[] {2, 0, 2}, dist[1]);
    assertArrayEquals(new int[] {4, 2, 0}, dist[2]);
  }

  @Test
  @DisplayName("Floyd 3")
  void testFloydUnreachableIsMaxValue() {
    Graph directed = new Graph(3, true);
    directed.setWeight(0, 1, 5);
    directed.setWeight(1, 2, 3);

    int[][] dist = algorithms.getShortestPathsBetweenAllVertices(directed);
    int inf = Integer.MAX_VALUE;

    assertArrayEquals(new int[] {0, 5, 8}, dist[0]);
    assertArrayEquals(new int[] {inf, 0, 3}, dist[1]);
    assertArrayEquals(new int[] {inf, inf, 0}, dist[2]);
  }

  @Test
  @DisplayName("Floyd 4")
  void testFloydDisconnectedAndSingleVertex() {
    int[][] disconnected = algorithms.getShortestPathsBetweenAllVertices(disconnectedGraph());
    int inf = Integer.MAX_VALUE;

    assertArrayEquals(new int[] {0, 1, inf}, disconnected[0]);
    assertArrayEquals(new int[] {1, 0, inf}, disconnected[1]);
    assertArrayEquals(new int[] {inf, inf, 0}, disconnected[2]);

    int[][] single = algorithms.getShortestPathsBetweenAllVertices(new Graph(1, false));
    assertEquals(1, single.length);
    assertEquals(0, single[0][0]);
  }

  @Test
  @DisplayName("Floyd 5")
  void testFloydMatchesDijkstraForAllPairs() {
    Graph g = new Graph(4, false);
    g.setWeight(0, 1, 5);
    g.setWeight(0, 2, 2);
    g.setWeight(2, 1, 1);
    g.setWeight(1, 3, 1);
    g.setWeight(2, 3, 4);

    int[][] dist = algorithms.getShortestPathsBetweenAllVertices(g);
    int n = g.getSize();
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n; j++) {
        int dijkstra = algorithms.getShortestPathBetweenVertices(g, i, j);
        int expected = (dijkstra == -1) ? Integer.MAX_VALUE : dijkstra;
        assertEquals(expected, dist[i - 1][j - 1], "pair " + i + "-" + j);
      }
    }
  }

  @Test
  @DisplayName("DFS 1")
  void testDfsPathGraph() {
    Graph g = pathGraph3();

    assertArrayEquals(new int[] {1, 2, 3}, algorithms.depthFirstSearch(g, 1));
    assertArrayEquals(new int[] {2, 1, 3}, algorithms.depthFirstSearch(g, 2));
    assertArrayEquals(new int[] {3, 2, 1}, algorithms.depthFirstSearch(g, 3));
  }

  @Test
  @DisplayName("DFS 2")
  void testDfsBranchingOrder() {
    // Соседи кладутся в стек с конца, поэтому первым идёт меньший номер
    Graph g = branchingGraph();

    assertArrayEquals(new int[] {1, 2, 4, 5, 3}, algorithms.depthFirstSearch(g, 1));
  }

  @Test
  @DisplayName("DFS 3")
  void testDfsDisconnectedLeavesUnreachableAsZero() {
    Graph g = disconnectedGraph();

    assertArrayEquals(new int[] {1, 2, 0}, algorithms.depthFirstSearch(g, 1));
    assertArrayEquals(new int[] {3, 0, 0}, algorithms.depthFirstSearch(g, 3));
  }

  @Test
  @DisplayName("DFS 4")
  void testDfsSingleVertexAndDirected() {
    Graph single = new Graph(1, false);
    assertArrayEquals(new int[] {1}, algorithms.depthFirstSearch(single, 1));

    Graph directed = new Graph(3, true);
    directed.setWeight(0, 1, 1); // 1 -> 2
    directed.setWeight(1, 2, 1); // 2 -> 3

    assertArrayEquals(new int[] {1, 2, 3}, algorithms.depthFirstSearch(directed, 1));
    assertArrayEquals(new int[] {3, 0, 0}, algorithms.depthFirstSearch(directed, 3));
  }

  @Test
  @DisplayName("DFS 5")
  void testDfsCycleDoesNotRepeatVertices() {
    Graph g = cycleGraph4();

    assertArrayEquals(new int[] {1, 2, 3, 4}, algorithms.depthFirstSearch(g, 1));
  }

  @Test
  @DisplayName("BFS 1")
  void testBfsPathGraph() {
    Graph g = pathGraph3();

    assertArrayEquals(new int[] {1, 2, 3}, algorithms.breadthFirstSearch(g, 1));
    assertArrayEquals(new int[] {2, 1, 3}, algorithms.breadthFirstSearch(g, 2));
    assertArrayEquals(new int[] {3, 2, 1}, algorithms.breadthFirstSearch(g, 3));
  }

  @Test
  @DisplayName("BFS 2")
  void testBfsLevelOrder() {
    Graph g = branchingGraph();

    assertArrayEquals(new int[] {1, 2, 3, 4, 5}, algorithms.breadthFirstSearch(g, 1));
  }

  @Test
  @DisplayName("BFS 3")
  void testBfsDisconnectedLeavesUnreachableAsZero() {
    Graph g = disconnectedGraph();

    assertArrayEquals(new int[] {1, 2, 0}, algorithms.breadthFirstSearch(g, 1));
    assertArrayEquals(new int[] {3, 0, 0}, algorithms.breadthFirstSearch(g, 3));
  }

  @Test
  @DisplayName("BFS 4")
  void testBfsSingleVertexAndDirected() {
    Graph single = new Graph(1, false);
    assertArrayEquals(new int[] {1}, algorithms.breadthFirstSearch(single, 1));

    Graph directed = new Graph(3, true);
    directed.setWeight(0, 1, 1); // 1 -> 2
    directed.setWeight(0, 2, 1); // 1 -> 3

    assertArrayEquals(new int[] {1, 2, 3}, algorithms.breadthFirstSearch(directed, 1));
    assertArrayEquals(new int[] {3, 0, 0}, algorithms.breadthFirstSearch(directed, 3));
  }

  @Test
  @DisplayName("BFS 5")
  void testBfsCycleDoesNotRepeatVertices() {
    Graph g = cycleGraph4();

    assertArrayEquals(new int[] {1, 2, 4, 3}, algorithms.breadthFirstSearch(g, 1));
  }

  @Test
  @DisplayName("MST 1")
  void testMstAlreadyTree() {
    Graph g = pathGraph3();

    int[][] tree = algorithms.getLeastSpanningTree(g);

    assertArrayEquals(new int[] {0, 1, 0}, tree[0]);
    assertArrayEquals(new int[] {1, 0, 1}, tree[1]);
    assertArrayEquals(new int[] {0, 1, 0}, tree[2]);
    assertEquals(2, spanningWeight(tree));
  }

  @Test
  @DisplayName("MST 2")
  void testMstPrimChoosesCheapestEdges() {
    Graph g = new Graph(4, false);
    g.setWeight(0, 1, 2); // 1--2
    g.setWeight(0, 2, 6); // 1--3
    g.setWeight(0, 3, 3); // 1--4
    g.setWeight(1, 3, 4); // 2--4
    g.setWeight(2, 3, 1); // 3--4

    int[][] tree = algorithms.getLeastSpanningTree(g);

    assertSymmetric(tree);
    assertEquals(6, spanningWeight(tree));
    assertEquals(2, tree[0][1]);
    assertEquals(3, tree[0][3]);
    assertEquals(1, tree[2][3]);
    assertEquals(0, tree[0][2]);
    assertEquals(0, tree[1][3]);
  }

  @Test
  @DisplayName("MST 3")
  void testMstTwoVerticesAndSingleVertex() {
    Graph pair = new Graph(2, false);
    pair.setWeight(0, 1, 5);

    int[][] pairTree = algorithms.getLeastSpanningTree(pair);
    assertArrayEquals(new int[] {0, 5}, pairTree[0]);
    assertArrayEquals(new int[] {5, 0}, pairTree[1]);

    int[][] singleTree = algorithms.getLeastSpanningTree(new Graph(1, false));
    assertEquals(1, singleTree.length);
    assertEquals(0, singleTree[0][0]);
  }

  @Test
  @DisplayName("MST 4")
  void testMstDropsExpensiveEdge() {
    Graph g = new Graph(3, false);
    g.setWeight(0, 1, 1);
    g.setWeight(1, 2, 2);
    g.setWeight(0, 2, 10);

    int[][] tree = algorithms.getLeastSpanningTree(g);

    assertSymmetric(tree);
    assertEquals(3, spanningWeight(tree));
    assertArrayEquals(new int[] {0, 1, 0}, tree[0]);
    assertArrayEquals(new int[] {1, 0, 2}, tree[1]);
    assertArrayEquals(new int[] {0, 2, 0}, tree[2]);
  }

  @Test
  @DisplayName("TSP 1")
  void testTspTriangle() {
    Graph g = new Graph(3, false);
    g.setWeight(0, 1, 2);
    g.setWeight(1, 2, 3);
    g.setWeight(0, 2, 4);

    TsmResult result = algorithms.solveTravelingSalesmanProblem(g);

    assertValidTour(g, result);
    assertEquals(9.0, result.distance, 1e-9);
  }

  @Test
  @DisplayName("TSP 2")
  void testTspCheapCycleAmongExpensiveDiagonals() {
    Graph g = new Graph(4, false);
    g.setWeight(0, 1, 1);
    g.setWeight(1, 2, 1);
    g.setWeight(2, 3, 1);
    g.setWeight(3, 0, 1);
    g.setWeight(0, 2, 100);
    g.setWeight(1, 3, 100);

    TsmResult result = algorithms.solveTravelingSalesmanProblem(g);

    assertValidTour(g, result);
    assertEquals(4.0, result.distance, 1e-9);
  }

  @Test
  @DisplayName("TSP 3")
  void testTspImpossibleReturnsNull() {
    assertNull(algorithms.solveTravelingSalesmanProblem(pathGraph3()));
    assertNull(algorithms.solveTravelingSalesmanProblem(disconnectedGraph()));
    assertNull(algorithms.solveTravelingSalesmanProblem(new Graph(1, false)));
  }

  @Test
  @DisplayName("TSP 4")
  void testTspTwoVertices() {
    Graph g = new Graph(2, false);
    g.setWeight(0, 1, 5);

    TsmResult result = algorithms.solveTravelingSalesmanProblem(g);

    assertValidTour(g, result);
    assertEquals(10.0, result.distance, 1e-9);
  }

  @Test
  @DisplayName("TSP 5")
  void testTspDirectedCycle() {
    Graph g = new Graph(3, true);
    g.setWeight(0, 1, 2);
    g.setWeight(1, 2, 3);
    g.setWeight(2, 0, 4);

    TsmResult result = algorithms.solveTravelingSalesmanProblem(g);

    assertValidTour(g, result);
    assertEquals(9.0, result.distance, 1e-9);
  }

  private static Graph pathGraph3() {
    Graph g = new Graph(3, false);
    g.setWeight(0, 1, 1);
    g.setWeight(1, 2, 1);
    return g;
  }

  private static Graph branchingGraph() {
    Graph g = new Graph(5, false);
    g.setWeight(0, 1, 1); // 1--2
    g.setWeight(0, 2, 1); // 1--3
    g.setWeight(1, 3, 1); // 2--4
    g.setWeight(1, 4, 1); // 2--5
    return g;
  }

  private static Graph disconnectedGraph() {
    Graph g = new Graph(3, false);
    g.setWeight(0, 1, 1); // 1--2, вершина 3 изолирована
    return g;
  }

  private static Graph cycleGraph4() {
    Graph g = new Graph(4, false);
    g.setWeight(0, 1, 1); // 1--2
    g.setWeight(1, 2, 1); // 2--3
    g.setWeight(2, 3, 1); // 3--4
    g.setWeight(3, 0, 1); // 4--1
    return g;
  }

  private static int spanningWeight(int[][] tree) {
    int sum = 0;
    for (int i = 0; i < tree.length; i++) {
      for (int j = i + 1; j < tree.length; j++) {
        sum += tree[i][j];
      }
    }
    return sum;
  }

  private static void assertSymmetric(int[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        assertEquals(matrix[i][j], matrix[j][i]);
      }
    }
  }

  private static void assertValidTour(Graph graph, TsmResult result) {
    assertNotNull(result);
    assertNotNull(result.vertices);

    int n = graph.getSize();
    assertEquals(n + 1, result.vertices.length);
    assertEquals(result.vertices[0], result.vertices[n]);

    boolean[] seen = new boolean[n + 1];
    double expectedDistance = 0.0;
    for (int i = 0; i < n; i++) {
      int vertex = result.vertices[i];
      assertTrue(vertex >= 1 && vertex <= n);
      assertFalse(seen[vertex]);
      seen[vertex] = true;

      int next = result.vertices[i + 1];
      int weight = graph.getWeight(vertex - 1, next - 1);
      assertTrue(weight != 0);
      expectedDistance += weight;
    }
    assertEquals(expectedDistance, result.distance, 1e-9);
  }
}
