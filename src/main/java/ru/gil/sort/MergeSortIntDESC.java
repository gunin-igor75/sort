package ru.gil.sort;

import ru.gil.exception.NotElementFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MergeSortIntDESC extends MergeSortInt{

    private static final Logger LOG = Logger.getLogger(MergeSortIntDESC.class.getName());
    @Override
    protected void mergeSort(BufferedReader inOne, BufferedReader inTwo, PrintWriter out) {
        int n;
        int m;
        int minCurrent;
        try {
            n = nextInt(inOne);
            m = nextInt(inTwo);
            // Изначально в 2 файлах числа валидные
            minCurrent = Math.max(n, m);
            while (inOne.ready() && inTwo.ready()) {
                if (n >= m && n <= minCurrent) {
                    minCurrent = n;
                    out.println(n);
                    n = nextInt(inOne);
                } else if (m > n && m <= minCurrent) {
                    minCurrent = m;
                    out.println(m);
                    m = nextInt(inTwo);
                } else if (n > minCurrent) {
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

    @Override
    protected void mergeOneTwo(BufferedReader in, int minCurrent, int n, int m, PrintWriter out) throws IOException {

    }

    @Override
    protected void mergeFileOne(Path infile, String outFile) {

    }
}
