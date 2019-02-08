package Heap;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.*;

public class MaxHeap<E extends Comparable<E>> {
    private List<E> data;

    public MaxHeap(int capacity) {
        data = new ArrayList<>(capacity);
    }

    public MaxHeap() {
        data = new ArrayList<>();
    }

    private int getLeftChildIndex(int index) {
        return index * 2 + 1;
    }

    private int getRightChildIndex(int index) {
        return getLeftChildIndex(index) + 1;
    }

    private int getParentIndex(int index) {
        if (index <= 0)
            throw new IllegalArgumentException("getParentIndex failed.");
        return (index - 1) / 2;
    }

    private void siftUp(int k) {
        while (k > 0 && data.get(getParentIndex(k)).compareTo(data.get(k)) < 0) {
            swap(data, k, getParentIndex(k));
            k = getParentIndex(k);
        }
    }

    private void swap(List<E> list, int i, int j) {
        E temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public void add(E e) {
        data.add(e);
        siftUp(data.size() - 1);
    }

    public E extractMax() {
        E extracted = data.get(0);
        swap(data, 0, data.size() - 1);
        siftDown();
        return extracted;
    }

    private void siftDown() {
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public static void main(String[] args) {
        MaxHeap<Integer> heap = new MaxHeap<>();
        Integer[] inputSeq = {3, 10, 6, 4, 8, 7, 1, 5, 2, 9};
        log(inputSeq);

        for (int e : inputSeq) {
            heap.add(e);
            log("add " + e + ":");
            log(heap);
        }

        heap.extractMax();

    }
}
