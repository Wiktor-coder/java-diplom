package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {
    private static final char[] SYMBOLS = {'#', '$', '@', '%', '*', '+', '-', '.'};

    @Override
    public char convert(int color) {
        // Проверяем, что цвет находится в допустимом диапазоне
        if (color < 0 || color > 255) {
            throw new IllegalArgumentException("Цвет должен быть в диапазоне от 0 до 255");
        }

        // Вычисляем индекс символа на основе яркости
        int index = (int) Math.floor((double) color / 255 * (SYMBOLS.length - 1));
        return SYMBOLS[index];
    }
}
