package utility;
import bootstrap.Driver;

import domain.POSDetail;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class ParsingUtility {
    public static final String REG_TIME_PATTERN = "MM/dd/yy HH:mm";
    public static Logger   logger  = LoggerFactory.getLogger(ParsingUtility.class);

    public static POSDetail parsePosDetailsRow(String line) throws IllegalArgumentException {
        String[] rowCols = line.split(",");


        if ( rowCols.length < 19 ) {
            throw new IllegalArgumentException("Invalid row passed, cannot parse. " + line);
        }

        POSDetail posDetail = new POSDetail();
        try{
            posDetail.setCashier(rowCols[3]);
            posDetail.setHeaderRowKey(rowCols[4]);
            posDetail.setQuantity(Double.parseDouble(rowCols[10]));
            posDetail.setRegisterTime(rowCols[13]);
            posDetail.setServerTime(rowCols[15]);
            posDetail.setTxType(rowCols[17]);
            posDetail.setValueNum(Double.parseDouble(rowCols[18]));
        }
        catch(Exception e){
            logger.info(""+e);
        }


        return posDetail;
    }

    public static int getTransactionHourOfDay(POSDetail posDetail) {
        DateTime dateTime = getTransactionDateTime(posDetail);

        return dateTime.getHourOfDay();
    }

    public static DateTime getTransactionDateTime(POSDetail posDetail) {
        String timeStamp = posDetail.getRegisterTime();
        return getTransactionDateTime(timeStamp);
    }

    public static DateTime getTransactionDateTime(String timeStamp) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(REG_TIME_PATTERN);
        //DateTime          dateTime          = dateTimeFormatter.parseDateTime(timeStamp).withZone(DateTimeZone.UTC);

        DateTime dateTime = dateTimeFormatter.parseDateTime(timeStamp);
        return dateTime;
    }

    public static String generateCashierKey(POSDetail posDetail) {
        return ParsingUtility.getTransactionHourOfDay(posDetail) + "_" + posDetail.getCashier();
    }

    public static String generateTransactionTypeKey(POSDetail posDetail) {
        return ParsingUtility.getTransactionHourOfDay(posDetail) + "_" + posDetail.getTxType();
    }


    public static void printHistogram(Map<String, Map<String, Double>> cashierTransactionHistogram) throws IOException {
        // fixed path - so code is not flexible.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        String outputPath = new File("./src/main/resources/outputHistograms/cashierHistogram_")+timeStamp+""+".txt";
        FileWriter filewriter  = new FileWriter(outputPath);
        PrintWriter printWriter = new PrintWriter(filewriter);
        for (String key : cashierTransactionHistogram.keySet()) {
            Map<String, Double> map = cashierTransactionHistogram.get(key);
            printWriter.println("Cashier Info: " + key);
            for (String txnKey : map.keySet()) {
                printWriter.printf("    %f %s \n", map.get(txnKey), txnKey);
            }
        }

        printWriter.close();
        logger.info("Success");
    }





}
