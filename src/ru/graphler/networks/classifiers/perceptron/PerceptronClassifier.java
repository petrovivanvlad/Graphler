package ru.graphler.networks.classifiers.perceptron;

import ru.graphler.matrix_handlers.DataSetLoader;
import ru.graphler.matrix_handlers.debug.MatrixPrint;

/**
 * Created by Iwanp on 16.12.2016.
 *
 * 1. Обучение: нахождение весов для каждого датасета.
 * 2. Сравнение входного массива графика с матрицами
 * весов каждого графика из датасетов пользовательской выборки;
 *
 */
public class PerceptronClassifier {

    private int[][] inputMatrix;
    private int[][] weightMatrix;
    private int[][] multipliedMatrix; // Отмасштабированные сигналы (inputMatrix * weightMatrix)
    private int sumOfMultipliedMatrixCoefficients; // Сумма коэффициентов похожих пикселей входной матрицы с матрицой весов

    public PerceptronClassifier(int[][] inputMatrix, int weightMatrixXBound, int weightMatrixYBound) {
        weightMatrix = new int[weightMatrixXBound][weightMatrixYBound];
        multipliedMatrix = new int[weightMatrixXBound][weightMatrixYBound];

        this.inputMatrix = new int[weightMatrixXBound][weightMatrixYBound];
        this.inputMatrix = inputMatrix;
    }

    private void multiplyInputAndWeights() {
        for (int x = 0; x < weightMatrix.length; x++) {
            for (int y = 0; y < weightMatrix[0].length; y++) {
                multipliedMatrix[x][y] = inputMatrix[x][y] * weightMatrix[x][y];
            }
        }
    }

    private void summarizeMultipliedMatrixCoefficients() {
        sumOfMultipliedMatrixCoefficients = 0;
        for (int x = 0; x < weightMatrix.length; x++) {
            for (int y = 0; y < weightMatrix[0].length; y++) {
                sumOfMultipliedMatrixCoefficients += multipliedMatrix[x][y];
            }
        }
    }

    public float getSimilarityPercent() {
        this.multiplyInputAndWeights();
        this.summarizeMultipliedMatrixCoefficients();

        float similarityPercent = ((float) sumOfMultipliedMatrixCoefficients / ((float) weightMatrix.length * (float) weightMatrix[0].length)) * 100.0f;

        return similarityPercent;
    }

    private void increaseWeights(int[][] inputMatrix) {
        for (int x = 0; x < weightMatrix.length; x++) {
            for (int y = 0; y < weightMatrix[0].length; y++) {
                this.weightMatrix[x][y] += inputMatrix[x][y];
            }
        }
    }

    private void decreaseWeights(int[][] inputMatrix) {
        for (int x = 0; x < weightMatrix.length; x++) {
            for (int y = 0; y < weightMatrix[0].length; y++) {
                this.weightMatrix[x][y] -= inputMatrix[x][y];
            }
        }
    }

    public void trainWeightsOnDataSet() {
        for (int i = 0; i < DataSetLoader.dataSetMatrixContainer.size(); i++) {
            increaseWeights(DataSetLoader.dataSetMatrixContainer.get(i));
        }
        MatrixPrint.print(weightMatrix, "weights");
    }

}
































