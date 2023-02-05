package ru.gil.sort;


import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MyVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (Files.exists(file)) {
            String name = file.getFileName().toString();
            if (name.endsWith(".txt")) {
               Files.delete(file);
            }
        }
        return FileVisitResult.CONTINUE;
    }
}
