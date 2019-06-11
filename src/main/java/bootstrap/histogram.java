package bootstrap;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class name - a) Start with a capital letter
// b) variables start with a small letter - avoid numbers and underscores or special characters
// c) variables are camelcase.

// Histogram histogram = new Histogram(); - tells me that Histogram is a class
// Histogram customerHistogram; - tells me that customerHistogram is a variable



// Not a strong object oriented design. Need to think about what data this class takes and what functions/ services it exposes and offers
public class histogram {

    public static final String REG_TIME_PATTERN = "MM/dd/yy HH:mm";
    Logger                           logger                    = LoggerFactory.getLogger(histogram.class);
    Map<String, Map<String, Double>> cashierHourOfDayHistogram = new HashMap<>();

    // poor function name - names for variables and functions/ classes should express what they are doing.
    public void utility() throws IOException {
        // hard coded input path - can not run the program with different paths
        String       inputPath = "/Users/aap1018/Documents/workspace/minorityReportCopy/minorityreport/trendsDataDao/java/transactionCompare/src/main/resources/posDetails_03_2016.csv";
        List<String> lines     = Files.readAllLines(Paths.get(inputPath));

        for (String line : lines) {
            if ( line == lines.get(0) )
                // do not write code assuming fixed structure of input file.

                continue;
            posDetail posDetail = parseUtil.parsePosDetailsRow(line);
            //logger.info("I got "+posDetail.getRegisterTime());
            //assert (posDetail.getRegisterTime()!=null) : "Null here";
            if ( posDetail.getRegisterTime() == null ) {
                continue;
            }
            addToCashierHistogram(posDetail);
        }
    }


    public void addToCashierHistogram(posDetail posDetail) {
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

    public int getTransactionHourOfDay(posDetail posDetail) {
        DateTime dateTime = getTransactionDateTime(posDetail);

        return dateTime.getHourOfDay();
    }

    public DateTime getTransactionDateTime(posDetail posDetail) {
        String timeStamp = posDetail.getRegisterTime();
        return getTransactionDateTime(timeStamp);
    }

    public DateTime getTransactionDateTime(String timeStamp) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(REG_TIME_PATTERN);
        //DateTime          dateTime          = dateTimeFormatter.parseDateTime(timeStamp).withZone(DateTimeZone.UTC);

        DateTime dateTime = dateTimeFormatter.parseDateTime(timeStamp);
        return dateTime;
    }


    public String generateCashierKey(posDetail posDetail) {
        return getTransactionHourOfDay(posDetail) + "_" + posDetail.getCashier();
    }

    public String generateTransactionTypeKey(posDetail posDetail) {
        return getTransactionHourOfDay(posDetail) + "_" + posDetail.getTxType();
    }

    public void printHistogram() throws IOException {
        // fixed path - so code is not flexible.
        FileWriter  filewriter  = new FileWriter("/Users/aap1018/Documents/workspace/javaProjectTemplate/src/main/java/bootstrap/hist.txt");
        PrintWriter printWriter = new PrintWriter(filewriter);
        for (String key : cashierHourOfDayHistogram.keySet()) {
            Map<String, Double> map = cashierHourOfDayHistogram.get(key);
            printWriter.println("Cashier Info: " + key);
            for (String txnKey : map.keySet()) {
                printWriter.printf("    %f %s \n", map.get(txnKey), txnKey);
            }
        }
        printWriter.close();
        System.out.println("Success");
    }


}
