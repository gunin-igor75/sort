package ru.gil.sort;

import ru.gil.exception.NotElementFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

public class MergeSortString extends AbstractMergeSort<String>{
    private static final Logger LOG = Logger.getLogger(MergeSortString.class.getName());
    @Override
    protected String next(BufferedReader in) throws IOException{
        String s;
        while ((s = in.readLine()) != null) {
                return s;
        }
        String message = "Missing numbers in file";
        LOG.warning(message);
        throw new NotElementFileException(message);
    }
}
