package ru.job4j.finder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

public class Search {
    public static void main(String[] args) throws IOException {
        if (checkArguments(args)) {
            Path start = Paths.get(args[0]);
            search(start, path -> path.toFile().getName().endsWith(args[1])).forEach(System.out::println);
        }
    }

    public static List<Path> search(Path root, Predicate<Path> condition) throws IOException {
        VisitorFiles searcher = new VisitorFiles(condition);
        Files.walkFileTree(root, searcher);
        return searcher.getPaths();
    }

    private static boolean checkArguments(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Error: count of arguments must be two");
        }
        /*
        if (!".".equals(args[0])) {
            throw new IllegalArgumentException("Error: first argument must be root directory");
        }
         */
        if (!args[1].startsWith(".")) {
            throw new IllegalArgumentException("Error: second argument must contains file extension");
        }
        return true;
    }
}
