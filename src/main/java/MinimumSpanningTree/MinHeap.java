package MinimumSpanningTree;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;
import static java.util.Collections.swap;

public class MinHeap<E extends Comparable> {
    private List<E> data;

    public MinHeap(int capacity) { data = new ArrayList<>(capacity); }

    public MinHeap() { data = new ArrayList<>(); }

    public MinHeap(E[] arr) {  // heapify
        data = new ArrayList<>();
        for (E e : arr)
            data.add(e);

        int lastNonLeafNodeIndex = getParentIndex(getSize() - 1);
        for (int i = lastNonLeafNodeIndex; i >= 0; i--)
            siftDown(lastNonLeafNodeIndex--);
    }

    private int getLeftChildIndex(int index) { return index * 2 + 1; }

    private int getParentIndex(int index) {
        if (index <= 0)
            throw new IllegalArgumentException("getParentIndex failed.");
        return (index - 1) / 2;
    }

    private void siftUp(int k) {
        while (k > 0 && data.get(getParentIndex(k)).compareTo(data.get(k)) > 0) {
            swap(data, k, getParentIndex(k));
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < getSize()) {
            int i = getLeftChildIndex(k);
            if (i + 1 < getSize() && data.get(i + 1).compareTo(data.get(i)) < 0)
                i += 1;
            if (data.get(k).compareTo(data.get(i)) <= 0)
                break;
            swap(data, i, k);
            k = i;
        }
    }

    private E findMin() { return data.get(0); }

    public void insert(E e) {
        data.add(e);
        siftUp(getSize() - 1);
    }

    public E extractMin() {
        E ret = data.get(0);
        int lastIndex = getSize() - 1;
        data.set(0, data.get(lastIndex));
        data.remove(lastIndex);
        siftDown(0);
        return ret;
    }

    public E replace(E e) {
        E ret = findMin();
        data.set(0, e);
        siftDown(0);
        return ret;
    }

    public int getSize() { return data.size(); }

    public boolean isEmpty() { return getSize() == 0; }

    @Override
    public String toString() { return data.toString(); }

    public static void main(String[] args) {
        MinHeap<Integer> heap = new MinHeap<>();
        Integer[] inputSeq = {3, 10, 6, 4, 8, 7, 1, 5, 2, 9};

        // test insert
        for (int e : inputSeq) {
            heap.insert(e);
            log("insert " + e + " -> " + heap.toString());
        }

        // test extractMax
        while (!heap.isEmpty()) {
            int max = heap.extractMin();
            log("extract " + max + " -> " + heap.toString());
        }

        // test heapify
        log(new MinHeap<>(inputSeq));
    }
}
