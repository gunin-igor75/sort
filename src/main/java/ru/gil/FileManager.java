package ru.gil;

import ru.gil.sort.CreateListDataRandom;
import ru.gil.sort.MergeSortIntASC;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class FileManager {


    private final MergeSortIntASC mergeSortIntASC;
    private  String outFile;

    private static final Logger LOG = Logger.getLogger(FileManager.class.getName());
    private final List<String> commands = new ArrayList<>();

    private final ArrayDeque<Path> inFiles = new ArrayDeque<>();


    public FileManager(MergeSortIntASC mergeSortIntASC) {
        this.mergeSortIntASC = mergeSortIntASC;
    }

    public String getOutFile() {
        return outFile;
    }

    public List<String> getCommands() {
        return new ArrayList<>(commands);
    }

    public List<Path> getInFiles() {
        return new ArrayList<>(inFiles);
    }

    private void parse(String... args) {
        for (String arg : args) {
            if (arg.endsWith("txt")) {
                Path path = Paths.get(arg);
                inFiles.offer(path);
            } else {
                commands.add(arg);
            }
        }
        outFile = Objects.requireNonNull(inFiles.poll()).toFile().toString();
//        inFiles.removeIf(file -> {
//            if (Files.notExists(file)) {
//                LOG.log(Level.SEVERE, "The file does not exist: {0}", file);
//                return true;
//            }
//            return false;
//        });
    }


    public void run() {

        mergeSortIntASC.mainMethod(inFiles, outFile);
    }

    public static void main(String[] args) {
        MergeSortIntASC merge = new MergeSortIntASC();
        FileManager manager = new FileManager(merge);
        CreateListDataRandom service = new CreateListDataRandom(manager);
        manager.parse(args);
        service.createFile();
        manager.run();
    }
}
