package h2o.ai;

public class IrisModelUtil {


    public static final String convertToString(Double[] data) {
        String dataString = "";
        for (Double d : data) {
            dataString += d.toString() + "|";
        }
        return dataString;
    }

    public static final String convertToString(double[] data) {
        String dataString = "";
        for (Double d : data) {
            dataString += d.toString() + "|";
        }
        return dataString;
    }
}
