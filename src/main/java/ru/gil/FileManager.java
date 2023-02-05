package ru.gil;

import ru.gil.sort.AbstractMergeSort;
import ru.gil.sort.MergeSortInt;
import ru.gil.sort.MergeSortString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {


    // Сортировка строк
    private final AbstractMergeSort<String> mergeSortString;

    // Сортировка int
    private final AbstractMergeSort<Integer> mergeSortInt;

    // файл выходной с отсортированными данными
    private String outFile;

    private static final Logger LOG = Logger.getLogger(FileManager.class.getName());

    // Лист с командами
    private final List<String> commands = new ArrayList<>();

    // Очередь файлов с входными данными
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
        inFiles.removeIf(file -> {
            if (Files.notExists(file)) {
                LOG.log(Level.SEVERE, "The file does not exist: {0}", file);
                return true;
            }
            return false;
        });
    }


    public void run() {
        if (inFiles.isEmpty() || outFile == null) {
            LOG.log(Level.INFO, "missing input");
            return;
        }
        if (commands.size() == 1 || commands.contains("-a")) {
            if (commands.contains("-i")) {
                mergeSortInt.sort(inFiles, outFile, Integer::compareTo);
            } else if (commands.contains("-s")) {
                mergeSortString.sort(inFiles, outFile, String::compareTo);
            }
        } else if (commands.contains("-d")) {
            if (commands.contains("-i")) {
                mergeSortInt.sort(inFiles, outFile, Comparator.reverseOrder());
            } else if (commands.contains("-s")) {
                mergeSortString.sort(inFiles, outFile, Comparator.reverseOrder());
            }
        }
    }

    public static void main(String[] args) {
        AbstractMergeSort<String> mergeString = new MergeSortString();
        AbstractMergeSort<Integer> mergeInt = new MergeSortInt();
        FileManager manager = new FileManager(mergeString, mergeInt);
        manager.parse(args);
        manager.run();
    }
}
