package ru.graphler;

import ru.graphler.networks.classifiers.perceptron.PerceptronClassifier;
import ru.graphler.matrix_handlers.DataSetLoader;
import ru.graphler.matrix_handlers.InputMatrixLoader;

public class Main {

    private static String dataSetPath = "res/BaA";

    public static void main(String[] args) { new Main(); }

    public Main() {
        // 1. Загрузка графиков из БД согласно пользовательской выборке:
        DataSetLoader.load(dataSetPath);
        // 2. Получение входного графика:
        InputMatrixLoader.load();
        // 3. Предобучение сети для нахождения весов для каждого датасета:
        PerceptronClassifier NW1 = new PerceptronClassifier(InputMatrixLoader.imageMatrix, Settings.graphCols, Settings.graphRows);
        NW1.trainWeightsOnDataSet();
        // 4. Прохождение входной матрицы по нейросети (каждый нейрон со своим датасетом):
        System.out.println("Similarity is: " + NW1.getSimilarityPercent() + "%");

    }

}


















































