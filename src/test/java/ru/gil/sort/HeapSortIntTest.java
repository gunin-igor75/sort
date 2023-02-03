package ru.gil.sort;

import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class HeapSortIntTest extends TestCase {

    private HeapSortInt heap = new HeapSortInt();
    private CreateListDataRandom service = new CreateListDataRandom();

    @Test
    public void testHeap() {
        List<Integer> listOther = service.generateListInteger(100000);
        heap.heapIfyFast(listOther);
        Collections.sort(listOther);
        List<Integer> result = heap.getListSorted();
        Assertions.assertThat(listOther).isEqualTo(result);
    }
}