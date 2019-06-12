package histogram;

import utility.UtilityHelper;
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

// Class name - a) Start with a capital letter
// b) variables start with a small letter - avoid numbers and underscores or special characters
// c) variables are camelcase.

// Histogram histogram = new Histogram(); - tells me that Histogram is a class
// Histogram customerHistogram; - tells me that customerHistogram is a variable



// Not a strong object oriented design. Need to think about what data this class takes and what functions/ services it exposes and offers
public class CashierTransactionHistogram {
    Logger                           logger                    = LoggerFactory.getLogger(CashierTransactionHistogram.class);
    Map<String, Map<String, Double>> cashierHourOfDayHistogram = new HashMap<>();

    private String inputFile;

    public CashierTransactionHistogram(String inputFile){
        this.inputFile = inputFile;
    }

    // poor function name - names for variables and functions/ classes should express what they are doing.
    public void generateHistogram() throws IOException{
        // hard coded input path - can not run the program with different path

        List<String> lines     = Files.readAllLines(Paths.get(inputFile));

        for (String line : lines) {
            POSDetail posDetail = UtilityHelper.parsePosDetailsRow(line);

            if(checkForNullAttributes(posDetail))
                continue;

            addToCashierHistogram(posDetail);
        }
    }

    public boolean checkForNullAttributes(POSDetail posDetail){
        if(posDetail.getRegisterTime()==null) {
            logger.info("Register time: "+posDetail.getRegisterTime());
            return true;
        }
        if(posDetail.getTxType()==null){
            logger.info("Transaction Type: "+posDetail.getTxType());
            return true;
        }
        if(posDetail.getCashier()==null){
            logger.info("Cashier Id: "+posDetail.getCashier());
        }
        return false;
    }

    public void addToCashierHistogram(POSDetail posDetail) {
        String cashierKey         = generateCashierKey(posDetail);
        String transactionTypeKey = generateTransactionTypeKey(posDetail);
        if ( cashierHourOfDayHistogram.containsKey(cashierKey) ) {
            Map<String, Double> transactionTypeHistogram = cashierHourOfDayHistogram.get(cashierKey);
            if ( transactionTypeHistogram.containsKey(transactionTypeKey) ) {
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

    public String generateCashierKey(POSDetail posDetail) {
        return UtilityHelper.getTransactionHourOfDay(posDetail) + "_" + posDetail.getCashier();
    }

    public String generateTransactionTypeKey(POSDetail posDetail) {
        return UtilityHelper.getTransactionHourOfDay(posDetail) + "_" + posDetail.getTxType();
    }


    public void printHistogram() throws IOException {
        // fixed path - so code is not flexible.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        String outputPath = new File("./src/main/resources/outputHistograms/cashierHistogram_")+timeStamp+""+".txt";
        FileWriter filewriter  = new FileWriter(outputPath);
        PrintWriter printWriter = new PrintWriter(filewriter);
        for (String key : cashierHourOfDayHistogram.keySet()) {
            Map<String, Double> map = cashierHourOfDayHistogram.get(key);
            printWriter.println("Cashier Info: " + key);
            for (String txnKey : map.keySet()) {
                printWriter.printf("    %f %s \n", map.get(txnKey), txnKey);
            }
        }

        printWriter.close();
        logger.info("Success");
    }


}
