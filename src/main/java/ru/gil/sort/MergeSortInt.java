package ru.gil.sort;

import ru.gil.exception.NotElementFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MergeSortInt {
    private static final Logger LOG = Logger.getLogger(MergeSortInt.class.getName());

    // Сортировка 2 файлов
    protected abstract void mergeSort(BufferedReader inOne, BufferedReader inTwo, PrintWriter out);

    // Слияние остатка с его валидацией
    protected abstract void mergeOneTwo(BufferedReader in, int minCurrent, int n, int m,
                                        PrintWriter out) throws IOException;

    // Слияние с валидацией если у нас пришел один входящий файл
    protected abstract void mergeFileOne(Path infile, String outFile);

    public void mainMethod(ArrayDeque<Path> inFiles, String outFile) {
        if (inFiles.size() == 1) {
            mergeFileOne(inFiles.poll(), outFile);
            return;
        }
        int numberFile = 1;
        while (inFiles.size() > 1) {
            Path fileOne = inFiles.removeFirst();
            Path fileTwo = inFiles.removeFirst();
            StringBuilder nameFile = new StringBuilder();
            nameFile.append("tmp").append(numberFile).append(".txt");
            try (BufferedReader inOne = Files.newBufferedReader(fileOne);
                 BufferedReader inTwo = Files.newBufferedReader(fileTwo);
                 PrintWriter out = new PrintWriter(nameFile.toString())) {
                mergeSort(inOne, inTwo, out);
                inFiles.addFirst(Paths.get(nameFile.toString()));
                numberFile++;
                if (fileOne.getFileName().toString().startsWith("tmp")) {
                    Files.delete(fileOne);
                }
                if (fileTwo.getFileName().toString().startsWith("tmp")) {
                    Files.delete(fileTwo);
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "File opening error", ex);
                return;
            }
        }
        Path result = Path.of(outFile);
        try {
            Files.move(inFiles.getFirst(), result, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Unable to move file", ex);
            LOG.log(Level.INFO, "File source {0}", inFiles.getFirst());
        }
    }

    // Считывание и парсинг int
    protected int nextInt(BufferedReader in) throws IOException, NotElementFileException {
        String nString;
        int n;
        while ((nString = in.readLine()) != null) {
            try {
                n = Integer.parseInt(nString);
                return n;
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "Not a valid number ", ex);
            }
        }
        String message = "Missing numbers in file";
        LOG.warning(message);
        throw new NotElementFileException(message);
    }
}
