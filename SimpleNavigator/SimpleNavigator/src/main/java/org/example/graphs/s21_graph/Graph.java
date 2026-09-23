package org.example.graphs.s21_graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Graph {
  private int[][] adjacencyMatrix;
  private boolean isDirected;

  public Graph(int size, boolean isDirected) {
    this.isDirected = isDirected;
    this.adjacencyMatrix = new int[size][size];
    // по умолчанию 0 — нет ребра
  }

  public int getSize() {
    return adjacencyMatrix.length;
  }

  public boolean isDirected() {
    return isDirected;
  }

  public int getWeight(int u, int v) {
    return adjacencyMatrix[u][v];
  }

  public void setWeight(int u, int v, int weight) {
    adjacencyMatrix[u][v] = weight;
    if (!isDirected) {
      adjacencyMatrix[v][u] = weight;
    }
  }

  // загрузка из файла: первая строка — N и флаг ориентированности (0/1)
  // далее N строк по N чисел — матрица смежности
  public static Graph loadGraphFromFile(String filename) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String firstLine = br.readLine();
      if (firstLine == null) throw new IOException("Empty file");

      String[] parts = firstLine.trim().split("\\s+");
      int n = Integer.parseInt(parts[0]);
      boolean isDirected = (parts.length > 1) && Integer.parseInt(parts[1]) == 1;

      Graph g = new Graph(n, isDirected);

      for (int i = 0; i < n; i++) {
        String line = br.readLine();
        if (line == null) throw new IOException("Incomplete matrix");

        String[] row = line.trim().split("\\s+");

        for (int j = 0; j < n; j++) {
          int w = Integer.parseInt(row[j]);
          if (w != 0) {
            g.setWeight(i, j, w);
          }
        }
      }
      return g;
    }
  }

  // выводит матрицу в консоль (для проверки)
  public void printMatrix() {
    int n = getSize();
    System.out.println(
        "Матрица смежности (размер: " + n + ", ориентированный: " + isDirected + "):");
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        System.out.printf("%4d", adjacencyMatrix[i][j]);
      }
      System.out.println();
    }
  }

  // выгрузка в DOT
  public void exportGraphToDot(String filename) throws IOException {
    String graphType = isDirected ? "digraph" : "graph";
    String edgeOp = isDirected ? "->" : "--";
    try (PrintWriter out = new PrintWriter(filename)) {
      out.println(graphType + " graphname {");
      // объявляем вершины (1..N)
      for (int i = 1; i <= getSize(); i++) {
        out.println("    " + i + ";");
      }
      // выводим рёбра
      if (isDirected) {
        // Для ориентированного: перебираем все пары (u, v)
        for (int u = 1; u <= getSize(); u++) {
          for (int v = 1; v <= getSize(); v++) {
            // конвертируем 1-based в 0-based для доступа к матрице
            int w = getWeight(u - 1, v - 1);
            if (w != 0) {
              out.println("    " + u + " " + edgeOp + " " + v + ";");
            }
          }
        }
      } else {
        // Для неориентированного: петли (диагональ)
        for (int u = 1; u <= getSize(); u++) {
          int w = getWeight(u - 1, u - 1);
          if (w != 0) {
            out.println("    " + u + " " + edgeOp + " " + u + ";");
          }
        }
        // обычные рёбра - перебираем только верхний треугольник (u < v),
        // чтобы не продублировать каждое ребро дважды (1--2 и 2--1)
        for (int u = 1; u <= getSize(); u++) {
          for (int v = u + 1; v <= getSize(); v++) {
            // конвертируем 1-based в 0-based
            int w = getWeight(u - 1, v - 1);
            if (w != 0) {
              out.println("    " + u + " " + edgeOp + " " + v + ";");
            }
          }
        }
      }
      out.println("}");
    }
  }
}
