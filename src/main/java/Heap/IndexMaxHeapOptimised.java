package Heap;

import sun.awt.util.IdentityLinkedList;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;
import static java.util.Collections.swap;

/*
 * 带反向查找的的索引堆（Index Heap with Reverse Retrieval）
 *
 * - 问题：
 *   - IndexMaxHeap 的实现中，insert、extractMax 方法的复杂度为 O(logn)，而 change 为 O(n)，成了堆操作的性能短板。
 *   - 具体来说，在 change 过程中，需要通过遍历找到指定元素的索引 i 在 indexes 中的位置，这个过程是 O(n) 的复杂度。
 *
 * - 优化：
 *   - 如果采用空间换时间的思想，将要查找的结果提前维护在一个数组中，这样在需要用的时候就可以以 O(1) 的复杂度来查找了。
 *   - 具体来说，建立"反向索引" reverse 数组：
 *            data: [15, 17, 19, 13, 22, 20]
 *         indexes: [4,  2,  5,  3,  0,  1]   -- 维护 data 中每个元素在最大堆中的位置
 *         reverse: [4,  5,  1,  3,  0,  2]   -- 维护 data 中每个元素的索引在 indexes 中的位置
 *   - 由此可推导，因为 revers[i] 表示索引 i 在 indexes 中的位置：
 *     1. 若 j = indexes[i]，则 reverse[j] = i
 *        比如要让 indexes[2] = 0，即索引0在 indexes 中的位置发生了变化，此时需要维护 reverse[0] = 2。
 *     2. indexes[reverse[i]] == i
 *        因为 reverse[i] 表示索引 i 在 indexes 中的位置，所以 indexes 中 reverse[i] 位置上的元素就是 i。
 *     3. reverse[indexes[i]] == i
 *        结合1、2两条性质得到。
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

    private void swapIndexes(int i, int j) {
        swap(indexes, i, j);  // indexes 中索引 i 和 j 上的值发生了变化
        reverse.set(indexes.get(i), i);  // 修改 indexes 之后要对应地维护 reverse
        reverse.set(indexes.get(j), j);
    }

    private void siftUp(int k) {
        while (k > 0 && getElement(getParentIndex(k)).compareTo(getElement(k)) < 0) {
            int p = getParentIndex(k);
            swapIndexes(k, p);
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
            swapIndexes(k, i);
            k = i;
        }
    }

    private boolean contains(int i) {
        return i >= 0 && i < indexes.size();
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
        reverse.set(lastIndex, 0);  // 修改 indexes 之后对应地维护 reverse
        indexes.remove(last);
        siftDown(0);
        return ret;
    }

    public void change(int i, E newE) {
        if (!contains(i))
            throw new IllegalArgumentException("change failed.");
        data.set(i, newE);
        int j = reverse.get(i);  // 不再需要遍历，以 O(1) 的复杂度获得 i 在 indexes 中的索引
        siftUp(j);
        siftDown(j);
    }

    public E getItem(int i) {
        if (!contains(i))
            throw new IllegalArgumentException("getItem failed.");
        return data.get(i);
    }

    public boolean isEmpty() {
        return indexes.size() == 0;
    }

    @Override
    public String toString() {
        return String.format("Elements: %s; Indexes: %s; Reverse: %s", data.toString(), indexes.toString(), reverse.toString());
    }

    public static void main(String[] args) {
        log("---- Testing insert ----");
        Integer[] inputSeq = {15, 17, 19, 13, 22, 20};
        IndexMaxHeapOptimised<Integer> heap1 = new IndexMaxHeapOptimised<>();
        for (int n : inputSeq)
            heap1.add(n);
        log(heap1);

        log("\n---- Testing change ----");
        heap1.change(2, 999);
        log(heap1);

        log("\n---- Testing extractMax ----");
        while (!heap1.isEmpty())
            log("Extracted: " + heap1.extractMax() + "\n" + heap1.toString());
    }
}
