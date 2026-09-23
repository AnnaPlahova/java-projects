package org.example.graphs.s21_cli;

import java.io.IOException;
import java.util.Scanner;
import org.example.graphs.s21_graph.Graph;
import org.example.graphs.s21_graph_algorithms.GraphAlgorithms;
import org.example.graphs.s21_graph_algorithms.TsmResult;

public class Main {
  private static final Scanner SCANNER = new Scanner(System.in);
  private static final GraphAlgorithms ALGORITHMS = new GraphAlgorithms();
  private static Graph graph;
  private static String loadedGraphPath;

  public static void main(String[] args) {
    boolean running = true;
    while (running) {
      printMenu();
      String choice = readLine("Выберите действие: ");
      try {
        switch (choice) {
          case "1":
            loadGraph();
            break;
          case "2":
            traverseBreadthFirst();
            break;
          case "3":
            traverseDepthFirst();
            break;
          case "4":
            findShortestPath();
            break;
          case "5":
            findAllShortestPaths();
            break;
          case "6":
            findLeastSpanningTree();
            break;
          case "7":
            solveTravelingSalesman();
            break;
          case "8":
            exportToDot();
            break;
          case "0":
            running = false;
            System.out.println("Выход.");
            break;
          default:
            System.out.println("Неизвестная команда. Введите число от 0 до 8.");
        }
      } catch (Exception e) {
        System.out.println("Ошибка: " + e.getMessage());
      }
      System.out.println();
    }
  }

  private static void printMenu() {
    System.out.println("МЕНЮ");
    System.out.println("1. Загрузить граф из файла");
    System.out.println("2. Обход графа в ширину");
    System.out.println("3. Обход графа в глубину");
    System.out.println("4. Кратчайший путь между двумя вершинами");
    System.out.println("5. Кратчайшие пути между всеми парами вершин");
    System.out.println("6. Минимальное остовное дерево");
    System.out.println("7. Задача коммивояжера");
    System.out.println("8. Экспорт в .dot");
    System.out.println("0. Выход");
  }

  private static void loadGraph() throws IOException {
    String filename = readLine("Путь к файлу: ");
    graph = Graph.loadGraphFromFile(filename);
    loadedGraphPath = filename;
    System.out.println("Граф успешно загружен.");
    graph.printMatrix();
  }

  private static void traverseBreadthFirst() {
    if (!requireGraph()) {
      return;
    }
    int startVertex = readVertex("Стартовая вершина: ");
    int[] result = ALGORITHMS.breadthFirstSearch(graph, startVertex);
    System.out.print("Обход в ширину: ");
    printVertices(result);
  }

  private static void traverseDepthFirst() {
    if (!requireGraph()) {
      return;
    }
    int startVertex = readVertex("Стартовая вершина: ");
    int[] result = ALGORITHMS.depthFirstSearch(graph, startVertex);
    System.out.print("Обход в глубину: ");
    printVertices(result);
  }

  private static void findShortestPath() {
    if (!requireGraph()) {
      return;
    }
    int vertex1 = readVertex("Первая вершина: ");
    int vertex2 = readVertex("Вторая вершина: ");
    int distance = ALGORITHMS.getShortestPathBetweenVertices(graph, vertex1, vertex2);
    if (distance == -1) {
      System.out.println("Нет пути между вершинами");
      return;
    }
    System.out.println("Кратчайшее расстояние: " + distance);
  }

  private static void findAllShortestPaths() {
    if (!requireGraph()) {
      return;
    }
    int[][] distances = ALGORITHMS.getShortestPathsBetweenAllVertices(graph);
    System.out.println("Матрица кратчайших путей:");
    printMatrix(distances);
  }

  private static void findLeastSpanningTree() {
    if (!requireGraph()) {
      return;
    }

    if (graph.isDirected()) {
      System.out.println("Нельзя найти остовное дерево для ориентированного графа");
      return;
    }

    int[][] tree = ALGORITHMS.getLeastSpanningTree(graph);
    System.out.println("Матрица смежности минимального остовного дерева:");
    printMatrix(tree);
  }

  private static void solveTravelingSalesman() {
    if (!requireGraph()) {
      return;
    }
    TsmResult result = ALGORITHMS.solveTravelingSalesmanProblem(graph);
    if (result == null || result.vertices == null) {
      System.out.println("Невозможно решить задачу коммивояжера для данного графа.");
      return;
    }
    System.out.print("Маршрут: ");
    printRoute(result.vertices);
    System.out.println("Длина маршрута: " + result.distance);
  }

  private static void exportToDot() throws IOException {
    if (!requireGraph()) {
      return;
    }
    String filename = toDotFilename(loadedGraphPath);
    graph.exportGraphToDot(filename);
    System.out.println("Загруженный граф экспортирован в " + filename);
  }

  private static String toDotFilename(String sourcePath) {
    if (sourcePath == null || sourcePath.isBlank()) {
      return "graph.dot";
    }
    int separator = Math.max(sourcePath.lastIndexOf('/'), sourcePath.lastIndexOf('\\'));
    int dot = sourcePath.lastIndexOf('.');
    if (dot > separator) {
      return sourcePath.substring(0, dot) + ".dot";
    }
    return sourcePath + ".dot";
  }

  private static boolean requireGraph() {
    if (graph == null) {
      System.out.println("Сначала загрузите граф (пункт 1).");
      return false;
    }
    return true;
  }

  private static int readVertex(String prompt) {
    int vertex = readInt(prompt);
    int size = graph.getSize();
    if (vertex < 1 || vertex > size) {
      throw new IllegalArgumentException("Номер вершины должен быть в диапазоне от 1 до " + size);
    }
    return vertex;
  }

  private static int readInt(String prompt) {
    String line = readLine(prompt);
    try {
      return Integer.parseInt(line);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Ожидалось целое число, получено: " + line);
    }
  }

  private static String readLine(String prompt) {
    System.out.print(prompt);
    if (!SCANNER.hasNextLine()) {
      throw new IllegalStateException("Ввод завершён.");
    }
    return SCANNER.nextLine().trim();
  }

  private static void printVertices(int[] vertices) {
    if (vertices == null || vertices.length == 0) {
      System.out.println("(пусто)");
      return;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < vertices.length; i++) {
      if (vertices[i] == 0) {
        continue;
      }

      if (sb.length() > 0) {
        sb.append(' ');
      }
      sb.append(vertices[i]);
    }

    if (sb.length() == 0) {
      System.out.println("(пусто)");
      return;
    }

    System.out.println(sb);
  }

  private static void printRoute(int[] vertices) {
    if (vertices == null || vertices.length == 0) {
      System.out.println("(пусто)");
      return;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < vertices.length; i++) {
      if (i > 0) {
        sb.append(" -> ");
      }
      sb.append(vertices[i]);
    }
    System.out.println(sb);
  }

  private static void printMatrix(int[][] matrix) {
    if (matrix == null) {
      System.out.println("(пусто)");
      return;
    }
    for (int[] row : matrix) {
      for (int value : row) {
        if (value == Integer.MAX_VALUE) {
          System.out.printf("%6s", "inf");
        } else {
          System.out.printf("%6d", value);
        }
      }
      System.out.println();
    }
  }
}
