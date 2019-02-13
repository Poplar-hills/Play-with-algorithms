package Heap;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;
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
*          初始堆数据： [15, 17, 19, 13, 22, 20]
*          初始堆索引：  0   1   2   3   4   5
*     在将这个数组通过 heapify 整理为最大堆的过程中：
*       1. 画出堆索引的树结构
*       2. 找到最后一个非叶子节点2，对其进行 siftDown，比较2与其子节点5的大小
*       3. 因为数据中第2个元素19 < 第5个元素20，因此交换2和5在堆索引中的位置：
*                    0                     0
*                 /    \                /    \
*                1     2    ------>    1     5
*              /  \   /              /  \   /
*             3   4  5              3   4  2
*       4. 再对下一个非叶子节点1进行 siftDown，并如此往复直到整理完成（自己画一下这个过程）：
*          堆数据： [15, 17, 19, 13, 22, 20]
*          堆索引：  4   1   5   3   0   2
*
*   - 可见在整理完成之后，堆数据丝毫不变，变的只是堆索引，这样做：
*     1. 因为交换的是堆索引，而堆索引只是 int，数据结构简单，不会有性能问题。
*     2. 因为交换的是堆索引，而堆数据本身的数组索引不变，因此语义不会丢失，任然可以通过随机访问取到对应的元素。
*
* - 索引堆的实现：
*   1. 在普通堆的基础上添加堆索引。
*   2. 在 shiftUp、shiftDown 时，比较的仍然是元素，但交换的是堆索引。
* */

public class IndexMaxHeap<E extends Comparable> {
    List<E> data;
    List<Integer> indexes;  // 堆索引（其元素个数 <= data.size()，因为 data 不变，变的是 indexes），理解这里就理解了索引堆

    public IndexMaxHeap(int capacity) {
        data = new ArrayList<>(capacity);
        indexes = new ArrayList<>(capacity);
    }

    public IndexMaxHeap() {
        data = new ArrayList<>();
        indexes = new ArrayList<>();
    }

    public IndexMaxHeap(E[] arr) {
        data = new ArrayList<>(arr.length);
        for (E e : arr)
            data.add(e);

        indexes = new ArrayList<>(arr.length);  // 创建堆索引数组
        for (int i = 0; i < arr.length; i++)
            indexes.add(i);

        int lastNonLeafNodeIndex = getParentIndex(arr.length - 1);  // heapify
        while (lastNonLeafNodeIndex >= 0)
            siftDown(lastNonLeafNodeIndex--);
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
        while (k > 0 && getElement(getParentIndex(k)).compareTo(getElement(k)) < 0) {  // 比较的是堆中元素
            swap(indexes, k, getParentIndex(k));  // 交换的是堆索引
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < indexes.size()) {  // 注意这里是 < 堆索引的元素个数
            int i = getLeftChildIndex(k);
            if (i + 1 < indexes.size() && getElement(i).compareTo(getElement(i + 1)) < 0)
                i += 1;
            if (getElement(k).compareTo(getElement(i)) >= 0)
                break;
            swap(indexes, k, i);
            k = i;
        }
    }

    public void add(E e) {
        data.add(e);
        indexes.add(indexes.size());
        siftUp(indexes.size() - 1);  // 对 indexes 的最后一个元素进行上浮
    }

    public E extractMax() {
        E ret = getElement(0);
        indexes.set(0, indexes.get(indexes.size() - 1));
        indexes.remove(indexes.size() - 1);  // 去掉 indexes 的最后一个元素，而 data 不变
        siftDown(0);  // 对 indexes 的第一个元素进行下沉
        return ret;
    }

    public void change(int i, E newE) {  // 修改堆中元素（最差情况下为 O(n + logn) = O(n)，相对于其他操作 O(logn) 来说并不理想，在下个版本中优化）
        // 修改 data 中的元素
        data.set(i, newE);
        // 修改 indexes 中的该元素的索引位置
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
        return "Elements: " + data.toString() + "; Indexes: " + indexes.toString();
    }

    public static void main(String[] args) {
        Integer[] inputSeq = {15, 17, 19, 13, 22, 20};
        IndexMaxHeap<Integer> heap1 = new IndexMaxHeap<>(inputSeq);
        log(heap1);

        IndexMaxHeap<Integer> heap2 = new IndexMaxHeap<>();
        for (int n : inputSeq)
            heap2.add(n);
        log(heap2);  // 生成的 indexes 堆可能与 heap1 中的不同，因为生成机制不同

        heap2.change(2, 999);
        log(heap2);

        while (!heap2.isEmpty()) {
            log("Extracted: " + heap2.extractMax());
            log(heap2);
        }
    }
}
