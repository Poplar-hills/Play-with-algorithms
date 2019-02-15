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
 *   - 具体来说，在 change 过程中，需要通过遍历找到指定元素的索引 i 在 indexes 中的位置，这个过程是 O(n) 的复杂度。
 * - 优化：
 *   - 如果采用空间换时间的思想，将要查找的结果提前维护在一个数组中，这样在需要用的时候就可以以 O(1) 的复杂度来查找了。
 *   - 具体来说，建立"反向索引" reverse 数组：
 *            data: [15, 17, 19, 13, 22, 20]
 *         indexes: [4,  2,  5,  3,  0,  1]   -- 维护 data 中每个元素在最大堆中的位置
 *         reverse: [4,  5,  1,  3,  0,  2]   -- 维护 data 中每个元素的索引在 indexes 中的位置
 *   - 由此可推导：
 *     1. 若让 indexes[i] = j，则需让 reverse[j] = i；
 *        比如要 swap indexes 中的5和0，即让 indexes[2] = 0、indexes[4] = 2，则需让 reverse[0] = 2、reverse[2] = 4。
 *     2. 若让 indexes[reverse[i]] = i，则需让 reverse[indexes[i]] = i
 * */

public class IndexMaxHeapOptimised<E extends Comparable> {
    List<E> data;
    List<Integer> indexes;
    List<Integer> reverse;

    public IndexMaxHeapOptimised(int capacity) {
        data = new ArrayList<>(capacity);
        indexes = new ArrayList<>(capacity);
        reverse = new ArrayList<>(capacity);
    }

    public IndexMaxHeapOptimised() {
        data = new ArrayList<>();
        indexes = new ArrayList<>();
        reverse = new ArrayList<>();
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

        reverse = new ArrayList<>(arr.length);  // 初始化 reverse
        for (int i = 0; i < arr.length; i++)
            reverse.add(i);
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
            int p = getParentIndex(k);
            swap(indexes, k, p);
            reverse.set(indexes.get(k), k);
            reverse.set(indexes.get(p), p);
            k = p;
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
            reverse.set(indexes.get(k), k);
            reverse.set(indexes.get(i), i);
            k = i;
        }
    }

    public void add(E e) {
        data.add(e);
        indexes.add(indexes.size());
        reverse.add(reverse.size());
        siftUp(indexes.size() - 1);
    }

    public E extractMax() {
        E ret = getElement(0);
        int last = indexes.size() - 1;
        int lastIndex = indexes.get(last);
        indexes.set(0, lastIndex);
        reverse.set(lastIndex, 0);
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

    public boolean isEmpty() {
        return indexes.size() == 0;
    }

    @Override
    public String toString() {
        return String.format("Elements: %s; Indexes: %s; Reverse: %s", data.toString(), indexes.toString(), reverse.toString());
    }

    public static void main(String[] args) {
        log("---- Testing add ----");
        Integer[] inputSeq = {15, 17, 19, 13, 22, 20};
        IndexMaxHeapOptimised<Integer> heap1 = new IndexMaxHeapOptimised<>();
        for (int n : inputSeq)
            heap1.add(n);
        log(heap1);


        log("\n---- Testing extractMax ----");
        while (!heap1.isEmpty())
            log("Extracted: " + heap1.extractMax() + "\n" + heap1.toString());
    }
}
