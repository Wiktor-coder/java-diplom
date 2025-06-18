package ru.netology.graphics.image;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private int maxWidth = Integer.MAX_VALUE;
    private int maxHeight = Integer.MAX_VALUE;
    private double maxRatio = Double.MAX_VALUE;
    private TextColorSchema schema = new TextColorSchemaImpl();

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        // Проверяем соотношение сторон
        double currentRatio = (double) img.getWidth() / img.getHeight();
        if (currentRatio > maxRatio) {
            throw new BadImageSizeException(maxRatio, currentRatio);
        }

        // Масштабируем изображение
        int newWidth = Math.min(maxWidth, img.getWidth());
        int newHeight = Math.min(maxHeight, img.getHeight());
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        // Конвертируем изображение в черно-белый вид
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        // Получаем растр для анализа пикселей
        WritableRaster bwRaster = bwImg.getRaster();
        int[] pixel = new int[3]; // Массив для хранения RGB значений

        // Сборка текстовой графики
        StringBuilder textRepresentation = new StringBuilder();
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                int color = bwRaster.getPixel(x, y, pixel)[0];
                char c = schema.convert(color);
                textRepresentation.append(c).append(c); // Повторяем символ дважды для лучшего визуального эффекта
            }
            textRepresentation.append("\n"); // Переход на следующую строку
        }

        return textRepresentation.toString();
    }
}
