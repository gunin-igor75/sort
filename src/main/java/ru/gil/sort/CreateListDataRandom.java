package ru.gil.sort;

import ru.gil.FileManager;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateListDataRandom {

    FileManager manager;

    private Random random = new Random();

    public CreateListDataRandom() {
    }

    public CreateListDataRandom(FileManager manager) {
        this.manager = manager;
    }

    // генерация списка случайными целыми числами
    public List<Integer> generateListInteger(int count) {
        List<Integer> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(random.nextInt(1000000));
        }
        return list;
    }

    // Создание тестовых файлов
    public void createFile(int n) {
        for (Path path : manager.getInFiles()) {
            try (PrintWriter writer = new PrintWriter(path.toFile())){
                List<Integer> list = generateListInteger(1000000);
                for (Integer number :list) {
                    writer.println(number);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }



    public static void main(String[] args) throws IOException {
        Path path = Paths.get("in1.txt");
        Files.createFile(path);
    }
}
