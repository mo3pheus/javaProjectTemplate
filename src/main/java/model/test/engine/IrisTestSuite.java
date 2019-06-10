package model.test.engine;

import h2o.ai.IrisInferenceEngine;
import h2o.ai.IrisModelUtil;
import hex.genmodel.easy.prediction.MultinomialModelPrediction;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class IrisTestSuite {

    public static final String IRIS_SCHEMA = "sepal_length,sepal_width,petal_length,petal_width,species";

    public static final String[] getIrisSchema() {
        return IRIS_SCHEMA.split(",");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello welcome to Iris h2o example");

        List<String> allSamples = Files.readAllLines(Paths.get("src/main/resources/testData/iris.csv"));
        List<String> subSample  = getRandomSubSamples(allSamples);

        IrisInferenceEngine irisInferenceEngine = new IrisInferenceEngine("src/main/resources/modelRepository/iris.zip", getIrisSchema());
        for (String s : subSample) {
            System.out.println("Inference Sample = " + s);
            MultinomialModelPrediction multinomialModelPrediction = irisInferenceEngine.predict(convertToDouble(s));
            System.out.println(" class Prob = " + IrisModelUtil.convertToString(multinomialModelPrediction.classProbabilities));
        }


    }

    public static List<String> getRandomSubSamples(List<String> data) {
        int size = data.size();
        size = (int) (size * 0.1d);

        List<String> subSampledData = new ArrayList<>();
        while (subSampledData.size() < size) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, data.size());
            subSampledData.add(data.get(randomIndex));
            data.remove(randomIndex);
        }
        return subSampledData;
    }

    public static Double[] convertToDouble(String data) {
        String[] dataParts  = data.split(",");
        Double[] dataDouble = new Double[dataParts.length - 1];

        for (int i = 0; i < dataDouble.length; i++) {
            dataDouble[i] = Double.parseDouble(dataParts[i]);
        }
        return dataDouble;
    }
}
