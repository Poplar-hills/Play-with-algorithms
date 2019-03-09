package MinimumSpanningTree;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.swap;

/*
* 最小索引堆（作为 PrimMST 的辅助数据结构）
*
* - 采用了增加 reverse 数据的设计，使 change 方法的复杂度为 O()
* */

public class IndexMinHeap<E extends Comparable> {
    private List<E> data;
    private List<Integer> indexes;
    private List<Integer> reverse;

    public IndexMinHeap() {
        data = new ArrayList<>();
        indexes = new ArrayList<>();
        reverse = new ArrayList<>();
    }

    public IndexMinHeap(E[] arr) {
        data = new ArrayList<>();
        indexes = new ArrayList<>();
        reverse = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            data.add(arr[i]);
            indexes.add(i);
            reverse.add(i);
        }

        int lastNonLeafNodeIndex = getParentIndex(arr.length - 1);
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

    private void swapIndexes(int i, int j) {
        swap(indexes, i, j);
        reverse.set(indexes.get(i), i);
        reverse.set(indexes.get(j), j);
    }

    private boolean contains(int i) { return i >= 0 && i < getSize(); }

    private E getElement(int i) { return data.get(indexes.get(i)); }

    private void siftUp(int k) {
        while (k > 0 && getElement(getParentIndex(k)).compareTo(getElement(k)) > 0) {
            swapIndexes(k, getParentIndex(k));
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < getSize()) {
            int i = getLeftChildIndex(k);
            if (i + 1 < getSize() && getElement(i + 1).compareTo(getElement(i)) < 0)
                i += 1;
            if (getElement(k).compareTo(getElement(i)) <= 0)
                break;
            swapIndexes(k, i);
            k = i;
        }
    }

    public void insert(E e) {
        data.add(e);
        indexes.add(getSize());
        reverse.add(getSize());
        siftUp(getSize() - 1);
    }

    public E extractMin() {
        E ret = getElement(0);
        int last = getSize() - 1;
        int lastIndex = indexes.get(last);
        indexes.set(0, lastIndex);
        reverse.set(lastIndex, 0);
        indexes.remove(last);
        siftDown(0);
        return ret;
    }

    public void change(int i, E newE) {
        if (!contains(i))
            throw new IllegalArgumentException("change failed.");
        data.set(i, newE);
        int j = reverse.get(i);
        siftUp(j);
        siftDown(j);
    }

    public int getSize() { return indexes.size(); }

    public boolean isEmpty() { return getSize() == 0; }

    @Override
    public String toString() {
        return String.format("Elements: %s; Indexes: %s; Reverse: %s", data.toString(), indexes.toString(), reverse.toString());
    }
}
