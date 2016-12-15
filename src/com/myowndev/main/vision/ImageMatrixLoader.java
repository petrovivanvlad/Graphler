package com.myowndev.main.vision;

import com.myowndev.main.brain.storage.ImageContainer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Iwanp on 15.12.2016.
 */
public class ImageMatrixLoader {

    /*
    Структура матрицы изображения:
        x y color
        0 0 0 - белый пиксель
        0 0 1 - черный пиксель
     */

    public ImageMatrixLoader() {
        initImages();

        matrixPrint(ImageContainer.bact_BaA_266nm_matrix);
        matrixPrint(ImageContainer.bact_cereus_280nm_matrix);
    }

    private void initImages() {
        try {
            ImageContainer.bact_BaA_266nm_BI = ImageIO.read(Files.newInputStream(Paths.get(ImageContainer.bact_BaA_266nm_Path)));
            ImageContainer.bact_BaA_266nm_Width = ImageContainer.bact_BaA_266nm_BI.getWidth();
            ImageContainer.bact_BaA_266nm_Height = ImageContainer.bact_BaA_266nm_BI.getHeight();
            ImageContainer.bact_BaA_266nm_matrix = new int[ImageContainer.bact_BaA_266nm_Width][ImageContainer.bact_BaA_266nm_Height][2];
            ImageContainer.bact_BaA_266nm_matrix = populateMatrix(ImageContainer.bact_BaA_266nm_BI, ImageContainer.bact_BaA_266nm_matrix, ImageContainer.bact_BaA_266nm_Path);
            ImageContainer.bact_BaA_266nm_matrix = ImageMatrixPreProcess.normalizeMatrix(ImageContainer.bact_BaA_266nm_matrix);

            ImageContainer.bact_cereus_280nm_BI = ImageIO.read(Files.newInputStream(Paths.get(ImageContainer.bact_cereus_280nm_Path)));
            ImageContainer.bact_cereus_280nm_Width = ImageContainer.bact_cereus_280nm_BI.getWidth();
            ImageContainer.bact_cereus_280nm_Height = ImageContainer.bact_cereus_280nm_BI.getHeight();
            ImageContainer.bact_cereus_280nm_matrix = new int[ImageContainer.bact_cereus_280nm_Width][ImageContainer.bact_cereus_280nm_Height][2];
            ImageContainer.bact_cereus_280nm_matrix = populateMatrix(ImageContainer.bact_cereus_280nm_BI, ImageContainer.bact_cereus_280nm_matrix, ImageContainer.bact_cereus_280nm_Path);
            ImageContainer.bact_cereus_280nm_matrix = ImageMatrixPreProcess.normalizeMatrix(ImageContainer.bact_cereus_280nm_matrix);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[][][] populateMatrix(BufferedImage tempImage, int[][][] tempMatrix, String imageName) {
        Color c;
        System.out.println("Заполняем матрицу изображения " + imageName);
        for (int y = 0; y < tempImage.getHeight(); y++) {
            for (int x = 0; x < tempImage.getWidth(); x++) {
                c = new Color(tempImage.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                if (red < 240 || green < 240 || blue < 240) {
                    tempMatrix[x][y][0] = 1;
                } else {
                    tempMatrix[x][y][0] = 0;
                }
            }
        }
        return tempMatrix;
    }

    public static void matrixPrint(int[][][] tempMatrix) {
        System.out.println("Выводим матрицу: ");
        System.out.println("X = " + tempMatrix.length); // X
        System.out.println("Y = " + tempMatrix[0].length); // Y
        System.out.println("Z = " + tempMatrix[0][0].length); // Z
        //System.out.println(tempMatrix[0][0][0]);
        //System.out.println(tempMatrix[0][0][1]);

        for (int y = 0; y < tempMatrix[0].length; y++) {
            for (int x = 0; x < tempMatrix.length; x++) {
                System.out.print(tempMatrix[x][y][0]);
            }
            System.out.print("\n");
        }
    }
}
