package histogram;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;

import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


public class CashierTransactionHistogramTest {
    String inputFile = "./src/main/resources/input/txTest.csv";

    static Logger logger = LoggerFactory.getLogger(CashierTransactionHistogramTest.class);
    Map<String, Map<String, Double>> actualHistogram = new HashMap<>();
    Map<String, Map<String, Double>> expectedHistogram = new HashMap<>();
    CashierTransactionHistogram cashierHistogram;

    @Before
    public void setUp() {
        cashierHistogram = new CashierTransactionHistogram(inputFile);
        actualHistogram = cashierHistogram.getCashierHourOfDayHistogram();


    }

    @Test
    public void targetHistogramTest() {
        Map<String, Double> txMap1 = new HashMap<>();
        Map<String, Double> txMap2 = new HashMap<>();
        Map<String, Double> txMap3 = new HashMap<>();
        txMap1.put("3_AgeVerifyDOB", 1.0);
        expectedHistogram.put("3_5662593", txMap1);
        txMap2.put("2_Entry", 1.0);
        expectedHistogram.put("2_5662593", txMap2);
        txMap3.put("1_SignInOut", 2.0);
        expectedHistogram.put("1_5662593", txMap3);

        assertTrue("Error: Unexpected data", (actualHistogram.keySet().size() == expectedHistogram.keySet().size()));

        for (String cashier : expectedHistogram.keySet()) {
            assertTrue("Error: Unexpected data", (actualHistogram.containsKey(cashier)));
        }

        for (String cashier : expectedHistogram.keySet()) {
            Map<String, Double> expectedMap = expectedHistogram.get(cashier);
            Map<String, Double> actualMap = actualHistogram.get(cashier);

            assertTrue("Error: Unexpected data", (expectedMap.keySet().size() == actualMap.keySet().size()));

            for (String t : expectedMap.keySet()) {
                assertTrue("Error: Unexpected data", (actualMap.containsKey(t)));

            }

            for (String t : expectedMap.keySet()) {
                assertEquals(expectedMap.get(t), actualMap.get(t), 0.0001d);

            }

        }


    }


}
