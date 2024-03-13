package owlapi.tutorial;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
//import org.deeplearning4j.examples.utils.DownloaderUtility;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/* EXAMPLE
 * MushroomClassifier mushroomClassifier = new MushroomClassifier();
 * mushroomClassifier.initialize();
 * mushroomClassifier.fit(
 *      "agaricus-lepiota.csv",  // String filepath 
 *      0,                       // int labelIndex 
 *      true,                    // boolean fitNormalizer
 *      32,                      // int batchSize
 *      10000,                   // int iterations
 *      true,                    // boolean shuffleData
 *      0.8,                     // double trainTestSplitRatio
 *      100                      // int recordScoreFrequency
 * );
 * 
 * Evaluation eval = mushroomClassifier.evaluate();
 */
public class MushroomClassifier {
    private static Logger log = LoggerFactory.getLogger(IrisClassifier.class);

    public static class DataSetCache {
        public final DataSet trainData;
        public final DataSet testData;

        public DataSetCache(DataSet trainData, DataSet testData) {
            this.trainData = trainData;
            this.testData = testData;
        }
    }

    // constants
    private static final int NUM_INPUTS = 22, NUM_OUTPUTS = 1;
    private static final int NUM_CLASSES = 2;
    private static final long DEFAULT_SEED = 6;

    // Data
    private RecordReader recordReader;
    private final long seed;
    private DataSetCache dataSetCache;
    private final DataNormalization normalizer = new NormalizerStandardize(); // standardizer utility provided for data

    // Model
    private final MultiLayerNetwork model;
    private final MultiLayerConfiguration config;

    public MushroomClassifier(long seedValue) {
        seed = seedValue;

        //First: get the dataset using the record reader. CSVRecordReader handles loading/parsing
        //int numLinesToSkip = 0;
        //char delimiter = ',';
        recordReader = new CSVRecordReader(); /*numLinesToSkip,delimiter*/

        double learningRate = 0.01;

        log.info("Build model....");

        //TODO: update model architecture
        config = new NeuralNetConfiguration.Builder()
            .seed(seed)
            //.activation(Activation.RELU)
            .weightInit(WeightInit.RELU)
            .updater(new Sgd(learningRate))
            //.l2(1e-4)
            .list()
            .layer(new DenseLayer.Builder().nIn(NUM_INPUTS).nOut(512)
                .activation(Activation.RELU)
                .build())
            .layer(new DenseLayer.Builder().nIn(512).nOut(256)
                .activation(Activation.RELU)
                .build())
            .layer(new DenseLayer.Builder().nIn(256).nOut(128)
                .activation(Activation.RELU)
                .build())
            .layer(new DenseLayer.Builder().nIn(128).nOut(32)
                .activation(Activation.RELU)
                .build())
            .layer( new OutputLayer.Builder(LossFunction.XENT) // Binary Crossentropy
                .activation(Activation.SIGMOID) //Override the global TANH activation with sigmoid for this layer
                .nIn(32).nOut(NUM_OUTPUTS).build())
            .build();
        
        model = new MultiLayerNetwork(config);
    }

    public MushroomClassifier() {
        this(DEFAULT_SEED);
    }

    public void initialize() {
        model.init();
    }

    // ASSUMES initialize() was called beforehand
    // The normalizer will still be used regardless, but you have the option to re-fit it
    public void fit(String filepath, int labelIndex, boolean fitNormalizer, int batchSize, int iterations, boolean shuffleData, double trainTestSplitRatio, int recordScoreFrequency) {
        //TODO: load dataset with `filePath` and `recordReader`
        recordReader.initialize(new FileSplit(new File(filepath)));
        
        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, NUM_CLASSES);
        DataSet allData = iterator.next();

        if (shuffleData) {
            allData.shuffle();
        }

        // Train test split
        DataSet trainingData = null;
        if (trainTestSplit > 0.0) {
            SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(trainTestSplitRatio);  // Use trainTestSplitRatio% of data for training

            DataSet trainData = testAndTrain.getTrain();
            DataSet testData = testAndTrain.getTest();

            if (fitNormalizer) {
                normalizer.fit(trainData);
            }

            normalizer.transform(trainData);
            normalizer.transform(testData);

            // cache these values
            dataSetCache = new DataSetCache(trainData, testData);

            // set trainingData
            trainingData = trainData;
        } else {
            normalizer.transform(allData);

            // set trainingData
            trainingData = allData;
        }

        // Record score
        if (recordScoreFrequency > 0) {
            model.setListeners(new ScoreIterationListener(recordScoreFrequency));
        }

        // Fit the model
        for (int i = 0; i < iterations; ++i) {
            model.fit(trainingData);
        }
    }

    public Evaluation evaluate(DataSet testData) {
        Evaluation eval = new Evaluation(NUM_OUTPUTS);
        INDArray output = model.output(testData.getFeatures());
        
        // evaluate it
        eval.eval(testData.getLabels(), output);
        log.info(eval.stats());

        return eval;
    }

    public Evaluation evaluate() {
        return evaluate(dataSetCache.testData);
    }

    public void fitTransformData(DataSet data) {
        normalizer.fit(data);
        normalizer.transform(data);
    }

    public MultiLayerNetwork getModel() { 
        return model; 
    }

    public MultiLayerConfiguration getConfiguration() { 
        return config; 
    }

    public MushroomClassifier.DataSetCache getDataSetCache() { 
        return dataSetCache; 
    }

    public DataNormalization getNormalizer() {
        return normalizer;
    }
}