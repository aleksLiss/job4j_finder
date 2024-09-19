package ru.job4j.finder;

import java.util.HashMap;
import java.util.Map;

public class ArgsName {

    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {
        if (!isContainsKey(key)) {
            throw new IllegalArgumentException(String.format("This key: '%s' is missing", key));
        }
        return values.get(key);
    }

    private void parse(String[] args) {
        for (String arg : args) {
            if (checkString(arg)) {
                values.put(arg.split("=", 2)[0].replace("-", ""),
                        arg.split("=", 2)[1]);
            }
        }
    }

    public static ArgsName of(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments not passed to program");
        }
        ArgsName names = new ArgsName();
        names.parse(args);
        return names;
    }

    public static void main(String[] args) {
        ArgsName jvm = ArgsName.of(new String[]{"-Xmx=512", "-encoding=UTF-8"});
        System.out.println(jvm.get("Xmx"));

        ArgsName zip = ArgsName.of(new String[]{"-out=project.zip", "-encoding=UTF-8"});
        System.out.println(zip.get("out"));
    }

    private boolean isContainsKey(String key) {
        return values.get(key) != null;
    }

    private boolean checkString(String arg) {
        if (!arg.contains("=")) {
            throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain an equal sign", arg));
        }
        String key = arg.split("=", 2)[0];
        if (!key.startsWith("-")) {
            throw new IllegalArgumentException(String.format("Error: This argument '%s' does not start with a '-' character", arg));
        }
        if (key.length() < 2) {
            throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain a key", arg));
        }
        String val = arg.split("=", 2)[1];
        if (val.isEmpty()) {
            throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain a value", arg));
        }
        return true;
    }
}