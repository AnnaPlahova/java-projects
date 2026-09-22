package com.example.ticTacToe.domain.model;

import java.util.Arrays;

/**
 * Представляет поле 3x3 в виде матрицы целых чисел.
 * 0 – пустая клетка,
 * 1 – X,
 * 2 – O.
 */
public final class GameFieldDto {

    public static final int SIZE = 3;
    private final int[][] field; // [row][col]

    /**
     * Создаёт пустое поле
     */
    public GameFieldDto() {
        this.field = new int[SIZE][SIZE];
    }

    /**
     * Копирует существующее поле (для неизменяемости)
     */
    public GameFieldDto(int[][] field) {
        this.field = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(field[i], 0, this.field[i], 0, SIZE);
        }
    }

    /**
     * Возвращает копию матрицы
     */
    public int[][] getCells() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(field[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    /**
     * Устанавливает символ в указанную клетку
     */
    public void setCell(int row, int col, int symbol) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Неверные координаты");
        }
        field[row][col] = symbol;
    }

    /**
     * Возвращает символ в указанной клетке
     */
    public int getCell(int row, int col) {
        return field[row][col];
    }

    @Override
    public String toString() {
        return Arrays.deepToString(field);
    }
}
