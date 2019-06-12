package utility;
import bootstrap.Driver;

import domain.POSDetail;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilityHelper {
    public static final String REG_TIME_PATTERN = "MM/dd/yy HH:mm";
    public static Logger   logger  = LoggerFactory.getLogger(UtilityHelper.class);

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





}
