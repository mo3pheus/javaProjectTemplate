package bootstrap;

import org.apache.log4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ArgumentParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Driver {
    public static final String SEPARATOR =
            "==============================================================";

    public static Properties projectProperties = new Properties();
    public static Logger logger = LoggerFactory.getLogger(Driver.class);

    public static void main(String[] args) {
        try {
            ArgumentParser argumentParser = new ArgumentParser(args);
            argumentParser.printArgMap();
            configureLogging(
                    Boolean.parseBoolean(argumentParser.getArgMap().get("debug.logging")));

            logger.info("Hello World");

            int newsFrequencyHours = Integer.parseInt(argumentParser.getArgMap().get("news.frequency.hours"));
            logger.info("News hour frequency = " + newsFrequencyHours);
        } catch (Exception io) {
            // Incorrect error message -
            logger.error("IOException", io);
            io.printStackTrace();
            ArgumentParser.printHelpMessage();
        }
    }

    public static String configureLogging(boolean debug) {
        FileAppender fileAppender = new FileAppender();

        if (!debug) {
            fileAppender.setThreshold(Level.toLevel(Priority.INFO_INT));
            fileAppender.setFile("executionLogs/log_infoLevel_report_" + Long.toString(System.currentTimeMillis()) + ".log");
        } else {
            fileAppender.setThreshold(Level.toLevel(Priority.DEBUG_INT));
            fileAppender.setFile("executionLogs/log_debugLevel_report_" + Long.toString(System.currentTimeMillis()) + ".log");
        }

        fileAppender.setLayout(new EnhancedPatternLayout("%-6d [%25.35t] %-5p %40.80c - %m%n"));

        fileAppender.activateOptions();
        org.apache.log4j.Logger.getRootLogger().addAppender(fileAppender);
        return fileAppender.getFile();
    }

    public static void configureConsoleLogging(boolean debug) {
        ConsoleAppender ca = new ConsoleAppender();
        if (!debug) {
            ca.setThreshold(Level.toLevel(Priority.INFO_INT));
        } else {
            ca.setThreshold(Level.toLevel(Priority.DEBUG_INT));
        }
        ca.setLayout(new EnhancedPatternLayout("%-6d [%25.35t] %-5p %40.80c - %m%n"));
        ca.activateOptions();
        org.apache.log4j.Logger.getRootLogger().addAppender(ca);
    }

    public static Properties getProjectProperties(String propertiesFilePath) throws IOException {
        logger.info("Properties file specified at location = " + propertiesFilePath);
        FileInputStream projFile = new FileInputStream(propertiesFilePath);
        Properties properties = new Properties();
        properties.load(projFile);
        return properties;
    }
}
