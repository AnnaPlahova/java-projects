package org.example.graphs.s21_stack;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StackTest {
  @Test
  void testPushPop() {
    Stack<Integer> s = new Stack<>();
    s.push(10);
    s.push(20);
    assertEquals(20, s.pop());
    assertEquals(10, s.pop());
    assertTrue(s.isEmpty());
  }

  @Test
  void testTopWithoutRemoving() {
    Stack<String> s = new Stack<>();
    s.push("first");
    s.push("second");
    assertEquals("second", s.top());
    assertEquals("second", s.top()); // top не удаляет
    assertEquals(2, s.size());
    assertEquals("second", s.pop());
    assertEquals("first", s.pop());
    assertTrue(s.isEmpty());
  }

  @Test
  void testPopEmpty() {
    Stack<Integer> s = new Stack<>();
    assertThrows(IllegalStateException.class, () -> s.pop());
  }

  @Test
  void testTopEmpty() {
    Stack<Integer> s = new Stack<>();
    assertThrows(IllegalStateException.class, () -> s.top());
  }
}
