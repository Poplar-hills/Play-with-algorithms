package Heap;

import java.util.Arrays;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
* 索引堆（Index Heap）：
*
* - 我们之前实现的堆都是基于交换数组中的元素，这会带来两个问题：
*   1. 若元素的结构比较复杂（如超长字符串等），交换起来性能开销很大。
*   2. 交换元素之后元素索引的语义会丢失，因此很难再通过索引找到当初的元素。
*
*   例如：[15, 17, 19, 13, 22, 20] 中每个元素表示一个系统任务，元素索引表示的是系统进程的 id，比如：id=0 的任务的优先级
*   是15，id=1 的任务的优先级为17。当我们通过交换元素将这个数组转化成最大堆后，元素索引的语义被改变了，即元素索引不再表示某
*   个任务的系统进程 id，而表示的是该元素在堆中的位置，因此我们无法再通过系统进程 id 来取到该 id 最初对应的系统任务。而这个
*   问题可以通过索引堆来解决。
*
* - 索引堆的原理：
*   - 在数据集 data 之外构建一个"堆索引"数组 indexes，用于保存数据集中各个元素的索引：
*          data：    [15, 17, 19, 13, 22, 20]
*          indexes： [0,  1,  2,  3,  4,  5]
*     在将该数据集通过 heapify 整理为最大堆的过程中：
*       1. 画出初始 indexes 的树结构
*       2. 找到最后一个非叶子节点2，对其进行 siftDown
*       3. siftDown 过程中，因为 data 中索引为2的元素19 < 索引为5的元素20，因此交换2和5在 indexes 中的位置：
*                    0                     0
*                 /    \                /    \
*                1     2    ------>    1     5
*              /  \   /              /  \   /
*             3   4  5              3   4  2
*       4. 再对下一个非叶子节点1进行 siftDown，并如此往复直到整理完成（自己画一下这个过程）：
*          data：    [15, 17, 19, 13, 22, 20]
*          indexes： [4,  1,  5,  3,  0,  2]
*
*   - 可见在整理完成之后：
*     1. data 丝毫不变，变的只是 indexes 中元素的顺序。
*     2. 无论是 data，还是 indexes 都不构成堆，实际上真正构成堆的是这个数组：
*        data[indexes[0]], data[indexes[1]], data[indexes[2]], ..., data[indexes[n]]，如果画出对应的树来：
*                           22
*                        /     \
*                      17      20      - 如果没有 indexes，则 data 就会被整理成这样
*                    /   \    /
*                   13   15  19
*        另一种理解索引堆的方式：将 data 整理成一个最大堆后，把堆中的每个元素用它们在 data 中的索引值来替换，这样构
*        成的数组就是 indexes，最后再让 data 再恢复到最初状态即可。
*
*   - 这样构建出来的索引堆具有以下优势：
*     1. 因为交换的是 indexes 中的元素，数据结构简单（只是单纯的 int），不会有性能问题。
*     2. 因为交换的是 indexes 中的元素，而 data 中元素的索引不变，因此语义不会丢失，仍然可以通过随机访问取到对应的元素。
*     3. 另外，索引堆比普通堆在功能上更强大 —— 能够修改、查询堆中指定元素的值（change, getItem 方法）。因为普通堆只能取到堆
*        顶元素，而对堆中的其他元素都失去了控制。但是索引堆依靠索引，可以随时查询、更新堆中的任意元素。
*     👉 因此，如果某个问题中需要一种即能取到最大/小值，又能随时查询、更新堆中的任意元素的数据结构，则应使用索引堆。
*
* - 索引堆的实现：
*   1. 在普通堆的基础上添加堆索引。
*   2. 在 shiftUp、shiftDown 时，比较的仍然是数据集中的元素，但交换的是堆索引中的元素。
*
* - 背后的算法思想：
*   这种给加数据集入反向索引的思路其实是一种经典的算法优化思路，应用十分广泛：
*   1. 另一个典型应用是对排序算法的优化：在之前讲解的排序算法中，排序算法的复杂度主要在于其比较过程。例如，快排的复杂度是 O(nlogn)
*      是因为其比较操作的次数是 nlogn 这个级别的。但是当时我们并没有对交换操作进行优化。如果数据集中每个元素的结构很复杂、挪动成本
*      很高，则算法的整体性能就会大幅很低。此时可以采用反向索引的优化思路 —— 建立一个索引数组，在比较两个元素的时候通过索引取得真正
*      的数据进行比较，但交换的只是索引而非数据集中的元素。
*   2. 这种思路同样也被用于数据库的底层算法。
* */

public class IndexMaxHeap<E extends Comparable<E>> {
    private E[] data;
    private int[] indexes;  // 堆索引数组
    private int size;

    public IndexMaxHeap(int capacity) {
        data = (E[]) new Comparable[capacity];
        indexes = new int[capacity];
        size = 0;
    }

    public IndexMaxHeap(E[] arr) {
        int n = arr.length;
        data = (E[]) new Comparable[n];
        indexes = new int[n];  // 创建堆索引数组

        for (int i = 0; i < n; i++) {
            data[i] = arr[i];
            indexes[i] = i;  // 初始化堆索引数组
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

    private E getElement(int i) {  // 添加这个辅助方法作为 data 和 indexes 之间的桥梁
        return data[indexes[i]];
    }

    private void siftUp(int k) {
        while (k > 0 && getElement(getParentIndex(k)).compareTo(getElement(k)) < 0) {  // 比较的是堆中元素
            swap(indexes, k, getParentIndex(k));  // 交换的是堆索引
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < size) {  // 如果左孩子存在（没越界）就继续循环
            int i = getLeftChildIndex(k);
            if (i + 1 < size && getElement(i + 1).compareTo(getElement(i)) > 0)
                i += 1;
            if (getElement(k).compareTo(getElement(i)) >= 0)
                break;
            swap(indexes, k, i);
            k = i;
        }
    }

    public void insert(E e) {  // 索引堆的 insert 方法可以指定插入位置
//        if (i < 0 || i >= size || data[i] != null)
//            throw new IllegalArgumentException("insert failed. Target index already exists.");
        data[size] = e;
        indexes[size] = size;  // 同样需要添加到 indexes 中
        size++;
        siftUp(size - 1);  // 对新添元素进行上浮（并不是对新添索引进行上浮）
    }

    public E extractMax() {
        E ret = getElement(0);  // 返回的是 data 中的最大值（但是不从 data 中删除，只删除 indexes 中的对应索引）
        swap(indexes, 0, size - 1);  // 将 indexes 中第0个元素 swap 到末尾去，之后 size-- 后就相当于软删除了 data 中的对应元素
        size--;
        siftDown(0);
        return ret;
    }

    public void change(int i, E newE) {  // 更新堆中任意一个元素（索引堆的优势，普通堆做不到）
        // 更新 data 中的元素
        data[i] = newE;
        // 更新 indexes 中的该元素的索引位置（最差情况下为 O(n+logn) = O(n)，相对于其他操作 O(logn) 来说并不理想，在下个版本中优化）
        for (int j = 0; j < getSize(); j++)
            if (indexes[j] == i) {
                siftUp(j);
                siftDown(j);
                return;
            }
    }

    public E getItem(int i) {  // 查询堆中任意一个元素（索引堆的优势，普通堆做不到）
        if (i < 0 || i >= size)
            throw new IllegalArgumentException("getItem failed. Index is out of bounds");
        return data[i];  // 因为 data 不变，元素的索引语义不变，所以可以随时通过索引查询到（其实普通堆也可以通过索引找到元素，但是没有意义，因为内容可能已经改变）
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        return "Elements: " + Arrays.toString(data) + "; Indexes: " + Arrays.toString(indexes);
    }

    public static void main(String[] args) {
        Integer[] inputSeq = {15, 17, 19, 13, 22, 20};

        log("---- Testing heapify ----");
        IndexMaxHeap<Integer> heap1 = new IndexMaxHeap<>(inputSeq);
        log(heap1);
        while (!heap1.isEmpty())
            log("Extracted: " + heap1.extractMax() + "; " + heap1.toString());

        log("\n---- Testing insert ----");
        IndexMaxHeap<Integer> heap2 = new IndexMaxHeap<>(inputSeq.length);
        for (int e : inputSeq)
            heap2.insert(e);
        log(heap2);  // 生成的 indexes 可能与 heap1 中的不同，因为生成机制不同

        log("\n---- Testing change ----");
        heap2.change(2, 999);  // 修改中间元素
        log(heap2);
        while (!heap2.isEmpty())
            log("Extracted: " + heap2.extractMax() + "; " + heap2.toString());
    }
}
