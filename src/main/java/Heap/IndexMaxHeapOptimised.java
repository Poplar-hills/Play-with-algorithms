package Heap;

import java.util.Arrays;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
 * 带反向查找的的最大索引堆（Index Heap with Reverse Retrieval）
 *
 * - 问题：
 *   - IndexMaxHeap 的实现中，insert、extractMax 方法的复杂度为 O(logn)，只有 change 是 O(n)，成了堆操作的性能短板。
 *   - 具体来说，在 change 过程中，需要通过遍历找到指定元素的索引 i 在 indexes 中的位置，这个过程最坏情况下是 O(n) 的复杂度。
 *
 * - 优化：
 *   - 仍然采用空间换时间的思想，将要查找的结果提前维护在一个数组中，这样在需要用的时候就可以以 O(1) 的复杂度来查找了，从而
 *     使得 change 方法的总体复杂度为 O(logn)。
 *   - 具体来说，建立"反向索引" reverse 数组：
 *            data: [15, 17, 19, 13, 22, 20]
 *         indexes: [4,  2,  5,  3,  0,  1]   -- 维护 data 中每个元素在最大堆中的位置
 *         reverse: [4,  5,  1,  3,  0,  2]   -- 维护 data 中每个元素的索引在 indexes 中的位置
 *   - 因为 reverse[i] 表示索引 i 在 indexes 中的位置，可知以下性质：
 *     1. 若 j = indexes[i]，则 reverse[j] = i
 *        比如 indexes 中第1个位置上是2，则 reverse 中第2个位置上是1。
 *        因此若要让 indexes[2] = 0，则需同时维护 reverse[0] = 2。
 *     2. indexes[reverse[i]] == i
 *        把性质1中的 reverse[j] = i 带入 indexes[i]。
 *     3. reverse[indexes[i]] == i
 *        把性质1中的 j = indexes[i] 带入 reverse[j]。
 * */

public class IndexMaxHeapOptimised<E extends Comparable<E>> {
    private E[] data;
    private int[] indexes;
    private int[] reverse;  // 反向索引数组
    private int size;

    public IndexMaxHeapOptimised(int capacity) {
        data = (E[]) new Comparable[capacity];
        indexes = new int[capacity];
        reverse = new int[capacity];
        size = 0;

        for (int i = 0; i < capacity; i++)
            reverse[i] = -1;  // reverse 中的元素都初始化为 -1，因为最开始 data 中没有元素
    }

    public IndexMaxHeapOptimised(E[] arr) {
        int n = arr.length;
        data = (E[]) new Comparable[n];
        indexes = new int[n];
        reverse = new int[n];

        for (int i = 0; i < n; i++) {
            data[i] = arr[i];
            indexes[i] = i;
            reverse[i] = i;
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

    private E getElement(int i) {
        return data[indexes[i]];
    }

    private void swapIndexes(int i, int j) {
        swap(indexes, i, j);      // indexes 中索引 i 和 j 上的值发生了变化
        reverse[indexes[i]] = i;  // 修改 indexes 之后要对应地维护 reverse（见上面介绍中的性质1）
        reverse[indexes[j]] = j;
    }

    private boolean contains(int i) {  // 检查索引 i 所在的位置是否被占用（存在元素）
        if (i < 0 || i >= data.length)
            throw new IllegalArgumentException("contains failed. Index out of bounds.");
        return reverse[i] != -1;  // 注意这里不能用 data[i] 或 indexes[i] 检查，因为 extract 之后 data 和 indexes 中的元素不会变，只有 reverse 中的会被置为-1
    }

    private void siftUp(int k) {
        while (k > 0 && getElement(getParentIndex(k)).compareTo(getElement(k)) < 0) {
            int p = getParentIndex(k);
            swapIndexes(k, p);
            k = p;
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < size) {
            int i = getLeftChildIndex(k);
            if (i + 1 < size && getElement(i + 1).compareTo(getElement(i)) > 0)
                i += 1;
            if (getElement(k).compareTo(getElement(i)) >= 0)
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
        reverse[i] = size;  // 维护 reverse（见上面介绍中的性质1）
        size++;
        siftUp(size - 1);
    }

    public E extractMax() {
        E ret = getElement(0);  // 返回的是 data 中的最大值（但是不从 data 中删除，只删除 indexes 中的对应索引）
        swapIndexes(0, size - 1);  // 将 indexes 中第0个元素 swap 到末尾，并维护 reverse
        reverse[indexes[size - 1]] = -1;  // 置为-1表示删除该索引（覆盖 swapIndexes 中的最后一句），最后再 size--，就相当于软删除了 data 中的对应元素
        size--;
        siftDown(0);
        return ret;
    }

    public int extractMaxIndex() {
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
        int j = reverse[i];  // 不再需要遍历，以 O(1) 的复杂度获得 i 在 indexes 中的索引
        siftUp(j);
        siftDown(j);
    }

    public E getItem(int i) {
        if (!contains(i))
            throw new IllegalArgumentException("getItem failed.");
        return data[i];
    }

    public int getSize() { return size; }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        return String.format("Elements: %s; Indexes: %s; Reverse: %s", Arrays.toString(data), Arrays.toString(indexes), Arrays.toString(reverse));
    }

    public static void main(String[] args) {
        Integer[] inputSeq = {15, 17, 19, 13, 22, 20};

        log("---- Testing heapify ----");
        IndexMaxHeapOptimised<Integer> heap1 = new IndexMaxHeapOptimised<>(inputSeq);
        log(heap1);
        while (!heap1.isEmpty())
            log("Extracted: " + heap1.extractMax() + "; " + heap1.toString());

        log("\n---- Testing insert ----");  // 测试将元素以乱序插入堆中
        IndexMaxHeapOptimised<Integer> heap2 = new IndexMaxHeapOptimised<>(inputSeq.length);
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
            log("Extracted: " + heap2.extractMax() + "; " + heap2.toString());
    }
}
