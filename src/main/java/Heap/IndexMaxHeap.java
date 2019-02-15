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
*     1. data 数组丝毫不变，变的只是 indexes 中元素的顺序。
*     2. 无论是 data，还是 indexes 都不构成堆，真正构成堆的是这个数组：
*        data[indexes[1]], data[indexes[2]], data[indexes[3]], ..., data[indexes[n]]
*        换句话说，我们可以这样理解 indexes —— 将 data 整理成一个最大堆后，把堆中的每个元素用它们在 data 中的索引值来替换，
*        这样构成的数组就是 indexes，最后再让 data 的内容再恢复到最初状态。
*
*   - 这样构建出来的索引堆具有以下优势：
*     1. 因为交换的是 indexes 中的元素，数据结构简单（只是单纯的 int），不会有性能问题。
*     2. 因为交换的是 indexes 中的元素，而 data 中元素的索引不变，因此语义不会丢失，仍然可以通过随机访问取到对应的元素。
*     3. 另外，索引堆比普通堆在功能上更强大 —— 能够修改、查询堆中指定元素的值（change, getItem 方法）。因为普通堆只能取到堆
*        顶元素，而对堆中的其他元素都失去了控制。但是索引堆依靠索引，可以随时查询、修改堆中的任意元素，这也是索引堆的意义所在。
*
* - 索引堆的实现：
*   1. 在普通堆的基础上添加堆索引。
*   2. 在 shiftUp、shiftDown 时，比较的仍然是数据集中的元素，但交换的是堆索引中的元素。
*
* - 算法思想：
*   这种给加数据集入反向索引的思路其实是一种经典的算法优化思路，应用十分广泛：
*   1. 另一个典型应用是对排序算法的优化：在之前讲解的排序算法中，排序算法的复杂度主要在于其比较过程。例如，快排的复杂度是 O(nlogn)
*      是因为其比较操作的次数是 nlogn 这个级别的。但是当时我们并没有对交换操作进行优化。如果数据集中每个元素的结构很复杂、挪动成本
*      很高，则算法的整体性能就会大幅很低。此时可以采用反向索引的优化思路 —— 建立一个索引数组，在比较两个元素的时候通过索引取得真正
*      的数据进行比较，但交换的只是索引而非数据集中的元素。
*   2. 这种思路同样也被用于数据库的底层算法。
* */

public class IndexMaxHeap<E extends Comparable> {
    List<E> data;
    List<Integer> indexes;  // 堆索引（其元素个数 <= data.size()，因为 data 不变，变的是 indexes）

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
        siftUp(indexes.size() - 1);  // 对新添元素进行上浮（并不是对新添索引进行上浮）
    }

    public E extractMax() {
        E ret = getElement(0);
        int last = indexes.size() - 1;
        indexes.set(0, indexes.get(last));
        indexes.remove(last);  // 去掉 indexes 中的最后一个元素，而 data 不变
        siftDown(0);  // 对第一个元素进行下沉
        return ret;
    }

    public void change(int i, E newE) {  // 修改堆中任意一个元素（最差情况下为 O(n + logn) = O(n)，相对于其他操作 O(logn) 来说并不理想，在下个版本中优化）
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

    public E getItem(int i) {  // 查询堆中任意一个元素
        if (i < 0 || i >= data.size())
            throw new IllegalArgumentException("getItem failed.");
        return data.get(i);  // 因为 data 不变，元素的索引语义不变，所以可以随时查询到。
    }

    public boolean isEmpty() {
        return indexes.size() == 0;
    }

    @Override
    public String toString() {
        return "Elements: " + data.toString() + "; Indexes: " + indexes.toString();
    }

    public static void main(String[] args) {
        log("---- Generating IndexMaxHeap by heapifying ----");
        Integer[] inputSeq = {15, 17, 19, 13, 22, 20};
        IndexMaxHeap<Integer> heap1 = new IndexMaxHeap<>(inputSeq);
        log(heap1);

        while (!heap1.isEmpty())
            log("Extracted: " + heap1.extractMax() + "; " + heap1.toString());


        log("\n---- Generating IndexMaxHeap by adding ----");
        IndexMaxHeap<Integer> heap2 = new IndexMaxHeap<>();
        for (int n : inputSeq)
            heap2.add(n);
        log(heap2);  // 生成的 indexes 可能与 heap1 中的不同，因为生成机制不同

        heap2.change(2, 999);  // 修改中间元素

        while (!heap2.isEmpty())
            log("Extracted: " + heap2.extractMax() + "; " + heap2.toString());
    }
}
