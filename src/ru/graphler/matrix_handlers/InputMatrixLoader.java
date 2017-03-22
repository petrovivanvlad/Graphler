package ru.graphler.matrix_handlers;

import ru.graphler.matrix_handlers.debug.MatrixPrint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Iwanp on 14.03.2017.
 */
public class InputMatrixLoader {

    private static String matrixPath = "res/custom_266nm_BaA.png";

    private static BufferedImage imageBI;
    private static int imageWidth = 0;
    private static int imageHeight = 0;

    public static int[][] imageMatrix;

    public static void load() {
        try {
            imageBI = ImageIO.read(Files.newInputStream(Paths.get(matrixPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageWidth = imageBI.getWidth();
        imageHeight = imageBI.getHeight();
        imageMatrix = new int[imageWidth][imageHeight];
        imageMatrix = populateMatrix(imageBI, imageMatrix, matrixPath);
        imageMatrix = MatrixPreprocessor.preprocess(imageMatrix);

        MatrixPrint.print(imageMatrix, "inputMatrix");
    }

    private static int[][] populateMatrix(BufferedImage tempImage, int[][] tempMatrix, String imageName) {
        Color c;
        System.out.println("Заполняем матрицу изображения " + imageName);
        for (int y = 0; y < tempImage.getHeight(); y++) {
            for (int x = 0; x < tempImage.getWidth(); x++) {
                c = new Color(tempImage.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                if (red < 240 || green < 240 || blue < 240) {
                    tempMatrix[x][y] = 1;
                } else {
                    tempMatrix[x][y] = 0;
                }
            }
        }
        return tempMatrix;
    }

}
