package MinimumSpanningTree.AuxiliaryDataStructure;

import java.util.Arrays;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
* 最小索引堆（作为 PrimMST 的辅助数据结构）
*
* - 采用了 IndexMaxHeapOptimised 中的实现，带有 reverse 反向索引，使 change 方法的复杂度为 O(logn)。
* */

public class IndexMinHeap<E extends Comparable<E>> {
    private E[] data;
    private int[] indexes;
    private int[] reverse;
    private int size;

    public IndexMinHeap(int capacity) {
        data = (E[]) new Comparable[capacity];
        indexes = new int[capacity];
        reverse = new int[capacity];
        size = 0;

        for (int i = 0; i < capacity; i++)
            reverse[i] = -1;
    }

    public IndexMinHeap(E[] arr) {
        int n = arr.length;
        data = (E[]) new Comparable[n];
        indexes = new int[n];
        reverse = new int[n];

        for (int i = 0; i < n; i++) {
            data[i] = arr[i];
            indexes[i] = i;
            reverse[i] = -1;
        }
        size = n;

        // heapify
        int lastNonLeafNodeIndex = getParentIndex(n - 1);
        for (int i = lastNonLeafNodeIndex; i >= 0; i--)
            siftDown(i);
    }

    private int getParentIndex(int index) {
        if (index <= 0)
            throw new IllegalArgumentException("getParentIndex failed.");
        return (index - 1) / 2;
    }

    private int getLeftChildIndex(int index) {
        return index * 2 + 1;
    }

    private E getElement(int i) { return data[indexes[i]]; }

    private void swapIndexes(int i, int j) {
        swap(indexes, i, j);
        reverse[indexes[i]] = i;
        reverse[indexes[j]] = j;
    }

    public boolean contains(int i) {
        if (i < 0 || i >= data.length)
            throw new IllegalArgumentException("contains failed. Index out of bounds.");
        return reverse[i] != -1;
    }

    private void siftUp(int k) {
        while (k > 0 && getElement(getParentIndex(k)).compareTo(getElement(k)) > 0) {
            swapIndexes(k, getParentIndex(k));
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < size) {
            int i = getLeftChildIndex(k);
            if (i + 1 < size && getElement(i + 1).compareTo(getElement(i)) < 0)
                i += 1;
            if (getElement(k).compareTo(getElement(i)) <= 0)
                break;
            swapIndexes(k, i);
            k = i;
        }
    }

    public void insert(int i, E e) {
        if (contains(i))
            throw new IllegalArgumentException("insert failed. Index has been taken");
        data[i] = e;
        indexes[size] = i;
        reverse[i] = size;
        size++;
        siftUp(size - 1);
    }

    public E extractMin() {
        E ret = getElement(0);
        swapIndexes(0, size - 1);
        reverse[indexes[size - 1]] = -1;
        size--;
        siftDown(0);
        return ret;
    }

    public int extractMinIndex() {
        int ret = indexes[0];
        swapIndexes(0, size - 1);
        reverse[indexes[size - 1]] = -1;
        size--;
        siftDown(0);
        return ret;
    }

    public void change(int i, E newE) {
        if (!contains(i))
            throw new IllegalArgumentException("change failed.");
        data[i] = newE;
        int j = reverse[i];
        siftUp(j);
        siftDown(j);
    }

    public E getItem(int i) {
        if (!contains(i))
            throw new IllegalArgumentException("getItem failed.");
        return data[i];
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        return String.format("Elements: %s; Indexes: %s; Reverse: %s", Arrays.toString(data), Arrays.toString(indexes), Arrays.toString(reverse));
    }

    public static void main(String[] args) {
        Integer[] inputSeq = {15, 17, 19, 13, 22, 20};

        log("---- Testing heapify ----");
        IndexMinHeap<Integer> heap1 = new IndexMinHeap<>(inputSeq);
        log(heap1);
        while (!heap1.isEmpty())
            log("Extracted: " + heap1.extractMin() + "; " + heap1.toString());

        log("\n---- Testing insert ----");  // 测试将元素以乱序插入堆中
        IndexMinHeap<Integer> heap2 = new IndexMinHeap<>(inputSeq.length);
        heap2.insert(4, inputSeq[0]);
        log(heap2);
        heap2.insert(2, inputSeq[1]);
        log(heap2);
        heap2.insert(3, inputSeq[2]);
        log(heap2);
        heap2.insert(0, inputSeq[3]);
        log(heap2);
        heap2.insert(5, inputSeq[4]);
        log(heap2);
        heap2.insert(1, inputSeq[5]);
        log(heap2);

        log("\n---- Testing change ----");
        heap2.change(2, 999);  // 修改中间元素
        log(heap2);
        while (!heap2.isEmpty())
            log("Extracted: " + heap2.extractMin() + "; " + heap2.toString());
    }
}
