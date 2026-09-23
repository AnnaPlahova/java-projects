package org.example.graphs.s21_stack;

import java.util.ArrayList;
import java.util.List;

public class Stack<T> {
  private final List<T> items;

  public Stack() {
    items = new ArrayList<>();
  }

  // Создание пустого стека
  public static <T> Stack<T> stack() {
    return new Stack<>();
  }

  // Добавление элемента в стек
  public void push(T value) {
    items.add(value);
  }

  // Получение элемента из стека с его последующим удалением из стека
  public T pop() {
    if (items.isEmpty()) {
      throw new IllegalStateException("Stack is empty");
    }
    return items.remove(items.size() - 1);
  }

  // Получение элемента из стека без его удаления из стека
  public T top() {
    if (items.isEmpty()) {
      throw new IllegalStateException("Stack is empty");
    }
    return items.get(items.size() - 1);
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public int size() {
    return items.size();
  }
}
