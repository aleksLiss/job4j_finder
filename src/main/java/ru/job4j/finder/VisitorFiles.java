package ru.job4j.finder;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class VisitorFiles extends SimpleFileVisitor<Path> {

    private Predicate<Path> condition;
    private List<Path> result;

    public VisitorFiles(Predicate<Path> condition) {
        this.condition = condition;
        this.result = new ArrayList<>();
    }

    public List<Path> getPaths() {
        return result;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (condition.test(file)) {
            result.add(file.toAbsolutePath());
        }
        return FileVisitResult.CONTINUE;
    }
}
