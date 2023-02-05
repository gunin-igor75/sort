package ru.gil.sort;

import ru.gil.exception.NotElementFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MergeSortString extends AbstractMergeSort<String> {

    private final Pattern pattern = Pattern.compile("^\\S+$");
    private static final Logger LOG = Logger.getLogger(MergeSortString.class.getName());

    @Override
    protected String next(BufferedReader in) throws IOException {
        String s;
        Matcher matcher;
        while ((s = in.readLine()) != null) {
            matcher = pattern.matcher(s.trim());
            if (matcher.find()) {
                return s;
            } else {
                LOG.log(Level.WARNING, "Not a valid line {0}", s);
            }
        }
        String message = "Missing numbers in file";
        LOG.warning(message);
        throw new NotElementFileException(message);
    }

}