package histogram;

import utility.ParsingUtility;
import domain.POSDetail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CashierTransactionHistogram {
    Logger logger = LoggerFactory.getLogger(CashierTransactionHistogram.class);
    private Map<String, Map<String, Double>> cashierHourOfDayHistogram = new HashMap<>();

    private String inputFile;

    public CashierTransactionHistogram(String inputFile) {
        this.inputFile = inputFile;
        try {
            this.generateHistogram();
        } catch (IOException e) {
            logger.info("" + e);
        }
    }

    public Map<String, Map<String, Double>> getCashierHourOfDayHistogram() {
        return this.cashierHourOfDayHistogram;
    }

    public void setCashierHourOfDayHistogram(Map<String, Map<String, Double>> cashierHourOfDayHistogram) {
        this.cashierHourOfDayHistogram = cashierHourOfDayHistogram;
    }


    private void generateHistogram() throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(inputFile));

        for (String line : lines) {
            POSDetail posDetail = ParsingUtility.parsePosDetailsRow(line);

            if (checkForNullAttributes(posDetail))
                continue;

            addToCashierHistogram(posDetail);
        }

        this.setCashierHourOfDayHistogram(cashierHourOfDayHistogram);
    }

    private boolean checkForNullAttributes(POSDetail posDetail) {
        if (posDetail.getRegisterTime() == null) {
            logger.info("Register time: " + posDetail.getRegisterTime());
            return true;
        }
        if (posDetail.getTxType() == null) {
            logger.info("Transaction Type: " + posDetail.getTxType());
            return true;
        }
        if (posDetail.getCashier() == null) {
            logger.info("Cashier Id: " + posDetail.getCashier());
        }
        return false;
    }

    private void addToCashierHistogram(POSDetail posDetail) {
        String cashierKey = ParsingUtility.generateCashierKey(posDetail);
        String transactionTypeKey = ParsingUtility.generateTransactionTypeKey(posDetail);
        if (cashierHourOfDayHistogram.containsKey(cashierKey)) {
            Map<String, Double> transactionTypeHistogram = cashierHourOfDayHistogram.get(cashierKey);
            if (transactionTypeHistogram.containsKey(transactionTypeKey)) {
                double frequency = transactionTypeHistogram.get(transactionTypeKey);
                transactionTypeHistogram.put(transactionTypeKey, frequency + 1.0);

            } else {
                transactionTypeHistogram.put(transactionTypeKey, 1.0);

            }
        } else {
            Map<String, Double> transactionTypeHistogram = new HashMap<>();
            transactionTypeHistogram.put(transactionTypeKey, 1.0);
            cashierHourOfDayHistogram.put(cashierKey, transactionTypeHistogram);
        }

    }


}
