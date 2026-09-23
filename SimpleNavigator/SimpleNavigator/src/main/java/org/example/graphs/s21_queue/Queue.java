package org.example.graphs.s21_queue;

import java.util.ArrayList;
import java.util.List;

public class Queue<T> {
  private final List<T> items;

  public Queue() {
    items = new ArrayList<>();
  }

  public static <T> Queue<T> queue() {
    return new Queue<>();
  }

  // Добавление элемента в конец
  public void push(T value) {
    items.add(value);
  }

  // Получение элемента из начала (индекс 0) с последующим удалением
  public T pop() {
    if (items.isEmpty()) {
      throw new IllegalStateException("Queue is empty");
    }
    return items.remove(0);
  }

  // Получение первого элемента без удаления из очереди
  public T front() {
    if (items.isEmpty()) {
      throw new IllegalStateException("Queue is empty");
    }
    return items.get(0);
  }

  // Получение последнего элемента без удаления из очереди
  public T back() {
    if (items.isEmpty()) {
      throw new IllegalStateException("Queue is empty");
    }
    return items.get(items.size() - 1);
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }
}
