package bootstrap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class parseUtil {
    public static posDetail parsePosDetailsRow(String line) throws IllegalArgumentException {
        String[] rowCols = line.split(",");
        Logger logger = LoggerFactory.getLogger(parseUtil.class);
        if (rowCols.length < 19) {
            throw new IllegalArgumentException("Invalid row passed, cannot parse. " + line);
        }
        posDetail posDetail = new posDetail();
        try{
            posDetail.setCashier(rowCols[3]);
            //logger.info(posDetail.getCashier());
            posDetail.setHeaderRowKey(rowCols[4]);
            //logger.info(posDetail.getHeaderRowKey());
            //System.out.println(rowCols[10]);
            posDetail.setQuantity(Double.parseDouble(rowCols[10]));
            posDetail.setRegisterTime(rowCols[13]);
            //assert (posDetail.getRegisterTime()!=null) : "Null";
            //logger.info(posDetail.getRegisterTime());
            posDetail.setServerTime(rowCols[15]);
            posDetail.setTxType(rowCols[17]);
            posDetail.setValueNum(Double.parseDouble(rowCols[18]));

        }
        catch(Exception e){

        }


        return posDetail;
    }



}
