package Heap;

import java.util.Arrays;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
* 最大堆（Max Heap）
* */

public class MaxHeap<E extends Comparable<E>> {
    private E[] data;  // 这里使用数组实现最大堆，因此不能扩容，若要扩容则需使用动态数据结构实现，或自行为 data 添加 resize 方法
    private int size;

    public MaxHeap(int capacity) {
        data = (E[]) new Comparable[capacity];
        size = 0;
    }

    public MaxHeap(E[] arr) {  // heapify
        int n = arr.length;

        data = (E[]) new Comparable[n];
        for (int i = 0; i < n; i++)
            data[i] = arr[i];
        size = n;

        int lastNonLeafNodeIndex = getParentIndex(n - 1);
        for (int i = lastNonLeafNodeIndex; i >= 0; i--)
            siftDown(lastNonLeafNodeIndex--);
    }

    private int getLeftChildIndex(int index) {
        return index * 2 + 1;
    }

    private int getParentIndex(int index) {
        if (index <= 0)
            throw new IllegalArgumentException("getParentIndex failed.");
        return (index - 1) / 2;
    }

    private void siftUp(int k) {
        while (k > 0 && data[getParentIndex(k)].compareTo(data[k]) < 0) {
            swap(data, k, getParentIndex(k));
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < size) {  // 如果左孩子存在（没越界）就继续循环
            // 找到位于 k 的节点的左右孩子中较大的那个的索引
            int i = getLeftChildIndex(k);
            if (i + 1 < size && data[i + 1].compareTo(data[i]) > 0)  // i+1 是右孩子的索引，i+1 < size 是要保证有右孩子
                i += 1;  // i 保存了左右孩子中值较大的那个的索引

            // 用父节点与较大的那个比，如果父节点大则 break loop，否则 swap（只有用较大的子节点跟父节点比才能保证 swap 之后换上来的新父节点比两个子节点都大，保证最大堆性质不被破坏）
            if (data[k].compareTo(data[i]) >= 0)
                break;

            swap(data, k, i);
            k = i;  // 记得最后要让 while 循环进入下一轮
        }
    }

    private E findMax() { return data[0]; }  // 找到最大值，相当于 priority queue 的 peek 方法

    public void insert(E e) {
        data[size] = e;
        size++;
        siftUp(size - 1);
    }

    public E extractMax() {
        E ret = data[0];           // 先保存最大值
        data[0] = data[size - 1];  // 取数组的最后一个元素覆盖第一个元素
        data[size - 1] = null;     // 移除最大值元素
        size--;
        siftDown(0);            // 对新的第一个元素进行下沉操作
        return ret;
    }

    public E replace(E e) {
        E ret = findMax();
        data[0] = e;
        siftDown(0);
        return ret;
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }

    public static void main(String[] args) {
        Integer[] inputSeq = {3, 10, 6, 4, 8, 7, 1, 5, 2, 9};
        MaxHeap<Integer> heap = new MaxHeap<>(inputSeq.length);

        log("---- Testing insert ----");
        for (int e : inputSeq) {
            heap.insert(e);
            log("insert " + e + " -> " + heap.toString());
        }

        log("\n---- Testing extractMax ----");
        while (!heap.isEmpty()) {
            int max = heap.extractMax();
            log("extract " + max + " -> " + heap.toString());
        }

        log("\n---- Testing heapify ----");
        log(new MaxHeap<>(inputSeq));
    }
}
