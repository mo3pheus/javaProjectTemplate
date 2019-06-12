package histogram;

import domain.POSDetail;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utility.UtilityHelper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.util.Map;

public class CashierTransactionHistogramTest{

    String inputFile = "/Users/aap1018/Documents/workspace/minorityReportCopy/minorityreport/trendsDataDao/java/transactionCompare/src/main/resources/posDetails_03_2016.csv";
    static Logger logger = LoggerFactory.getLogger(CashierTransactionHistogramTest.class);
    Map<String, Map<String, Double>> targetHistogram = new HashMap<>();
    @BeforeTest
    public void setUp() {
        CashierTransactionHistogram cashierHistogram = new CashierTransactionHistogram(inputFile);
        try {
            cashierHistogram.generateHistogram();
        }
        catch (Exception e){
            logger.info(""+e);
        }
    }
    @Test
    public void generateHistogramTest() throws IOException{
        List <String> lines = Files.readAllLines(Paths.get(inputFile));

        for(String line: lines){
            POSDetail posDetail = UtilityHelper.parsePosDetailsRow(line);

            if (nullCheckTests(posDetail))
                continue;

            constructHistogramTest(posDetail);

        }



    }

    @Test
    public void constructHistogramTest(POSDetail posDetail){
        String cashierKey = cashierHistogram.generate
    }

    private boolean nullCheckTests(POSDetail posDetail){
        if(posDetail.getCashier()==null || posDetail.getTxType()==null || posDetail.getRegisterTime()==null){
            return true;
        }
        return false;

    }





}
