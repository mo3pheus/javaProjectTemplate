package bootstrap;

import org.junit.Assert;

import java.io.File;


public class DriverTest {


    private String [] args ={"false", "src/main/resources/mock.properties", "/Users/aap1018/Documents/workspace/minorityReportCopy/minorityreport/trendsDataDao/java/transactionCompare/src/main/resources/posDetails_03_2016.csv"};

    File file = new File(args[2]);
    boolean exists = file.exists();

    if(exists==false){
        System.out.println("File Input Path Error");
    }

    /*TODO
    Assert for no of sys args, file input path
     */










}
