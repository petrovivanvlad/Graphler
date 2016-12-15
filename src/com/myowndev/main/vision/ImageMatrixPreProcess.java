package com.myowndev.main.vision;

/**
 * Created by Iwanp on 15.12.2016.
 */
public class ImageMatrixPreProcess {

    /* Нормализованная матрица кривой спектра -
         массив последовательных значений с одним закрашенным
         пикселем в каждом столбце по вертикали.
     */

    public static int[][][] normalizeMatrix(int[][][] tempMatrix) {
        // Убираем шкалы по краям (оси абсцисс и ординат с подписями):
        tempMatrix = removeCoordsLines(tempMatrix);
        // Убираем белое пространство вокруг линии спектра:
        tempMatrix = removeWhiteSpaces(tempMatrix);
        // Восстанавливаем пропущенные промежутки линии спектра:

        // Делаем линию тоньще, чтобы вокруг каждого закрашенного пикселя был только один закрашенный пиксель вокруг:

        return tempMatrix;
    }
    private int[][][] do_KalmanFilter(int[][][] tempMatrix) { // https://habrahabr.ru/post/166693/
        return tempMatrix;
    }
    private static int[][][] removeWhiteSpaces(int[][][] tempMatrix) {
        int leftXCornerValue = 0;
        int rightXCornerValue = tempMatrix.length;
        int topYCornerValue = 0;
        int bottomYCornerValue = tempMatrix[0].length;
        // Вычисляем края прямоугольника:
        int y = 0;
        do {
            for (int x = 0; x < tempMatrix.length; x++) {
                if (tempMatrix[x][y][0] == 1) {
                    topYCornerValue = y;
                }
            }
            y++;
        } while (topYCornerValue == 0);
        System.out.println(topYCornerValue);

        tempMatrix = reInitWithNewBoundsMatrix(
                tempMatrix, leftXCornerValue, rightXCornerValue, topYCornerValue, bottomYCornerValue);
        return tempMatrix;
    }
    private static int[][][] removeCoordsLines(int[][][] tempMatrix) {
        // 1. Убираем линию оси X:
        int lineRowValue = 20; // Число, чтобы быть уверенным, что это точно линия
        int lineCounter = 0;
        int bottomYCornerValue = 0;
        int y = (tempMatrix[0].length - (int)(tempMatrix[0].length * 25 / 100)); // Предположительно, линия находится в области 25% от нижнего края.
        do {
            for (int x = 0; x < tempMatrix.length; x++) {
                if (tempMatrix[x][y][0] == 1) {
                    lineCounter++;
                } else {
                    lineCounter = 0;
                }
                if (lineCounter == lineRowValue) {
                    bottomYCornerValue = y;
                }
            }
            y++;
        } while (y < tempMatrix[0].length);
        tempMatrix = reInitWithNewBoundsMatrix(
                tempMatrix, 0, tempMatrix.length, 0, bottomYCornerValue);
        // 2. Убираем линию оси Y:
        // нету
        return tempMatrix;
    }

    private static int[][][] reInitWithNewBoundsMatrix(
            int[][][] oldMatrix,
            int leftXCornerValue,
            int rightXCornerValue,
            int topYCornerValue,
            int bottomYCornerValue) { // (reinit an array)
        int[][][] newMatrix = new int[rightXCornerValue - leftXCornerValue][bottomYCornerValue - topYCornerValue][2];
        int i = 0;
        int j = 0;
        for (int y = topYCornerValue; y < bottomYCornerValue; y++) {
            for (int x = leftXCornerValue; x < rightXCornerValue; x++) {
                newMatrix[i][j][0] = oldMatrix[x][y][0];
                //System.out.print(newMatrix[i][j][0] + "-" + oldMatrix[x][y][0] + " ");
                //System.out.print(newMatrix[i][j][0] + "(" + i + "," + j + ") ");
                if (i < (rightXCornerValue - leftXCornerValue) - 1) {
                    i++;
                }
            }
            if (j < (bottomYCornerValue - topYCornerValue) - 1) {
                j++;
                i = 0;
            }
            //System.out.println();
        }
        /*for (int k = 0; k < i; k++) {
            for (int l = 0; l < j; l++) {
                System.out.print(newMatrix[k][l][0]);
            }
        }*/
        return newMatrix;
    }

}
