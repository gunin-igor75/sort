package ru.gil.sort;

import ru.gil.exception.NotElementFileException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.logging.Logger;

public class MergeSortInt {
    private static Logger LOG = Logger.getLogger(MergeSortInt.class.getName());

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
                 PrintWriter writer = new PrintWriter(nameFile.toString())) {
                mergeSort(inOne, inTwo, writer);
                inFiles.addFirst(Paths.get(nameFile.toString()));
                numberFile++;
                if (fileOne.getFileName().toString().startsWith("tmp")) {
                    Files.delete(fileOne);
                }
                if (fileTwo.getFileName().toString().startsWith("tmp")) {
                    Files.delete(fileTwo);
                }
            } catch (IOException ex) {
                LOG.severe("File opening error");
                ex.printStackTrace(System.out);
            }
        }
        Path result = Path.of(outFile);
        try {
            Files.move(inFiles.getFirst(), result, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOG.severe("File opening error");
            ex.printStackTrace(System.out);
        }
    }

    // Сортировка 2 файлов
    private void mergeSort(BufferedReader inOne, BufferedReader inTwo, PrintWriter writer) throws IOException {
        int n = Integer.MIN_VALUE;
        int m = Integer.MIN_VALUE;
        int minCurrent = Integer.MIN_VALUE;
        try {
            n = nextInt(inOne);
            m = nextInt(inTwo);
            // Изначально в 2 файлах числа валидные
            minCurrent = Math.min(n, m);
            while (inOne.ready() && inTwo.ready()) {
                if (n <= m && n >= minCurrent) {
                    minCurrent = n;
                    writer.println(n);
                    n = nextInt(inOne);
                } else if (m < n && m >= minCurrent) {
                    minCurrent = m;
                    writer.println(m);
                    m = nextInt(inTwo);
                } else if (n < minCurrent) {
                    LOG.warning("Data does not match sorting requirements");
                    n = nextInt(inTwo);
                } else {
                    LOG.warning("Data does not match sorting requirements");
                    m = nextInt(inTwo);
                }
            }
        } catch (NotElementFileException ignored) {
        }

        if (!inOne.ready()) {
            mergeOneTwo(inTwo, minCurrent, m, n, writer);
        } else {
            mergeOneTwo(inOne, minCurrent, n, m, writer);
        }
    }

    // Слияние остатка с его валидацией
    private void mergeOneTwo(BufferedReader in, int minCurrent, int n, int m,
                             PrintWriter out) {
        boolean flag = false;
        try {
            while (in.ready()) {
                if (n < m && n >= minCurrent) {
                    out.println(n);
                    minCurrent = n;
                    n = nextInt(in);
                } else if (m <= n && m >= minCurrent) {
                    out.println(m);
                    minCurrent = m;
                    flag = true;
                    break;
                } else {
                    n = nextInt(in);
                    LOG.warning("Data does not match sorting requirements");
                }
            }
            if (!flag) {                                                            // Осталось 2 числа
                if (n < m && n >= minCurrent) {
                    out.println(n);
                    out.println(m);
                } else if (m <= n && m >= minCurrent) {
                    out.println(m);
                    out.println(n);
                }
                return;
            }
            while (in.ready()) {
                if (n < minCurrent) {
                    LOG.warning("Data does not match sorting requirements");
                } else {
                    out.println(n);
                    minCurrent = n;
                }
                n = nextInt(in);
            }
            if (n >= minCurrent) {
                out.println(n);
            }
        } catch (IOException ex) {
            LOG.severe("File opening error");
            ex.printStackTrace(System.out);
        } catch (NotElementFileException ignored) {
        }
    }

    // Слияние с валидацией если у нас пришел один входящий файл
    private void mergeFileOne(Path infile, String outFile) {
        try (BufferedReader in = Files.newBufferedReader(infile);
             PrintWriter out = new PrintWriter(outFile)) {
            int n = nextInt(in);
            int minCurrent = n;
            while (in.ready()) {
                if (n >= minCurrent) {
                    out.println(n);
                    minCurrent = n;
                } else {
                    LOG.warning("Data does not match sorting requirements");
                }
                n = nextInt(in);
            }
            if (n >= minCurrent) {                                                  // Последний элемент
                out.println(n);
            }
        } catch (IOException ex) {
            LOG.severe("File opening error");
            ex.printStackTrace(System.out);
        } catch (NotElementFileException ignored) {
        }
    }

    // Считывание и парсинг int
    private int nextInt(BufferedReader reader) throws IOException, NotElementFileException {
        String numberString;
        int number;
        while ((numberString = reader.readLine()) != null) {
            try {
                number = Integer.parseInt(numberString);
                return number;
            } catch (NumberFormatException ex) {
                LOG.warning("Not a valid number");
            }
        }
        String message = "Missing numbers in file";
        LOG.warning(message);
        throw new NotElementFileException(message);
    }
}