package bootstrap;

import aync.handler.RequiemLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DbManagerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class Driver {
    public static final String SEPARATOR =
            "==============================================================";

    public static Properties projectProperties = new Properties();
    public static Logger     logger            = LoggerFactory.getLogger(Driver.class);

    public static void main(String[] args) {
        try {
            String logFilePath = "";
            String uuId = UUID.randomUUID().toString();
            RequiemLogger requiemLogger = new RequiemLogger(DbManagerFactory.createDbManager().getDbConfig(),
                                                            Driver.class);
            requiemLogger.info(uuId, "Some weird Message");

            Exception e = new Exception("This is an exception.");
            requiemLogger.error(uuId, "This is an error exception", e);

            logger.info(SEPARATOR);
            logger.info("Project properties are loaded. Log file generated for this run = " + logFilePath);
            projectProperties = getProjectProperties(args[1]);

            System.out.println(projectProperties.getProperty("project.name"));

            while (true) {

            }

        } catch (Exception io) {
            // Incorrect error message -
            logger.error("IOException", io);
            io.printStackTrace();
        }
    }

    public static Properties getProjectProperties(String propertiesFilePath) throws IOException {
        logger.info("Properties file specified at location = " + propertiesFilePath);
        FileInputStream projFile   = new FileInputStream(propertiesFilePath);
        Properties      properties = new Properties();
        properties.load(projFile);
        return properties;
    }
}
