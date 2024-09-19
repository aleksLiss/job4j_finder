package ru.job4j.finder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Finder {
    private List<Path> resultPaths;

    private void find(ArgsName argsName) {
        String root = argsName.get("d");
        String nameOfFile = argsName.get("n");
        switch (argsName.get("t")) {
            case "name":
                visitor(root, nameOfFile);
            case "mask":
            case "regex":
                visitor(root, parseMask(nameOfFile));
                break;
            default:
                break;
        }
        writeToFile(argsName);
    }

    private void visitor(String root, String mask) {
        try {
            String regex = Pattern.compile(mask).pattern();
            System.out.println(regex);
            Predicate<Path> predicate = (i) -> i.toFile().getName().matches(regex);
            try {
                VisitorFiles searcher = new VisitorFiles(predicate);
                Files.walkFileTree(Path.of(root), searcher);
                resultPaths = searcher.getPaths();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String parseMask(String mask) {
        return mask.replace(".", "[.]")
                .replace("*", ".*")
                .replace("?", ".");
    }

    private void writeToFile(ArgsName argsName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(argsName.get("o")))) {
            for (Path path : resultPaths) {
                writer.println(path.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void checkArgs(String key, String arg) {
        switch (key) {
            case "d":
                if (arg.isEmpty()) {
                    throw new IllegalArgumentException("Error: arg must contains directory name");
                }
                break;
            case "n":
                if (arg.isEmpty()) {
                    throw new IllegalArgumentException("Error: arg must contains name of file or mask or regex");
                }
                break;
            case "t":
                if (!"name".equals(arg) && !"mask".equals(arg) && !"regex".equals(arg)) {
                    throw new IllegalArgumentException("Error: search type must be name or mask or regex");
                }
                break;
            case "o":
                if (!arg.contains(".")) {
                    throw new IllegalArgumentException("Error: arg must contains file extension");
                }
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        ArgsName argsName = ArgsName.of(args);
        checkArgs("d", argsName.get("d"));
        checkArgs("n", argsName.get("n"));
        checkArgs("t", argsName.get("t"));
        checkArgs("o", argsName.get("o"));
        Finder finder = new Finder();
        finder.find(argsName);
    }
}
