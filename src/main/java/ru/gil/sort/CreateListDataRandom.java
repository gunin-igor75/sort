package ru.gil.sort;

import org.apache.commons.lang3.RandomStringUtils;
import ru.gil.FileManager;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CreateListDataRandom {

    FileManager manager;

    private final Random random = new Random();

    public CreateListDataRandom() {
    }

    public CreateListDataRandom(FileManager manager) {
        this.manager = manager;
    }

    // генерация списка случайными целыми числами
    public List<Integer> generateListInteger(int count) {
        List<Integer> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(random.nextInt(1000));
        }
        return list;
    }

    public List<String> generateListString(int count) {
        List<String> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(generateString());
        }
        return list;
    }

    private String generateString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    // Создание тестовых файлов
    public void createFileInt() {
        for (Path path : manager.getInFiles()) {
            try (PrintWriter writer = new PrintWriter(path.toFile())) {
                List<Integer> list = generateListInteger(10);
                list.sort(Collections.reverseOrder());
                for (Integer number : list) {
                    writer.println(number);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }

    public void createFileString() {
        for (Path path : manager.getInFiles()) {
            try (PrintWriter writer = new PrintWriter(path.toFile())) {
                List<String> list = generateListString(10);
                Collections.sort(list);
                for (String string : list) {
                    writer.println(string);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }


    public static void main(String[] args) throws IOException {
    }
}
