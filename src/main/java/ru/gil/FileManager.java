package ru.gil;

import ru.gil.sort.AbstractMergeSort;
import ru.gil.sort.CreateListDataRandom;
import ru.gil.sort.MergeSortInt;
import ru.gil.sort.MergeSortString;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class FileManager {


    private final AbstractMergeSort<String> mergeSortString;
    private final AbstractMergeSort<Integer> mergeSortInt;
    private  String outFile;

    private static final Logger LOG = Logger.getLogger(FileManager.class.getName());
    private final List<String> commands = new ArrayList<>();

    private final ArrayDeque<Path> inFiles = new ArrayDeque<>();


    public FileManager(AbstractMergeSort<String> mergeSortString, AbstractMergeSort<Integer> mergeSortInt) {
        this.mergeSortString = mergeSortString;
        this.mergeSortInt = mergeSortInt;
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
        mergeSortString.mainMethod(inFiles, outFile, String::compareTo);
    }

    public static void main(String[] args) {
        AbstractMergeSort<String> mergeString = new MergeSortString();
        AbstractMergeSort<Integer> mergeInt = new MergeSortInt();
        FileManager manager = new FileManager(mergeString, mergeInt);
        CreateListDataRandom service = new CreateListDataRandom(manager);
        manager.parse(args);
        service.createFileString();
        manager.run();
    }
}
