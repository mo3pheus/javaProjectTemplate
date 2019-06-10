package h2o.ai;

import hex.genmodel.MojoModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.exception.PredictException;
import hex.genmodel.easy.prediction.MultinomialModelPrediction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class IrisInferenceEngine {
    private String                  mojoFilePath            = null;
    private String[]                columnNames             = null;
    private EasyPredictModelWrapper easyPredictModelWrapper = null;
    private Logger                  logger                  = LoggerFactory.getLogger(IrisInferenceEngine.class);

    public IrisInferenceEngine(String mojoFilePath, String[] columnNames) throws IOException {
        this.mojoFilePath = mojoFilePath;
        this.columnNames  = columnNames;
        loadMojo();
    }

    private void loadMojo() throws IOException {
        EasyPredictModelWrapper.Config modelConfig = new EasyPredictModelWrapper.Config().setModel(MojoModel.load(mojoFilePath))
                .setEnableGLRMReconstrut(true);
        easyPredictModelWrapper = new EasyPredictModelWrapper(modelConfig);
    }

    public MultinomialModelPrediction predict(Double[] testVector) throws IllegalArgumentException {
        try {
            MultinomialModelPrediction multinomialModelPrediction = easyPredictModelWrapper.predictMultinomial(generateTestRow(testVector));
            return multinomialModelPrediction;
        } catch (PredictException p) {
            logger.error("Could not predict for testVector " + IrisModelUtil.convertToString(testVector), p);
        }
        return null;
    }

    private RowData generateTestRow(Double[] testVector) throws IllegalArgumentException {
        if ( columnNames.length != testVector.length ) {
            throw new IllegalArgumentException("Number of columns expected = " + columnNames.length);
        }

        RowData rowData = new RowData();
        for (int i = 0; i < columnNames.length; i++) {
            rowData.put(columnNames[i], testVector[i]);
        }

        return rowData;
    }
}
