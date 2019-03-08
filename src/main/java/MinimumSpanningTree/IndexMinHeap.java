package MinimumSpanningTree;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.swap;

/*
* 最小索引堆（作为 PrimMST 的辅助数据结构）
* */

public class IndexMinHeap<E extends Comparable> {
    private List<E> data;
    private List<Integer> indexes;
//    private List<Integer> reverse;

    public IndexMinHeap() {
        data = new ArrayList<>();
        indexes = new ArrayList<>();
//        reverse = new ArrayList<>();
    }

    private int getParentIndex(int index) {
        if (index <= 0)
            throw new IllegalArgumentException("getParentIndex failed.");
        return (index - 1) / 2;
    }

    private int getLeftChildIndex(int index) {
        return index * 2 + 1;
    }

    private void siftUp(int k) {
        while (k > 0 && getElement(getParentIndex(k)).compareTo(getElement(k)) > 0) {
            swap(indexes, k, getParentIndex(k));
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < indexes.size()) {
            int i = getLeftChildIndex(k);
            if (i + 1 < indexes.size() && getElement(i + 1).compareTo(getElement(i)) < 0)
                i += 1;
            if (getElement(k).compareTo(getElement(i)) <= 0)
                break;
            swap(indexes, k, i);
            k = i;
        }
    }

    private E getElement(int i) { return data.get(indexes.get(i)); }

    public void insert(E e) {
        data.add(e);
        indexes.add(indexes.size());
        siftUp(indexes.size() - 1);
    }

    public E extractMin() {
        E ret = getElement(0);
        int last = indexes.size() - 1;
        indexes.set(0, indexes.get(last));
        indexes.remove(last);
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

    public int getSize() { return indexes.size(); }

    public boolean isEmpty() { return getSize() == 0; }

    @Override
    public String toString() {
        return String.format("Elements: %s; Indexes: %s; Reverse: %s", data.toString(), indexes.toString(), reverse.toString());
    }
}
