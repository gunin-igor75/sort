package ru.gil.sort;

import ru.gil.exception.NotElementFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MergeSortInt extends AbstractMergeSort<Integer> {
    private static final Logger LOG = Logger.getLogger(MergeSortInt.class.getName());

    @Override
    protected Integer next(BufferedReader in) throws IOException, NotElementFileException {
        String s;
        int n;
        while ((s = in.readLine()) != null) {
            try {
                n = Integer.parseInt(s.trim());
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
