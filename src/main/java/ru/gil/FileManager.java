package ru.gil;

import ru.gil.sort.CreateListDataRandom;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static ru.gil.ReadWriteHelper.*;

public class FileManager {


    private Path outFile;

    private List<String> commands = new ArrayList<>();

    private final ArrayDeque<Path> inFiles = new ArrayDeque<>();

    public Path getOutFile() {
        return outFile;
    }

    public List<String> getCommands() {
        return new ArrayList<>(commands);
    }

    public List<Path> getInFiles() {
        return new ArrayList<>(inFiles);
    }

    private static final Logger LOG = Logger.getLogger(FileManager.class.getName());
    private void parse(String... args) {
        for (String arg : args) {
            if (arg.endsWith("txt")) {
                Path path = Paths.get(arg);
                if (Files.notExists(path)) {
                    String message = String.format("File %s not find ", arg );
                    LOG.warning(message);
                    writeMessage(message);
                    continue;
                }
                if (arg.startsWith("out")) {
                    outFile = path;
                } else {
                    inFiles.offer(path);
                }
            } else {
                commands.add(arg);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        FileManager manager = new FileManager();
        CreateListDataRandom service = new CreateListDataRandom(manager);
        manager.parse(args);

    }
}
