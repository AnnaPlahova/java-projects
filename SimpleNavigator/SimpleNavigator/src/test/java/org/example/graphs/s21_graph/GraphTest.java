package org.example.graphs.s21_graph;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class GraphTest {
  // Вспомогательный метод: создать временный файл с содержимым.
  private Path createTestFileWithContent(String content) throws Exception {
    Path tempFile = Files.createTempFile("graph_test", ".txt");
    Files.writeString(tempFile, content);
    tempFile.toFile().deleteOnExit();
    return tempFile;
  }

  // Вспомогательный метод. Только создаёт и помечает на удаление, ничего не пишет
  private Path createEmptyTempFile(String prefix, String suffix) throws Exception {
    Path tempFile = Files.createTempFile(prefix, suffix);
    tempFile.toFile().deleteOnExit();
    return tempFile;
  }

  // Загружает тестовый файл, проверяет размер и тип графа.
  @Test
  void testLoadGraphFromFile() throws Exception {
    String content =
        """
            3 0
            0 3 0
            3 0 5
            0 5 0
            """;
    Path file = createTestFileWithContent(content);

    Graph g = Graph.loadGraphFromFile(file.toString());

    assertEquals(3, g.getSize());
    assertFalse(g.isDirected());
    assertEquals(3, g.getWeight(0, 1));
    assertEquals(3, g.getWeight(1, 0)); // симметрия
    assertEquals(5, g.getWeight(1, 2));
    assertEquals(5, g.getWeight(2, 1));
  }

  // Проверяет, что в неориентированном графе матрица симметрична.
  @Test
  void testSetWeight_Undirected() {
    Graph g = new Graph(3, false);
    g.setWeight(0, 1, 7);

    assertEquals(7, g.getWeight(0, 1));
    assertEquals(7, g.getWeight(1, 0)); // должно быть симметрично
  }

  // Проверяет, что в неориентированном графе рёбра не дублируются, а петли есть.
  @Test
  void testExportGraphToDot_Undirected_NoDuplicates() throws Exception {
    // Граф: 3 вершины, неориентированный, петля на 1, ребро 2--3
    Graph g = new Graph(3, false);
    g.setWeight(0, 0, 5); // петля
    g.setWeight(1, 2, 3); // ребро

    Path outFile = createEmptyTempFile("test_dot", ".dot");

    g.exportGraphToDot(outFile.toString());

    List<String> lines = Files.readAllLines(outFile);

    // Проверяем, что граф начинается правильно
    assertTrue(lines.get(0).startsWith("graph graphname {"));

    // Должны быть объявления вершин
    assertTrue(lines.contains("    1;"));
    assertTrue(lines.contains("    2;"));
    assertTrue(lines.contains("    3;"));

    // Петля должна быть
    assertTrue(lines.contains("    1 -- 1;"));

    // Ребро 2--3 должно быть ровно 1 раз (не должно быть 3--2)
    long edgeCount =
        lines.stream().filter(l -> l.contains("2 -- 3;") || l.contains("3 -- 2;")).count();
    assertEquals(1, edgeCount);

    // Не должно быть лишних рёбер
    assertFalse(lines.stream().anyMatch(l -> l.contains("1 -- 2;")));
    assertFalse(lines.stream().anyMatch(l -> l.contains("1 -- 3;")));
  }

  // Проверяет, что в ориентированном графе петли и разные направления рёбер сохраняются.
  @Test
  void testExportGraphToDot_Directed() throws Exception {
    // Ориентированный граф: петля на 1, рёбра 1->2 и 2->1 (разные!)
    Graph g = new Graph(2, true);
    g.setWeight(0, 0, 4); // петля
    g.setWeight(0, 1, 10); // 1 -> 2
    g.setWeight(1, 0, 20); // 2 -> 1

    Path outFile = createEmptyTempFile("test_dot_dir", ".dot");

    g.exportGraphToDot(outFile.toString());

    List<String> lines = Files.readAllLines(outFile);

    assertTrue(lines.get(0).startsWith("digraph graphname {"));
    assertTrue(lines.contains("    1 -> 1;")); // петля
    assertTrue(lines.contains("    1 -> 2;"));
    assertTrue(lines.contains("    2 -> 1;"));
  }
}
