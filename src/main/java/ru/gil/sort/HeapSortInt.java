package ru.gil.sort;

import ru.gil.exception.NotElementHeapException;

import java.util.ArrayList;
import java.util.List;

public class HeapSortInt {

    // хранилище кучи
    private List<Integer> values = new ArrayList<>();

    // размер кучи
    private int size;

    public int getSize() {
        return size;
    }

    public void insert(Integer number) {
        // Присоединение к листу
        values.add(number);
        int index = size;
        size++;
        // всплытие
        siftUp(index);
    }

    // поменять местами
    private void swap(int i, int j) {
        int tmp = values.get(i);
        values.set(i, values.get(j));
        values.set(j, tmp);
    }

    // всплытие условие выхода из цикла или ты корень, или родитель < сына
    private void siftUp(int i) {
        while (i != 0 && values.get(i) < values.get((i - 1) / 2)) {
            if (values.get(i) < values.get((i - 1) / 2)) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
        }
    }

    // Извлечение минимума
    public Integer extractMin() throws NotElementHeapException {
        if (values.size() == 0) {
            throw new NotElementHeapException("Heap is Empty");
        }
        int tmp = values.get(0);
        values.set(0, values.get(values.size() - 1));
        values.remove(values.size() - 1);
        size--;
        if (size > 1) {
            siftDown(0);
        }
        return tmp;
    }

    // Погружение вниз
    private void siftDown(int i) {
        while (2 * i + 1 < size) {
            int j = i;
            if (values.get(2 * i + 1) < values.get(i)) {
                j = 2 * i + 1;
            }
            if (2 * i + 2 < size && values.get(2 * i + 2) < values.get(i)
                    && values.get(2 * i + 2) < values.get(2 * i + 1)) {
                j = 2 * i + 2;
            }
            if (i == j) {
                break;
            } else {
                swap(i, j);
                i = j;
            }
        }
    }

    public void heapIfyFast(List<Integer> list) {
        values = new ArrayList<>(list);
        size = list.size();
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    public List<Integer> getListSorted() {
        List<Integer> result = new ArrayList<>(size);
        while (values.size() != 0) {
            result.add(extractMin());
        }
        return result;
    }
}
