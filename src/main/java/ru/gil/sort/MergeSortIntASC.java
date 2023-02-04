package ru.gil.sort;

import ru.gil.exception.NotElementFileException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MergeSortIntASC extends MergeSortInt {
    private static final Logger LOG = Logger.getLogger(MergeSortIntASC.class.getName());

    // Сортировка 2 файлов
    @Override
    protected void mergeSort(BufferedReader inOne, BufferedReader inTwo, PrintWriter out) {
        int n;
        int m;
        int minCurrent;
        try {
            n = nextInt(inOne);
            m = nextInt(inTwo);
            // Изначально в 2 файлах числа валидные
            minCurrent = Math.min(n, m);
            while (inOne.ready() && inTwo.ready()) {
                if (n <= m && n >= minCurrent) {
                    minCurrent = n;
                    out.println(n);
                    n = nextInt(inOne);
                } else if (m < n && m >= minCurrent) {
                    minCurrent = m;
                    out.println(m);
                    m = nextInt(inTwo);
                } else if (n < minCurrent) {
                    LOG.warning("Data does not match sorting requirements");
                    n = nextInt(inTwo);
                } else {
                    LOG.warning("Data does not match sorting requirements");
                    m = nextInt(inTwo);
                }
            }
            if (!inOne.ready()) {
                mergeOneTwo(inTwo, minCurrent, m, n, out);
            } else {
                mergeOneTwo(inOne, minCurrent, n, m, out);
            }
        } catch (NotElementFileException ignored) {
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Input output error", ex);
        }
    }

    // Слияние остатка с его валидацией
    @Override
    protected void mergeOneTwo(BufferedReader in, int minCurrent, int n, int m,
                               PrintWriter out) throws IOException {
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
                    LOG.log(Level.WARNING, "Data does not match sorting requirements {0}", n);
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
                    LOG.log(Level.WARNING, "Data does not match sorting requirements {0}", n);
                } else {
                    out.println(n);
                    minCurrent = n;
                }
                n = nextInt(in);
            }
            if (n >= minCurrent) {
                out.println(n);
            }
        } catch (NotElementFileException ignored) {
        }
    }

    // Слияние с валидацией если у нас пришел один входящий файл
    @Override
    protected void mergeFileOne(Path infile, String outFile) {
        try (BufferedReader in = Files.newBufferedReader(infile);
             PrintWriter out = new PrintWriter(outFile)) {
            int n = nextInt(in);
            int minCurrent = n;
            while (in.ready()) {
                if (n >= minCurrent) {
                    out.println(n);
                    minCurrent = n;
                } else {
                    LOG.log(Level.WARNING, "Data does not match sorting requirements {0}", n);
                }
                n = nextInt(in);
            }
            if (n >= minCurrent) {                                                  // Последний элемент
                out.println(n);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "File opening error or in/out", ex);
        } catch (NotElementFileException ignored) {
        }
    }
}