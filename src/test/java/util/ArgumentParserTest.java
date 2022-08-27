package util;

import bootstrap.Driver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArgumentParserTest {

    ArgumentParser argumentParser;

    @Before
    public void setUp(){
        Driver.configureConsoleLogging(false);
    }

    @Test
    public void testDuplicateArgumentsSuccess() {
        String commnadLineArgs = "--debug.logging true --debug.logging false --numberOfThreads 12";
        String[] args = commnadLineArgs.split(" ");

        argumentParser = new ArgumentParser(args);

        int expectedArgumentNumber = 2;
        int actualArgumentNumber = argumentParser.getArgMap().keySet().size();

        Assert.assertEquals(expectedArgumentNumber, actualArgumentNumber);
    }

    @Test
    public void testDuplicateArgumentsFail() {
        String commnadLineArgs = "--debug.logging true --debug.loggiNg false --numberOfThreads 12";
        String[] args = commnadLineArgs.split(" ");

        argumentParser = new ArgumentParser(args);

        int expectedArgumentNumber = 2;
        int actualArgumentNumber = argumentParser.getArgMap().keySet().size();

        Assert.assertFalse(expectedArgumentNumber == actualArgumentNumber);
    }

    @Test
    public void testMissingArguments() {
        String commandLineArgs = "--debug.logging true --numberOfThreads --debug.logging false --numberOfThreads 2";

        String[] args = commandLineArgs.split(" ");
        argumentParser = new ArgumentParser(args);

        argumentParser.printArgMap();
    }
}
