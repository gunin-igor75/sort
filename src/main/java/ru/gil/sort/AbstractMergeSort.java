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
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractMergeSort <T> {
    private static final Logger LOG = Logger.getLogger(MergeSortInt.class.getName());

    public void mainMethod(ArrayDeque<Path> inFiles, String outFile, Comparator<T> c) {
        if (inFiles.size() == 1) {
            mergeFileOne(inFiles.poll(), outFile, c);
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
                mergeSort(inOne, inTwo, out, c);
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

    // Сортировка 2 файлов
    protected void mergeSort(BufferedReader inOne, BufferedReader inTwo, PrintWriter out, Comparator<T> c) {
        T n;
        T m;
        T current;
        try {
            n = next(inOne);
            m = next(inTwo);
            // Изначально в 2 файлах числа валидные
            current = c.compare(n, m) <= 0 ? n : m;
            while (inOne.ready() && inTwo.ready()) {
                if (c.compare(n, m) < 0 && c.compare(n, current) >= 0) {
                    current = n;
                    out.println(n);
                    n = next(inOne);
                } else if (c.compare(m, n) <= 0 && c.compare(m, current) >= 0) {
                    current = m;
                    out.println(m);
                    m = next(inTwo);
                } else if (c.compare(n, current) < 0) {
                    LOG.warning("Data does not match sorting requirements");
                    n = next(inTwo);
                } else {
                    LOG.warning("Data does not match sorting requirements");
                    m = next(inTwo);
                }
            }
            if (!inOne.ready()) {
                mergeOneTwo(inTwo, current, m, n, out, c);
            } else {
                mergeOneTwo(inOne, current, n, m, out, c);
            }
        } catch (NotElementFileException ignored) {
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Input output error", ex);
        }
    }

    // Слияние остатка с его валидацией
    protected void mergeOneTwo(BufferedReader in, T current, T n, T m,
                               PrintWriter out, Comparator<T> c) throws IOException {
        boolean flag = false;
        try {
            while (in.ready()) {
                if (c.compare(n, m) < 0 && c.compare(n, current) >= 0) {
                    out.println(n);
                    current = n;
                    n = next(in);
                } else if (c.compare(m, n) <= 0 && c.compare(m, current) >= 0) {
                    out.println(m);
                    current = m;
                    flag = true;
                    break;
                } else {
                    n = next(in);
                    LOG.log(Level.WARNING, "Data does not match sorting requirements {0}", n);
                }
            }
            if (!flag) {                                                            // Осталось 2 числа
                if (c.compare(n, m) < 0 && c.compare(n, current) >= 0) {
                    out.println(n);
                    out.println(m);
                } else if (c.compare(m, n) <= 0 && c.compare(m, current) >= 0) {
                    out.println(m);
                    out.println(n);
                }
                return;
            }
            while (in.ready()) {
                if (c.compare(n, current) < 0) {
                    LOG.log(Level.WARNING, "Data does not match sorting requirements {0}", n);
                } else {
                    out.println(n);
                    current = n;
                }
                n = next(in);
            }
            if (c.compare(n, current) >= 0) {
                out.println(n);
            }
        } catch (NotElementFileException ignored) {
        }
    }

    // Слияние с валидацией если у нас пришел один входящий файл
    protected void mergeFileOne(Path infile, String outFile, Comparator<T> c) {
        try (BufferedReader in = Files.newBufferedReader(infile);
             PrintWriter out = new PrintWriter(outFile)) {
            T n = next(in);
            T current = n;
            while (in.ready()) {
                if (c.compare(n, current) >= 0) {
                    out.println(n);
                    current = n;
                } else {
                    LOG.log(Level.WARNING, "Data does not match sorting requirements {0}", n);
                }
                n = next(in);
            }
            if (c.compare(n, current) >= 0) {                                                  // Последний элемент
                out.println(n);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "File opening error or in/out", ex);
        } catch (NotElementFileException ignored) {
        }
    }

    // Считывание и парсинг int
    protected abstract T next(BufferedReader in) throws IOException, NotElementFileException;
}
