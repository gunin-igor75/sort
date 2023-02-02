package ru.gil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        HeapSortInt heap = new HeapSortInt();
        List<Integer> list = List.of(7, 4, 3, 8, 9, 33, 44, 6, 2, 1, 2, 4, 5, 6, 4, 2, 1);
        heap.heapIfyFast(list);
        list.forEach(e -> System.out.print(e + " "));
        System.out.println();
        for (int i = 0; i <list.size() ; i++) {
            System.out.print(heap.extractMin() + " ");
        }
    }
}
