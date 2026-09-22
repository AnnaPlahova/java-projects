package com.example.ticTacToe.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * DTO, который отправляется клиенту/получается от него.
 * Поле – 3×3 матрица, где 0 = пусто, 1 = X, 2 = O.
 */
public final class GameFieldWebDto {
    public static final int SIZE = 3;
    private final int[][] field;

    public GameFieldWebDto() {
        this.field = new int[SIZE][SIZE];
    }

    @JsonCreator // Указывает Jackson: используй этот конструктор
    public GameFieldWebDto(int[][] field) {
        this.field = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(field[i], 0, this.field[i], 0, SIZE);
        }
    }

    @JsonProperty("field")
    public int[][] getField() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(field[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    public void setCell(int row, int col, int symbol) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
        field[row][col] = symbol;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(field);
    }
}
