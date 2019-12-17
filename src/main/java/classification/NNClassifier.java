package classification;

import au.com.bytecode.opencsv.CSVReader;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
//import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.Normalizer;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializerStrategy;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NNClassifier {
    public static void main(String[] args) throws Exception {
        Logger log = LoggerFactory.getLogger(NNClassifier.class);
        // read the data into train and test sets and print it out.
        File irisDataFile = new File("/home/sanket/Documents/workspace/javaProjectTemplate/src/main/resources/data/IrisData/irisNew.txt");
        RecordReader recordReader = new CSVRecordReader(1, ',');
        recordReader.initialize(new FileSplit(irisDataFile));

        //Second: the RecordReaderDataSetIterator handles conversion to DataSet objects, ready for use in neural network
        int labelIndex = 4;     //5 values in each row of the iris.txt CSV: 4 input features followed by an integer label (class) index. Labels are the 5th value (index 4) in each row
        int numClasses = 3;     //3 classes (types of iris flowers) in the iris data set. Classes have integer values 0, 1 or 2
        int batchSize = 150;    //Iris data set: 150 examples total. We are loading all of them into one DataSet (not recommended for large data sets)

        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, 150, 4, 3);

        DataSet allData = iterator.next();

        String[] labels = {"Iris-setosa", "Iris-virginica", "Iris-versicolor"};
        allData.setLabelNames(Arrays.asList(labels));
        allData.shuffle();
        SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.6d);

        DataSet trainingData = testAndTrain.getTrain();
        DataSet testData = testAndTrain.getTest();

        DataSet testDataCopy = testData.copy();

        //We need to normalize our data. We'll use NormalizeStandardize (which gives us mean 0, unit variance):
        DataNormalization normalizer = new NormalizerStandardize();
        normalizer.fit(trainingData);           //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
        normalizer.transform(trainingData);     //Apply normalization to the training data
        normalizer.transform(testData);         //Apply normalization to the test data. This is using statistics calculated from the *training* set

        final int numInputs = 4;
        int outputNum = 3;
        long seed = 6;

        log.info("Build model....");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .activation(Activation.TANH)
                .weightInit(WeightInit.XAVIER)
                .updater(new Sgd(0.1))
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder().nIn(numInputs).nOut(3)
                        .build())
                .layer(new DenseLayer.Builder().nIn(3).nOut(3)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(3).nOut(outputNum).build())
                .build();

        //run the model
//        MultiLayerNetwork model = new MultiLayerNetwork(conf);
//        model.init();
//        model.setListeners(new ScoreIterationListener(100));

//        for (int i = 0; i < 1000; i++) {
//            model.fit(trainingData);
//        }

        MultiLayerNetwork model = MultiLayerNetwork.load(new File("modelSaved.txt"), false);

//        //evaluate the model on the test set
//        Evaluation eval = new Evaluation(3);
//        INDArray output = model.output(testData.getFeatures());
//        eval.eval(testData.getLabels(), output);
//        log.info(eval.stats());

        testData.setLabelNames(Arrays.asList(labels));
        System.out.println("=====================");
//        for (String s : model.predict(testData)) {
//            System.out.println(s);
//        }

        NormalizerSerializer normalizerSerializer = NormalizerSerializer.getDefault();
        normalizerSerializer.write(normalizer, "normalizerFile.txt");
        DataNormalization dataNormalizer = normalizerSerializer.restore("normalizerFile.txt");

//        List<org.nd4j.linalg.dataset.DataSet> testVectors = testData.asList();
//        for (org.nd4j.linalg.dataset.DataSet dataSet : testVectors) {
//            dataSet.setLabelNames(Arrays.asList(labels));
//            dataNormalizer.transform(dataSet);
//
//            System.out.println(dataSet.getFeatures());
//            System.out.println(model.predict(dataSet));
//        }

        List<DataSet> testVectors = testData.asList();
        List<DataSet> testVectorsCopy = testDataCopy.asList();
        for (int i = 0; i < testVectors.size(); i++) {
            System.out.println("**********************************************");
            System.out.println(" i = " + i);
            testVectors.get(i).setLabelNames(Arrays.asList(labels));
            System.out.println(testVectors.get(i));
            System.out.println(model.predict(testVectors.get(i)));
            System.out.println("==================");
            System.out.println(testVectorsCopy.get(i));
            System.out.println("==================");
            dataNormalizer.transform(testVectorsCopy.get(i));
            testVectorsCopy.get(i).setLabelNames(Arrays.asList(labels));
            System.out.println(testVectorsCopy.get(i));
            System.out.println(model.predict(testVectorsCopy.get(i)));
            System.out.println("**********************************************");
        }

        // Single sample classification.
        Double[][] sampleValues = {{5.9d, 3.0d, 5.1d, 1.8d}};
        INDArray sampleMatrix = Nd4j.createFromArray(sampleValues);
        dataNormalizer.transform(sampleMatrix);
        DataSet sampleDataset = new DataSet();
        sampleDataset.setFeatures(sampleMatrix);
        sampleDataset.setLabelNames(Arrays.asList(labels));
        List<String> classification = model.predict(sampleDataset);
        System.out.println(classification);

//
//        //5.9,3.0,5.1,1.8,2
//        Double[] sampleValues = {5.9d, 3.0d, 5.1d, 1.8d};
//
//        double[][] sample = new double[1][4];
//        for (int i = 0; i < 4; i++) {
//            sample[0][i] = sampleValues[i];
//        }
//
//        INDArray sampleMatrix = Nd4j.createFromArray(sample);
//        dataNormalizer.transform(sampleMatrix);
//
//        int[] predictions = model.predict(sampleMatrix);
//        for (int i = 0; i < predictions.length; i++) {
//            System.out.println(predictions[i]);
//        }
//
//        DataSet testSample = new org.nd4j.linalg.dataset.DataSet(sampleMatrix, testData.getLabels());
//        testSample.setLabelNames(Arrays.asList(labels));
//        System.out.println(model.predict(testSample));
    }
}
