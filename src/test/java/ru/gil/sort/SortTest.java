package ru.gil.sort;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.gil.FileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class SortTest {
    private CreateListDataRandom createList;

    @Before
    public void init() {
        createList = new CreateListDataRandom();
    }

    @After
    public void destroy() throws IOException {
        MyVisitor vis = new MyVisitor();
        Files.walkFileTree(Paths.get(""), new HashSet<FileVisitOption>(), 1, vis);
    }

    @Test
    public void sortMergeIntASC() throws Exception {
        createList.createFileIntASK(3);
        FileManager.main("-i -a out.txt in1.txt in2.txt in3.txt ".split("\\s+"));
        List<Integer> result1 = readIntFile();
        Assertions.assertThat(createList.getListIntFiles()).isEqualTo(result1);
        FileManager.main("-i out.txt in1.txt in2.txt in3.txt ".split("\\s+"));
        List<Integer> result2 = readIntFile();
        Assertions.assertThat(createList.getListIntFiles()).isEqualTo(result2);
    }

    @Test
    public void sortMergeIntDESC() throws Exception {
        createList.createFileIntDESC(3);
        FileManager.main("-i -d out.txt in1.txt in2.txt in3.txt ".split("\\s+"));
        List<Integer> result = readIntFile();
        Assertions.assertThat(createList.getListIntFiles()).isEqualTo(result);
    }

    @Test
    public void sortMergeStringASC() throws Exception {
        createList.createFileStringASK(3);
        FileManager.main("-s -a out.txt in1.txt in2.txt in3.txt".split("\\s+"));
        List<String> result1 = readStringFile();
        Assertions.assertThat(createList.getListStringFiles()).isEqualTo(result1);
        FileManager.main("-s out.txt in1.txt in2.txt in3.txt".split("\\s+"));
        List<String> result2 = readStringFile();
        Assertions.assertThat(createList.getListStringFiles()).isEqualTo(result2);
    }

    @Test
    public void sortMergeStringDESC() throws Exception {
        createList.createFileStringDESC(3);
        FileManager.main("-s -d out.txt in1.txt in2.txt in3.txt".split("\\s+"));
        List<String> result = readStringFile();
        Assertions.assertThat(createList.getListStringFiles()).isEqualTo(result);
    }

    private List<Integer> readIntFile() throws Exception {
        List<Integer> list = new ArrayList<>();
        try (BufferedReader in = Files.newBufferedReader(Paths.get("out.txt"))) {
            String s;
            while ((s = in.readLine()) != null) {
                list.add(Integer.parseInt(s));
            }
        }
        return list;
    }

    private List<String> readStringFile() throws Exception {
        List<String> list = new ArrayList<>();
        try (BufferedReader in = Files.newBufferedReader(Paths.get("out.txt"))) {
            String s;
            while ((s = in.readLine()) != null) {
                list.add(s);
            }
        }
        return list;
    }
}
