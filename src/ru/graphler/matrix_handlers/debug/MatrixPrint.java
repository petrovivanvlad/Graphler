package ru.graphler.matrix_handlers.debug;

/**
 * Created by Iwanp on 12.03.2017.
 */
public class MatrixPrint {

    public static void print(int[][] tempMatrix) {
        System.out.println("Выводим матрицу");
        //System.out.println("Colums: " + tempMatrix.length);
        System.out.println("Rows: " + tempMatrix[0].length);

        for (int y = 0; y < tempMatrix[0].length; y++) {
            for (int x = 0; x < tempMatrix.length; x++) {
                System.out.print(tempMatrix[x][y]);
            }
            System.out.print("\n");
        }
    }

    public static void print(int[][] tempMatrix, String matrixName) {
        System.out.println("Выводим матрицу " + matrixName + " : ");
        //System.out.println("Colums: " + tempMatrix.length);
        System.out.println("Rows: " + tempMatrix[0].length);

        for (int y = 0; y < tempMatrix[0].length; y++) {
            for (int x = 0; x < tempMatrix.length; x++) {
                System.out.print(tempMatrix[x][y]);
            }
            System.out.print("\n");
        }
    }

}

































