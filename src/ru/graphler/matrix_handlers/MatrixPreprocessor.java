package ru.graphler.matrix_handlers;

import ru.graphler.Settings;

/**
 * Created by Iwanp on 15.12.2016.
 *
 * Алгоритмы нормализации графиков и приведения к эталонному
 * виду согласно размерам весов нейронной сети (40х10).
 *
 */
public class MatrixPreprocessor {

    public static int[][] preprocess(int[][] tempMatrix) {
        // Убираем шкалы по краям (оси абсцисс и ординат с подписями):
        tempMatrix = removeCoordsLines(tempMatrix);
        // Убираем белое пространство вокруг линии графика:
        tempMatrix = removeWhiteSpaces(tempMatrix);
        // Скейлим матрицу, чтобы достичь универсального размера по датасету ([40][10]):
        tempMatrix = matrixScale(tempMatrix);
        // Восстанавливаем пропущенные промежутки линии спектра методом линейной аппроксимации:
        tempMatrix = linearApproximation(tempMatrix);
        // Делаем линию тоньще, чтобы вокруг каждого закрашенного пикселя был только один закрашенный пиксель вокруг:
        tempMatrix = makeLineThinner(tempMatrix);

        return tempMatrix;
    }

    private static int[][] makeLineThinner(int[][] tempMatrix) {
        return tempMatrix;
    }

    private static int[][] matrixScale(int[][] tempMatrix) {
        // TODO: переделать, чтобы не обрезало/увеличивало, а скейлило.
        int[][] newMatrix = new int[Settings.graphRows][Settings.graphCols];
        if (tempMatrix.length <= Settings.graphCols || tempMatrix[0].length <= Settings.graphRows) {
            newMatrix = reInitMatrixWithNewIncreasedBounds(
                    tempMatrix, Settings.graphCols, Settings.graphRows);
        } else {
            newMatrix = reInitMatrixWithNewCroppedBounds(
                    tempMatrix,
                    0,
                    Settings.graphCols,
                    0,
                    Settings.graphRows);
        } // TODO: Добавить еще условий

        return newMatrix;
    }

    private static int[][] linearApproximation(int[][] tempMatrix) {
        // TODO: доделать, чтоб линейно было
        int filledYLeftIndex = 0;
        int filledYRightIndex = 0;
        for (int x = 0; x < tempMatrix.length; x++) {
            int yIterator = 0;
            boolean emptyColumn = true;
            do {
                if (tempMatrix[x][yIterator] == 1) {
                    filledYLeftIndex = yIterator;
                    emptyColumn = false;
                    break;
                }
                yIterator++;
            } while (yIterator < tempMatrix[0].length);
            if (emptyColumn) {
                tempMatrix[x][filledYLeftIndex] = 1;
            }
        }
        return tempMatrix;
    }

    private static int[][] removeWhiteSpaces(int[][] tempMatrix) {
        int leftXCornerValue = 0;
        int rightXCornerValue = tempMatrix.length;
        int topYCornerValue = 0;
        int bottomYCornerValue = tempMatrix[0].length;
        // Вычисляем края прямоугольника:
        int y = 0;
        int x = 0;
        boolean check = true;
        for (int i = 0; i < tempMatrix.length; i++)
            if (tempMatrix[i][0] != 0)
                check = false;
        if (check)
            do {
                for (x = 0; x < tempMatrix.length; x++)
                    if (tempMatrix[x][y] == 1) {
                        topYCornerValue = y;
                        break;
                    }
                y++;
            } while (topYCornerValue == 0);
        check = true;
        x = 0;
        for (int i = 0; i < tempMatrix[0].length; i++)
            if (tempMatrix[0][i] != 0)
                check = false;
        if (check) {
            do {
                for (y = 0; y < tempMatrix[0].length; y++) {
                    if (tempMatrix[x][y] == 1) {
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
            if (tempMatrix[tempMatrix.length - 1][i] != 0)
                check = false;
        if (check) {
            do {
                for (y = 0; y < tempMatrix[0].length; y++) {
                    if (tempMatrix[x][y] == 1) {
                        rightXCornerValue = x + 1;
                        break;
                    }
                }
                x--;
            } while (rightXCornerValue == tempMatrix.length);
        }
        check = true;
        y = tempMatrix[0].length - 1;
        for (int i = 0; i < tempMatrix.length; i++)
            if (tempMatrix[i][tempMatrix[0].length - 1] != 0)
                check = false;
        if (check) {
            do {
                for (x = 0; x < tempMatrix.length; x++) {
                    if (tempMatrix[x][y] == 1) {
                        bottomYCornerValue = y + 1;
                        break;
                    }
                }
                y--;
            } while (bottomYCornerValue == tempMatrix[0].length);
        }
        tempMatrix = reInitMatrixWithNewCroppedBounds(
                tempMatrix,
                leftXCornerValue,
                rightXCornerValue,
                topYCornerValue,
                bottomYCornerValue);
        return tempMatrix;
    }

    private static int[][] removeCoordsLines(int[][] tempMatrix) {
        // 1. Убираем линию оси Y:
        int lineRowValue = 12; // Число продолжительности линии, чтобы быть уверенным, что это точно линия координат.
        int lineCounter = 0;
        int bottomBoundValue = 0;
        int y = (tempMatrix[0].length - (int)(tempMatrix[0].length * 25 / 100)); // Предположительно, линия находится в области 25% от нижнего края.
        do {
            for (int x = 0; x < tempMatrix.length; x++) {
                if (tempMatrix[x][y] == 1) {
                    lineCounter++;
                } else {
                    lineCounter = 0;
                }
                if (lineCounter == lineRowValue) { //  && lineCounter != 0
                    bottomBoundValue = y;
                    break;
                }
            }
            y++;
        } while (y < tempMatrix[0].length);
        // 2. Убираем линию оси X:
        lineCounter = 0;
        int leftBoundValue = 0;
        int x = (int)(tempMatrix.length * 25 / 100);
        do {
            for (y = 0; y < tempMatrix[0].length; y++) {
                if (tempMatrix[x][y] == 1) {
                    lineCounter++;
                } else {
                    lineCounter = 0;
                }
                if (lineCounter == lineRowValue) {
                    leftBoundValue = x + 1; // TODO: Проверить на единичку
                    break;
                }
            }
            x--;
        } while (x > 0);

        if (bottomBoundValue == 0) {
            tempMatrix = reInitMatrixWithNewCroppedBounds(
                    tempMatrix,
                    leftBoundValue,
                    tempMatrix.length,
                    0,
                    tempMatrix[0].length);
        } else {
            tempMatrix = reInitMatrixWithNewCroppedBounds(
                    tempMatrix,
                    leftBoundValue,
                    tempMatrix.length,
                    0,
                    bottomBoundValue);
        }
        return tempMatrix;
    }

    private static int[][] reInitMatrixWithNewCroppedBounds(
            int[][] oldMatrix, int leftBound, int rightBound, int upperBound, int bottomBound) {

        int newColCount = rightBound - leftBound ;
        int newRowCount = bottomBound - upperBound;
        int[][] newMatrix = new int[newColCount][newRowCount];

        int i = 0;
        int j = 0;
        for (int y = upperBound; y < bottomBound; y++) {
            for (int x = leftBound; x < rightBound; x++) {
                newMatrix[i][j] = oldMatrix[x][y];
                i++;
            }
            j++;
            i = 0;
        }
        return newMatrix;
    }

    private static int[][] reInitMatrixWithNewIncreasedBounds(
            int[][] oldMatrix, int newColCount, int newRowCount) {

        int[][] newMatrix = new int[newColCount][newRowCount];
        if (newColCount > oldMatrix.length || newRowCount > oldMatrix[0].length) {
            int i = 0;
            int j = 0;
            for (int y = 0; y < newRowCount; y++) {
                for (int x = 0; x < newColCount; x++) {
                    if (i < oldMatrix.length && j < oldMatrix[0].length) {
                        newMatrix[x][y] = oldMatrix[i][j];
                    } else {
                        newMatrix[x][y] = 0;
                    }
                    i++;
                }
                j++;
                i = 0;
            }
        } else {
            return oldMatrix; // just in case
        }

        return newMatrix;
    }
}













































