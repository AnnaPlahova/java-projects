package org.example.graphs.s21_graph_algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.example.graphs.s21_graph.Graph;
import org.example.graphs.s21_queue.Queue;
import org.example.graphs.s21_stack.Stack;

public class GraphAlgorithms {

  // внутри матрицы - обычный массив с индексами с 0
  // снаружи матрицы -  то, что видит задание, консоль, стек, result: вершины 1, 2, 3, N
  // startVertex приходит уже как будто снаружи

  // используем итеративный подход. Т.е. в стек кладем соседей текущей вершины.
  // пока в стеке что-то есть, будем доставать вершину и смотреть, посещали ли её.
  // Если нет, отмечаем как посещенную, заносим в result и в цикле скалдываем
  // в стек всех соседей текущей вершины. i ходит по столбцам матрицы, каждый i - кандидат
  // в соседи. getWeight смотрит, есть ли ребро из текущей вершины в соседнюю.
  // Если есть, берем. +1, т.к. по заданию надо не с 0 хранить, а с 1
  public int[] depthFirstSearch(Graph graph, int startVertex) {
    Stack<Integer> stack = new Stack<>();
    int[] result = new int[graph.getSize()];
    boolean[] visited = new boolean[graph.getSize()];

    stack.push(startVertex);
    int cnt = 0;

    while (!stack.isEmpty()) {
      int currentVertex = stack.pop();
      if (visited[currentVertex - 1]) {
        continue;
      }

      visited[currentVertex - 1] = true;
      result[cnt++] = currentVertex;

      for (int i = graph.getSize() - 1; i >= 0; i--) {
        if (graph.getWeight(currentVertex - 1, i) != 0 && !visited[i]) {
          stack.push(i + 1);
        }
      }
    }
    return result;
  }

  // суть аналогична обходу в глубину. Просто тут очередь и достается из начала
  // (кто первый попал в очередь)
  // еще сначала смотрим всех соседей, а потом их соседей
  public int[] breadthFirstSearch(Graph graph, int startVertex) {
    Queue<Integer> queue = new Queue<>();
    int[] result = new int[graph.getSize()];
    boolean[] visited = new boolean[graph.getSize()];

    queue.push(startVertex);
    int cnt = 0;

    while (!queue.isEmpty()) {
      int currentVertex = queue.pop();
      if (visited[currentVertex - 1]) {
        continue;
      }

      visited[currentVertex - 1] = true;
      result[cnt++] = currentVertex;

      for (int i = 0; i <= graph.getSize() - 1; i++) {
        if (graph.getWeight(currentVertex - 1, i) != 0 && !visited[i]) {
          queue.push(i + 1);
        }
      }
    }
    return result;
  }

  /**
   * Алгоритм Дейкстры. Поиск кратчайшего пути между двумя вершинами в графе. Принимает на вход
   * номера двух вершин и возвращает численный результат, равный наименьшему расстоянию между ними,
   * или -1, если пути не существует.
   */
  public int getShortestPathBetweenVertices(Graph graph, int vertex1, int vertex2) {
    int n = graph.getSize();

    // Переводим внешние номера вершин (1..N) во внутренние индексы (0..N-1)
    int start = vertex1 - 1;
    int end = vertex2 - 1;

    if (start < 0 || start >= n || end < 0 || end >= n) {
      throw new IllegalArgumentException("Invalid vertex numbers: valid values are from 1 to " + n);
    }

    final int INF = Integer.MAX_VALUE;
    int[] dist = new int[n];
    boolean[] visited = new boolean[n];

    // Инициализация расстояний
    for (int i = 0; i < n; i++) {
      dist[i] = INF;
    }
    dist[start] = 0;

    // Основной цикл алгоритма: N раз выбираем ближайшую непосещённую вершину
    for (int step = 0; step < n; step++) {
      int u = -1;
      int bestDist = INF;

      // Ищем непосещённую вершину с минимальным расстоянием
      for (int i = 0; i < n; i++) {
        if (!visited[i] && dist[i] < bestDist) {
          bestDist = dist[i];
          u = i;
        }
      }

      // Если u == -1, значит, все оставшиеся вершины имеют расстояние INF (недостижимы из старта).
      // Если dist[u] == INF, то дальше нет пути.
      // Прерываем цикл — остальные вершины недостижимы.
      if (u == -1 || dist[u] == INF) {
        break;
      }

      visited[u] = true;

      // Улучшение расстояний до соседей вершины u
      for (int v = 0; v < n; v++) {
        int weight = graph.getWeight(u, v);
        if (weight != 0 && !visited[v]) {
          if (dist[u] + weight < dist[v]) {
            dist[v] = dist[u] + weight;
          }
        }
      }
    }

    // Если dist[end] равен INF, значит, пути нет — возвращаем -1
    return (dist[end] == INF) ? -1 : dist[end];
  }

  /**
   * Алгоритм Флойда-Уоршелла. Поиск кратчайших путей между всеми парами вершин в графе. Возвращает
   * матрицу кратчайших путей между всеми вершинами графа. Если пути между вершинами нет, путь будет
   * равен Integer.MAX_VALUE.
   */
  public int[][] getShortestPathsBetweenAllVertices(Graph graph) {
    int n = graph.getSize();
    final int INF = Integer.MAX_VALUE;

    int[][] dist = new int[n][n];

    // инициализация матрицы расстояний
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        int weight = graph.getWeight(i, j);
        if (i == j) {
          dist[i][j] = 0;
        } else {
          // Если ребра нет (вес 0), считаем расстояние бесконечным
          dist[i][j] = (weight != 0) ? weight : INF;
        }
      }
    }
    // классический алгоритм Флойда-Уоршелла
    for (int k = 0; k < n; k++) {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          // Пропускаем, если пути до промежуточной вершины или от неё нет
          if (dist[i][k] == INF || dist[k][j] == INF) {
            continue;
          }
          // Пытаемся улучшить путь из i в j через вершину k
          int newDist = dist[i][k] + dist[k][j];
          if (newDist < dist[i][j]) {
            dist[i][j] = newDist;
          }
        }
      }
    }

    return dist;
  }

  // суть в том, что из вершины каждый раз добавляется ближайшая вершина, которую еще не
  // брали.
  // веса задаются числами в графе, а индексы - это вершины
  public int[][] getLeastSpanningTree(Graph graph) {
    if (graph.isDirected()) {
      throw new IllegalArgumentException("Нельзя найти остовное дерево для ориентированного графа");
    }

    int n = graph.getSize();

    boolean[] visited = new boolean[n];
    int[] key = new int[n]; // самый короткий способ попасть в конкретную вершину
    int[] parent = new int[n]; // из какой вершины дерева этот вход

    for (int i = 0; i < n; i++) {
      key[i] = Integer.MAX_VALUE;
      parent[i] = -1;
      visited[i] = false;
    }
    key[0] = 0; // так как это старт

    for (int step = 0; step < n; step++) { // n раз берём по вершине
      int u = -1; // какую вершину сейчас добавим
      int best = Integer.MAX_VALUE;
      for (int i = 0; i < n; i++) {
        if (!visited[i] && key[i] < best) { // перебор вершин, которые еще не в остове
          best = key[i];
          u = i;
        }
      }

      visited[u] = true;
      // тут смотрим соседей вершины u
      for (int neighboor = 0; neighboor < n; neighboor++) {
        int w = graph.getWeight(u, neighboor);
        if (w != 0 && !visited[neighboor] && w < key[neighboor]) {
          key[neighboor] = w;
          parent[neighboor] = u;
        }
      }
    }

    // После цикла у каждой вершины, кроме корня, есть parent и key
    int[][] adjacencyMatrix = new int[n][n];
    for (int v = 0; v < n; v++) {
      int u = parent[v];
      if (u == -1) { // ребра в себя быть не должно, кроме старта
        continue;
      }
      // Ребро неориентированное и вес пишется в обе клетки
      adjacencyMatrix[u][v] = key[v];
      adjacencyMatrix[v][u] = key[v];
    }
    return adjacencyMatrix;
  }

  // выбираем начало для каждого муравья
  // каждый муравей будет иметь список табу (уже посещенные города)
  // муравей проходит по всем городам. Смотрим на каждого
  // этот алгоритм прогоняется снова и снова. Записываются минимальные пути
  public TsmResult solveTravelingSalesmanProblem(Graph graph) {
    TsmResult tsmResult = new TsmResult();
    tsmResult.distance = 0;
    double[][] pheromone = new double[graph.getSize()][graph.getSize()];
    for (int i = 0; i < graph.getSize(); i++) {
      for (int j = 0; j < graph.getSize(); j++) {
        pheromone[i][j] = 1.0; // стартовый запах одинаковый, иначе все желания = 0
      }
    }
    List<Integer> bestTour = null; // лучший найденный цикл
    double bestDistance = Double.POSITIVE_INFINITY;
    Random random = new Random(); // какой город выбрать, если несколько кандидатов

    double alpha = 1.0; // насколько важен феромон в формуле желания
    double beta = 2.0; // насколько важна короткость ребра (1/вес)
    double rho = 0.5; // доля испарения за итерацию
    double q = 5.0; // сколько запаха класть на рёбра тура
    int maxIterations = 200; // сколько раз прогоняем всех муравьёв

    for (int i = 0; i < maxIterations; i++) {
      List<List<Integer>> tours = new ArrayList<>();

      for (int ant = 0; ant < graph.getSize(); ant++) {
        List<Integer> tour = getOneWayForAnt(graph, pheromone, ant, random, alpha, beta);
        if (tour == null) {
          continue;
        }

        double len = tourLength(graph, tour);
        tours.add(tour);
        if (len < bestDistance) {
          bestDistance = len;
          bestTour = new ArrayList<>(tour);
        }
      }
      evaporate(pheromone, rho);
      for (List<Integer> tour : tours) {
        deposit(graph, pheromone, tour, q);
      }
    }

    if (bestTour == null) {
      return null;
    }
    return toTsmResult(bestTour, bestDistance);
  }

  private List<Integer> getOneWayForAnt(
      Graph graph, double[][] pheromone, int start, Random random, double alpha, double beta) {

    int n = graph.getSize();
    boolean[] visited = new boolean[n];
    List<Integer> tour = new ArrayList<>();
    int current = start;
    tour.add(current);
    visited[current] = true;
    while (tour.size() < n) {
      int next = chooseNext(graph, pheromone, current, visited, random, alpha, beta);
      if (next == -1) {
        return null;
      }
      tour.add(next);
      visited[next] = true;
      current = next;
    }
    if (graph.getWeight(current, start) == 0) {
      return null;
    }
    tour.add(start);
    return tour;
  }

  private int chooseNext(
      Graph graph,
      double[][] pheromone,
      int from,
      boolean[] visited,
      Random random,
      double alpha,
      double beta) {
    int n = graph.getSize();
    double[] desire = new double[n];
    double sum = 0.0;
    for (int j = 0; j < n; j++) {
      int weight = graph.getWeight(from, j);
      if (visited[j] || weight == 0) {
        continue;
      }
      desire[j] = Math.pow(pheromone[from][j], alpha) * Math.pow(1.0 / weight, beta);
      sum += desire[j];
    }
    if (sum == 0.0) {
      return -1;
    }
    double r = random.nextDouble() * sum;
    for (int j = 0; j < n; j++) {
      if (desire[j] == 0.0) {
        continue;
      }
      r -= desire[j];
      if (r <= 0.0) {
        return j;
      }
    }
    for (int j = n - 1; j >= 0; j--) {
      if (desire[j] > 0.0) {
        return j;
      }
    }
    return -1;
  }

  private double tourLength(Graph graph, List<Integer> tour) {
    double length = 0.0;
    for (int i = 0; i < tour.size() - 1; i++) {
      length += graph.getWeight(tour.get(i), tour.get(i + 1));
    }
    return length;
  }

  // испарить запах. Каждый круг остаётся половина старого запаха
  private void evaporate(double[][] pheromone, double rho) {
    for (int i = 0; i < pheromone.length; i++) {
      for (int j = 0; j < pheromone.length; j++) {
        pheromone[i][j] *= (1.0 - rho);
      }
    }
  }

  // нанести запах
  private void deposit(Graph graph, double[][] pheromone, List<Integer> tour, double q) {
    double add = q / tourLength(graph, tour);
    for (int i = 0; i < tour.size() - 1; i++) {
      int a = tour.get(i);
      int b = tour.get(i + 1);
      pheromone[a][b] += add;
      pheromone[b][a] += add;
    }
  }

  private TsmResult toTsmResult(List<Integer> tour, double distance) {
    int[] vertices = new int[tour.size()];
    for (int i = 0; i < tour.size(); i++) {
      vertices[i] = tour.get(i) + 1;
    }
    return new TsmResult(vertices, distance);
  }
}
