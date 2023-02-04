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

public class MergeSortInt {
    private static final Logger LOG = Logger.getLogger(MergeSortInt.class.getName());

    public void mainMethod(ArrayDeque<Path> inFiles, String outFile, Comparator<Integer> c) {
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
    protected void mergeSort(BufferedReader inOne, BufferedReader inTwo, PrintWriter out, Comparator<Integer> c) {
        int n;
        int m;
        int current;
        try {
            n = nextInt(inOne);
            m = nextInt(inTwo);
            // Изначально в 2 файлах числа валидные
            current = c.compare(n, m) <= 0 ? n : m;
            while (inOne.ready() && inTwo.ready()) {
                if (c.compare(n, m) < 0 && c.compare(n, current) >= 0) {
                    current = n;
                    out.println(n);
                    n = nextInt(inOne);
                } else if (c.compare(m, n) <= 0 && c.compare(m, current) >= 0) {
                    current = m;
                    out.println(m);
                    m = nextInt(inTwo);
                } else if (c.compare(n, current) < 0) {
                    LOG.warning("Data does not match sorting requirements");
                    n = nextInt(inTwo);
                } else {
                    LOG.warning("Data does not match sorting requirements");
                    m = nextInt(inTwo);
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
    protected void mergeOneTwo(BufferedReader in, int current, int n, int m,
                               PrintWriter out, Comparator<Integer> c) throws IOException {
        boolean flag = false;
        try {
            while (in.ready()) {
                if (c.compare(n, m) < 0 && c.compare(n, current) >= 0) {
                    out.println(n);
                    current = n;
                    n = nextInt(in);
                } else if (c.compare(m, n) <= 0 && c.compare(m, current) >= 0) {
                    out.println(m);
                    current = m;
                    flag = true;
                    break;
                } else {
                    n = nextInt(in);
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
                n = nextInt(in);
            }
            if (c.compare(n, current) >= 0) {
                out.println(n);
            }
        } catch (NotElementFileException ignored) {
        }
    }

    ;

    // Слияние с валидацией если у нас пришел один входящий файл
    protected void mergeFileOne(Path infile, String outFile, Comparator<Integer> c) {
        try (BufferedReader in = Files.newBufferedReader(infile);
             PrintWriter out = new PrintWriter(outFile)) {
            int n = nextInt(in);
            int current = n;
            while (in.ready()) {
                if (c.compare(n, current) >= 0) {
                    out.println(n);
                    current = n;
                } else {
                    LOG.log(Level.WARNING, "Data does not match sorting requirements {0}", n);
                }
                n = nextInt(in);
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
