package Heap;

import static Utils.Helpers.log;

/*
* 最大堆（Max Heap）
* */

public class MaxHeap<E extends Comparable<E>> {
    private E[] data;  // 这里使用静态数据结构实现了不能扩容的最大堆，若需要扩容则需使用 List，或自行为 data 添加 resize 方法
    private int size;

    public MaxHeap(int capacity) {
        data = (E[]) new Comparable[capacity];
    }

    public MaxHeap(E[] arr) {  // heapify
        int n = arr.length;

        data = (E[]) new Comparable[n];
        for (int i = 0; i < n; i++)
            data[i] = arr[i];

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

    private void swap(E[] arr, int i, int j) {
        if (i < 0 || i >= arr.length || j < 0 || j >= arr.length)
            throw new IllegalArgumentException("swap failed. Index is out of bounds.");
        E temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void siftUp(int k) {
        while (k > 0 && data[getParentIndex(k)].compareTo(data[k]) < 0) {
            swap(data, k, getParentIndex(k));
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < getSize()) {  // 只要左孩子的索引 < 元素个数就说明还没到达叶子节点，可以继续循环
            // 找到位于 k 的节点的左右孩子中较大的那个的索引
            int i = getLeftChildIndex(k);
            if (i + 1 < getSize() && data[i + 1].compareTo(data[i]) > 0)  // i+1 是右孩子的索引，i+1 < getSize() 是要保证有右孩子
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
        data[data.length - 1] = e;
        siftUp(getSize() - 1);
    }

    public E extractMax() {
        E ret = data[0];     // 先保存最大值
        int lastIndex = getSize() - 1;
        data[0] = data[lastIndex];  // 取数组的最后一个元素覆盖第一个元素
        data[lastIndex] = null;  // 移除最大值元素（注：以上这三行不能写成 data.set(0, data.remove(getSize() - 1)); 因为当 data 中只剩一个元素时，remove 掉之后就无法再 set）
        siftDown(0);          // 对新的第一个元素进行下沉操作
        return ret;
    }

    public E replace(E e) {
        E ret = findMax();
        data[0] = e;
        siftDown(0);
        return ret;
    }

    public int getSize() { return data.length; }

    public boolean isEmpty() { return getSize() == 0; }

    @Override
    public String toString() {
        return data.toString();
    }

    public static void main(String[] args) {
        Integer[] inputSeq = {3, 10, 6, 4, 8, 7, 1, 5, 2, 9};
        MaxHeap<Integer> heap = new MaxHeap<>(inputSeq.length);

        // test insert
        for (int e : inputSeq) {
            heap.insert(e);
            log("insert " + e + " -> " + heap.toString());
        }

        // test extractMax
        while (!heap.isEmpty()) {
            int max = heap.extractMax();
            log("extract " + max + " -> " + heap.toString());
        }

        // test heapify
        log(new MaxHeap<>(inputSeq));
    }
}
