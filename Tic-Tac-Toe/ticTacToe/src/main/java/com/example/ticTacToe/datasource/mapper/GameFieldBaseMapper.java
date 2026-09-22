package com.example.ticTacToe.datasource.mapper;

import com.example.ticTacToe.domain.model.GameFieldDto;

import static com.example.ticTacToe.domain.model.GameFieldDto.SIZE;

/**
 * Конвертация между Domain‑моделью и DTO.
 */
public class GameFieldBaseMapper {

    // Конвертирует доменное поле (GameFieldDto) в строку из 9 символов для БД
    public static String toStringDto(GameFieldDto gameFieldDto) {
        int[][] cells = gameFieldDto.getCells();
        StringBuilder sb = new StringBuilder(SIZE * SIZE);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int value = cells[i][j];
                sb.append(value);
            }
        }
        return sb.toString();
    }

    // Конвертирует строку из 9 символов в доменное поле (GameFieldDto)
    // 123456789
    public static GameFieldDto toDomain(String stringField) {
        char[] charArray = stringField.toCharArray();
        int k = 0;
        int[][] arr = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char c = charArray[k++];
                arr[i][j] = c - '0'; // Преобразуем символ в число: '1' → 1
            }
        }
        return new GameFieldDto(arr);
    }


}
