package histogram;

import domain.POSDetail;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.ParsingUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;


public class CashierTransactionHistogramTest{

    String inputFile = "/Users/aap1018/Documents/workspace/minorityReportCopy/minorityreport/trendsDataDao/java/transactionCompare/src/main/resources/posDetails_03_2016.csv";
    static Logger logger = LoggerFactory.getLogger(CashierTransactionHistogramTest.class);
    Map<String, Map<String, Double>> targetHistogram = new HashMap<>();
    @Before
    public void setUp() throws IOException {
        CashierTransactionHistogram cashierHistogram = new CashierTransactionHistogram(inputFile);


    }

    @Test
    public void cashierTransactionHistogramTest




}
