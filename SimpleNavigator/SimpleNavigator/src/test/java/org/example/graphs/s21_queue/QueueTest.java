package org.example.graphs.s21_queue;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QueueTest {

  @Test
  void testPushPopOrder() {
    Queue<Integer> q = Queue.queue();
    q.push(10);
    q.push(20);
    q.push(30);

    assertEquals(10, q.pop()); // FIFO: первый ушёл первым
    assertEquals(20, q.pop());
    assertEquals(30, q.pop());
    assertTrue(q.isEmpty());
  }

  @Test
  void testFrontWithoutRemoving() {
    Queue<String> q = new Queue<>();
    q.push("first");
    q.push("second");

    assertEquals("first", q.front());
    assertEquals("first", q.front()); // front не удаляет
    assertEquals("first", q.pop()); // а pop удаляет
  }

  @Test
  void testPopEmpty() {
    Queue<Integer> q = new Queue<>();
    assertThrows(IllegalStateException.class, () -> q.pop());
  }

  @Test
  void testFrontEmpty() {
    Queue<Integer> q = new Queue<>();
    assertThrows(IllegalStateException.class, () -> q.front());
  }

  @Test
  void testBackWithoutRemoving() {
    Queue<String> q = new Queue<>();
    q.push("first");
    q.push("second");

    assertEquals("second", q.back());
    assertEquals("second", q.back()); // back не удаляет
    assertEquals("first", q.front());
    assertEquals("first", q.pop());
    assertEquals("second", q.back()); // хвост тот же, пока очередь не опустела
    assertEquals("second", q.pop());
    assertTrue(q.isEmpty());
  }

  @Test
  void testBackEmpty() {
    Queue<Integer> q = new Queue<>();
    assertThrows(IllegalStateException.class, () -> q.back());
  }
}
