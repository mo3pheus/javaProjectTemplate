package util;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
    private String[] args;
    private Map<String, String> argMap;

    //private Logger logger = LoggerFactory.getLogger(ArgumentParser.class);

    public ArgumentParser(String[] args) {
        this.args = args;
        this.argMap = new HashMap<>();
        populateArgMap();
    }

    public void printArgs() {
        for (String arg : args) {
            System.out.println(arg);
        }
    }

    private void populateArgMap() {
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("--")
                    && (i + 1 <= args.length)
                    && (!args[i + 1].contains("--"))
            ) {
                argMap.put(args[i].replaceAll("-", ""), args[i + 1]);
            }
        }
    }

    public void printArgMap() {
        for (String key : argMap.keySet()) {
            System.out.println(key + "::" + argMap.get(key));
        }
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public Map<String, String> getArgMap() {
        return argMap;
    }

    public void setArgMap(Map<String, String> argMap) {
        this.argMap = argMap;
    }

    public boolean isArgumentInteger(String argName) {
        String nameTimesArgValue = argMap.get(argName);
        try {
            int nameTimes = Integer.parseInt(nameTimesArgValue);
            return true;
        } catch (NumberFormatException numberFormatException) {
            System.out.println("name.times argument value could not be parsed to an Integer.");
            return false;
        }
    }

}
