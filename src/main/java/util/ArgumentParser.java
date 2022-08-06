package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
    private String[] args;
    private Map<String, String> argMap;

    private Logger logger = LoggerFactory.getLogger(ArgumentParser.class);

    public ArgumentParser(String[] args) {
        this.args = args;
        this.argMap = new HashMap<>();
        populateArgMap();
    }

    public void printArgs() {
        for (String arg : args) {
            logger.info(arg);
        }
    }

    private void populateArgMap() {
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("--")) {
                argMap.put(args[i], args[i + 1]);
            }
        }
    }

    public void printArgMap() {
        for (String key : argMap.keySet()) {
            logger.info(key + "::" + argMap.get(key));
        }
    }
}
