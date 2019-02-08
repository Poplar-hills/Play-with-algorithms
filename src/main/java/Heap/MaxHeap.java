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

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < data.size()) {  // 只要左孩子的索引 < 元素个数，则说明还没到达叶子节点，可以继续循环
            // 找到位于 k 的节点的左右孩子中较大的那个的索引
            int i = getLeftChildIndex(k);
            if (i + 1 < data.size() && data.get(i).compareTo(data.get(i + 1)) < 0)  // i 是左孩子的索引，i + 1 即为右孩子的索引
                i += 1;  // i 保存了左右孩子中值较大的那个的索引

            // 用较大的那个与父节点比较，如果父节点大，则 break loop，否则 swap
            if (data.get(k).compareTo(data.get(i)) >= 0)
                break;

            swap(data, k, i);
            k = i;
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
        E ret = data.get(0);  // 先保存最大值
        int lastIndex = data.size() - 1;
        data.set(0, data.get(lastIndex));  // 取数组的最后一个元素覆盖第一个元素
        data.remove(0);
        siftDown(0);  // 对新的第一个元素进行下沉操作
        return ret;
    }

    public int getSize() { return data.size(); }

    public boolean isEmpty() { return getSize() == 0; }

    @Override
    public String toString() {
        return data.toString();
    }

    public static void main(String[] args) {
        MaxHeap<Integer> heap = new MaxHeap<>();
        Integer[] inputSeq = {3, 10, 6, 4, 8, 7, 1, 5, 2, 9};

        for (int e : inputSeq) {
            heap.add(e);
            log("add " + e + " -> " + heap.toString());
        }

        while (!heap.isEmpty()) {
            int max = heap.extractMax();
            log("extract " + max + " -> " + heap.toString());
        }
    }
}
