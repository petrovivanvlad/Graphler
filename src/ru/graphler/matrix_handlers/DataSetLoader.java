package ru.graphler.matrix_handlers;

import ru.graphler.matrix_handlers.debug.MatrixPrint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by Iwanp on 15.12.2016.
 */
public class DataSetLoader {

    private static BufferedImage imageBI;
    private static int imageWidth = 0;
    private static int imageHeight = 0;
    private static int[][] imageMatrix;

    public static ArrayList<String> dataSetMatrixNames = new ArrayList<String>();
    public static ArrayList<int[][]> dataSetMatrixContainer = new ArrayList<int[][]>();

    public static void load(String dataSetPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(dataSetPath))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    try {
                        imageBI = ImageIO.read(Files.newInputStream(Paths.get(String.valueOf(filePath))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageWidth = imageBI.getWidth();
                    imageHeight = imageBI.getHeight();
                    imageMatrix = new int[imageWidth][imageHeight];
                    imageMatrix = populateMatrix(imageBI, imageMatrix, String.valueOf(filePath));
                    imageMatrix = MatrixPreprocessor.preprocess(imageMatrix);
                    dataSetMatrixNames.add(String.valueOf(filePath));
                    dataSetMatrixContainer.add(imageMatrix);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Debug:
        for (int i = 0; i < dataSetMatrixContainer.size(); i++) {
            MatrixPrint.print(dataSetMatrixContainer.get(i), dataSetMatrixNames.get(i));
        }
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








































