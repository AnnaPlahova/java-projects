package org.example.graphs.s21_graph_algorithms;

public class TsmResult {
  public int[] vertices; // массив с искомым маршрутом (с порядком обхода вершин)
  public double distance; // длина этого маршрута

  public TsmResult() {}

  public TsmResult(int[] vertices, double distance) {
    this.vertices = vertices;
    this.distance = distance;
  }
}
