package Heap;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;
import static java.util.Collections.swap;

/*
 * 带反向查找的的索引堆（Index Heap with Reverse Retrieval）
 *
 * - 问题：
 *   - IndexMaxHeap 的实现中，add、extractMax 方法的复杂度为 O(logn)，而 change 为 O(n)，成了堆操作的性能短板。
 *   - 具体来说，在 change 过程中，需要通过遍历找到指定索引 i 在堆索引 indexes 中的位置，这个过程是 O(n) 的复杂度。
 * - 优化：
 *   - 如果采用空间换时间的思想，将要查找的结果提前维护在数组中，这样在需要用的时候就可以通过 O(1) 的复杂度来查找了。
 *   - 具体来说，建立"反向索引" reverse 数组：
 *            data: [15, 17, 19, 13, 22, 20]
 *         indexes:  4   2   5   3   0   1     -- 维护 data 中每个元素在最大堆中的位置
 *         reverse:  4   5   1   3   0   2     -- 维护 data 中每个元素的索引在 indexes 中的位置
 * */

public class IndexMaxHeapOptimised<E extends Comparable> {
    List<E> data;
    List<Integer> indexes;

    public IndexMaxHeapOptimised(int capacity) {
        data = new ArrayList<>(capacity);
        indexes = new ArrayList<>(capacity);
    }

    public IndexMaxHeapOptimised() {
        data = new ArrayList<>();
        indexes = new ArrayList<>();
    }

    public IndexMaxHeapOptimised(E[] arr) {
        data = new ArrayList<>(arr.length);
        for (E e : arr)
            data.add(e);

        indexes = new ArrayList<>(arr.length);
        for (int i = 0; i < arr.length; i++)
            indexes.add(i);

        int lastNonLeafNodeIndex = getParentIndex(arr.length - 1);
        while (lastNonLeafNodeIndex >= 0)
            siftDown(lastNonLeafNodeIndex--);
    }

    private int getParentIndex(int index) {
        if (index <= 0)
            throw new IllegalArgumentException("getParentIndex failed.");
        return (index - 1) / 2;
    }

    private int getLeftChildIndex(int index) {
        return index * 2 + 1;
    }

    private E getElement(int i) {
        return data.get(indexes.get(i));
    }

    private void siftUp(int k) {
        while (k > 0 && getElement(getParentIndex(k)).compareTo(getElement(k)) < 0) {
            swap(indexes, k, getParentIndex(k));
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < indexes.size()) {
            int i = getLeftChildIndex(k);
            if (i + 1 < indexes.size() && getElement(i).compareTo(getElement(i + 1)) < 0)
                i += 1;
            if (getElement(k).compareTo(getElement(i)) >= 0)
                break;
            swap(indexes, k, i);
            k = i;
        }
    }

    public void add(E e) {
        data.add(e);
        indexes.add(indexes.size());
        siftUp(indexes.size() - 1);
    }

    public E extractMax() {
        E ret = getElement(0);
        indexes.set(0, indexes.get(indexes.size() - 1));
        indexes.remove(indexes.size() - 1);
        siftDown(0);
        return ret;
    }

    public void change(int i, E newE) {
        data.set(i, newE);
        for (int j = 0; j < indexes.size(); j++)
            if (indexes.get(j) == i) {
                siftUp(j);
                siftDown(j);
                return;
            }
    }

    public boolean isEmpty() {
        return indexes.size() == 0;
    }

    @Override
    public String toString() {
        return "Elements: " + data.toString() + "; Indexes: " + indexes.toString();
    }

    public static void main(String[] args) {
        Integer[] inputSeq = {15, 17, 19, 13, 22, 20};
        IndexMaxHeapOptimised<Integer> heap1 = new IndexMaxHeapOptimised<>(inputSeq);
        log(heap1);

    }
}
