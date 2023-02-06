package ru.gil.sort;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CreateListDataRandom {

    private final List<Integer> listIntFiles = new ArrayList<>();
    private final List<String> listStringFiles = new ArrayList<>();

    private final Random random = new Random();

    public List<Integer> getListIntFiles() {
        return listIntFiles;
    }

    public List<String> getListStringFiles() {
        return listStringFiles;
    }

    private List<Integer> generateListInteger() {
        List<Integer> list = new ArrayList<>(1000000);
        for (int i = 0; i < 1000000; i++) {
            list.add(random.nextInt(Integer.MAX_VALUE));
        }
        return list;
    }

    private List<String> generateListString() {
        List<String> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(generateString());
        }
        return list;
    }

    private String generateString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public void createFileIntASK(int n) {
        for (int i = 1; i <= n; i++) {
        Path path = Paths.get("in" + i + ".txt");
        try (PrintWriter writer = new PrintWriter(path.toFile())) {
            List<Integer> list = generateListInteger();
            Collections.sort(list);
            listIntFiles.addAll(list);
            for (Integer number : list) {
                writer.println(number);
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
        Collections.sort(listIntFiles);
}

    public void createFileIntDESC(int n) {
        for (int i = 1; i <= n; i++) {
            Path path = Paths.get("in" + i + ".txt");
            try (PrintWriter writer = new PrintWriter(path.toFile())) {
                List<Integer> list = generateListInteger();
                list.sort(Collections.reverseOrder());
                listIntFiles.addAll(list);
                for (Integer number : list) {
                    writer.println(number);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
        listIntFiles.sort(Comparator.reverseOrder());
    }

    public void createFileStringASK(int n) {
        for (int i = 1; i <= n; i++) {
            Path path = Paths.get("in" + i + ".txt");
            try (PrintWriter writer = new PrintWriter(path.toFile())) {
                List<String> list = generateListString();
                Collections.sort(list);
                listStringFiles.addAll(list);
                for (String string : list) {
                    writer.println(string);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
        Collections.sort(listStringFiles);
    }

    public void createFileStringDESC(int n) {
        for (int i = 1; i <= n; i++) {
            Path path = Paths.get("in" + i + ".txt");
            try (PrintWriter writer = new PrintWriter(path.toFile())) {
                List<String> list = generateListString();
                list.sort(Comparator.reverseOrder());
                listStringFiles.addAll(list);
                for (String string : list) {
                    writer.println(string);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
        listStringFiles.sort(Comparator.reverseOrder());
    }
}
