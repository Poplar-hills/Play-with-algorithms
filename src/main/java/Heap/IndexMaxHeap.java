package Heap;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.swap;

/*
* 索引堆（Index Heap）：
*
* - 我们之前实现的堆都是基于交换数组中的元素，这会带来两个问题：
*   1. 若元素的结构比较复杂（如超长字符串等）则交换起来性能开销很大。
*   2. 交换元素之后，元素索引的语义会丢失，因此很难再通过索引找到当初的元素。
*
*   例如：[15, 17, 19, 13, 22, 20] 中每个元素表示一个系统任务，元素索引表示的是系统进程的 id，比如：id = 0的任务的优先级
*   是15，id = 1的任务的优先级为17。当我们通过交换元素将这个数组转化成最大堆后，元素索引的语义被改变了，即元素索引不再表示某
*   个任务的系统进程 id，而表示的是该元素在堆中的位置，因此我们无法再通过系统进程 id 来取到该 id 最初对应的系统任务。这个问
*   题可以通过索引堆来解决。
*
* - 索引堆的原理：
*   - 在普通堆的基础上另外维护一组"堆索引"（即每个元素在堆中的索引），在生成堆的过程中交换的是不再是堆元素而是堆索引：
*         初始堆数据： [15, 17, 19, 13, 22, 20]    ----->    堆数据： [15, 17, 19, 13, 22, 20]
*         初始堆索引：  0   1   2   3   4   5                堆索引：  4   1   5   3   0   2
*   - 可见在生成完堆之后，堆中数据的位置（即数组索引）是不变的，变的只是堆索引，这样做：
*     1. 因为交换的是堆索引，而堆索引只是 int，数据结构简单，不会有性能问题。
*     2. 因为交换的是堆索引，而堆中元素的数组索引不变，因此语义不会丢失，任然可以通过随机访问取到对应的元素。
*
* - 索引堆的实现：
*   1. 在普通堆的基础上添加堆索引。
*   2. 在 shiftUp、shiftDown 时，比较的仍然是元素，但交换的是堆索引。
* */

public class IndexMaxHeap<E extends Comparable> {
    List<E> data;

    public IndexMaxHeap(int capacity) {
        data = new ArrayList<>(capacity);
    }

    public IndexMaxHeap() {
        data = new ArrayList<>();
    }

    public IndexMaxHeap(E[] arr) {
        data = new ArrayList<>(arr.length);
        for (E e : arr)
            data.add(e);
        int lastNonLeafNodeIndex = getParentIndex(arr.length - 1);
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
        while (k > 0 && data.get(getParentIndex(k)).compareTo(data.get(k)) < 0) {
            swap(data, k, getParentIndex(k));
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < data.size()) {
            int i = getLeftChildIndex(k);
            if (i + 1 < data.size() && data.get(i).compareTo(data.get(i + 1)) < 0)
                i += 1;
            if (data.get(k).compareTo(data.get(i)) >= 0)
                break;
            swap(data, k, i);
            k = i;
        }
    }

    public void add(E e) {
        data.add(e);
        siftUp(data.size() - 1);
    }

    public E extractMax() {
        E ret = data.get(0);
        int lastIndex = data.size() - 1;
        data.set(0, data.get(lastIndex));
        data.remove(lastIndex);
        siftDown(0);
        return ret;
    }
}
