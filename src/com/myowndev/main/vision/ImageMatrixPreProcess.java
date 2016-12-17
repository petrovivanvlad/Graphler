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
        // TODO: можно не делать
        // Делаем линию тоньще, чтобы вокруг каждого закрашенного пикселя был только один закрашенный пиксель вокруг:
        // TODO: можно сделать
        return tempMatrix;
    }
    private int[][][] makeLineThinner(int[][][] tempMatrix) {
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
        int x = 0;
        boolean check = true;

        for (int i = 0; i < tempMatrix.length; i++)
            if (tempMatrix[i][0][0] != 0)
                check = false;
        if (check)
            do {
                for (x = 0; x < tempMatrix.length; x++)
                    if (tempMatrix[x][y][0] == 1) {
                        topYCornerValue = y;
                        break;
                    }
                y++;
            } while (topYCornerValue == 0);
        check = true;
        x = 0;
        for (int i = 0; i < tempMatrix[0].length; i++)
            if (tempMatrix[0][i][0] != 0)
                check = false;
        if (check) {
            do {
                for (y = 0; y < tempMatrix[0].length; y++) {
                    if (tempMatrix[x][y][0] == 1) {
                        leftXCornerValue = x;
                        break;
                    }
                }
                x++;
            } while (leftXCornerValue == 0);
        }
        check = true;
        x = tempMatrix.length - 1;
        for (int i = 0; i < tempMatrix[0].length; i++)
            if (tempMatrix[tempMatrix.length - 1][i][0] != 0)
                check = false;
        if (check) {
            do {
                for (y = 0; y < tempMatrix[0].length; y++) {
                    if (tempMatrix[x][y][0] == 1) {
                        rightXCornerValue = x;
                        break;
                    }
                }
                x--;
            } while (rightXCornerValue == tempMatrix.length);
        }
        check = true;
        y = tempMatrix[0].length - 1;
        for (int i = 0; i < tempMatrix.length; i++)
            if (tempMatrix[i][tempMatrix[0].length - 1][0] != 0)
                check = false;
        if (check) {
            do {
                for (x = 0; x < tempMatrix.length; x++) {
                    if (tempMatrix[x][y][0] == 1) {
                        bottomYCornerValue = y;
                        break;
                    }
                }
                y--;
            } while (bottomYCornerValue == tempMatrix[0].length);
        }
        tempMatrix = reInitWithNewBoundsMatrix(
                tempMatrix,
                leftXCornerValue,
                rightXCornerValue,
                topYCornerValue,
                bottomYCornerValue);
        return tempMatrix;
    }
    private static int[][][] removeCoordsLines(int[][][] tempMatrix) {
        // 1. Убираем линию оси X:
        int lineRowValue = 12; // Число, чтобы быть уверенным, что это точно линия
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
                    break;
                }
            }
            y++;
        } while (y < tempMatrix[0].length);
        // 2. Убираем линию оси Y:
        lineCounter = 0;
        int leftXCornerValue = 0;
        int x = (int)(tempMatrix.length * 25 / 100);
        do {
            for (y = 0; y < tempMatrix[0].length; y++) {
                if (tempMatrix[x][y][0] == 1) {
                    lineCounter++;
                } else {
                    lineCounter = 0;
                }
                if (lineCounter == lineRowValue) {
                    leftXCornerValue = x + 1; // Проверить на единичку
                    break;
                }
            }
            x--;
        } while (x > 0);
        tempMatrix = reInitWithNewBoundsMatrix(
                tempMatrix,
                leftXCornerValue,
                tempMatrix.length,
                0,
                bottomYCornerValue);
        return tempMatrix;
    }
    private static int[][][] reInitWithNewBoundsMatrix(
            int[][][] oldMatrix,
            int leftXCornerValue,
            int rightXCornerValue,
            int topYCornerValue,
            int bottomYCornerValue) {
        int[][][] newMatrix = new int[rightXCornerValue - leftXCornerValue][bottomYCornerValue - topYCornerValue][2];
        int i = 0;
        int j = 0;
        for (int y = topYCornerValue; y < bottomYCornerValue; y++) {
            for (int x = leftXCornerValue; x < rightXCornerValue; x++) {
                newMatrix[i][j][0] = oldMatrix[x][y][0];
                i++;
            }
            j++;
            i = 0;
        }
        return newMatrix;
    }

}
