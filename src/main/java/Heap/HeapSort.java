package Heap;

import static Utils.Helpers.*;

/*
* 堆排序（Heap Sort）：
*
* - 堆排序的3种实现方式：
*   1. insert + extractMax：
*      - 先往一个空堆中不断 insert 元素，再逐一 extractMax 并放入数组。
*      - 两者都是 O(n * logn) 的操作，因此整体复杂度是 O(nlogn)。
*   2. heapify + extractMax：
*      - 先 heapify 生成最大堆，再逐一 extractMax 并放入数组。
*      - 时间复杂度：O(n) + O(n * logn) = O(n + nlogn)；因为 nlogn > n，因此是总体是 O(nlogn) 级别。
*   3. 原地 heapify + swap + siftDown：
*      - 先原地 heapify 生成最大堆，再逐一将最大值与换到数组末尾正确的位置上，并对换到头部的元素 siftDown。
*      - 时间复杂度：O(n) + O(n * 1) + O(n * logn) = O(n + nlogn) = O(nlogn)。
*      - 演示 SEE：https://coding.imooc.com/lesson/71.html#mid=1468（0'28''）
*
*   - 前两种实现的思路都是先 new 出一个最大堆（不管是通过 insert 还是通过 heapify），然后不断 extractMax 来实现排序。
*     因为要通过 new 来新开辟 n 的空间，因此空间复杂度是 O(n)。而第3种不需要 new（不用额外空间），仅凭原地对数组元
*     素进行交换就实现了排序，因此空间复杂度是 O(1)，同时还省去了开辟新空间、往新空间里赋值的时间成本。
*
* - 性能上来说，不管哪种实现，heap sort 都要慢于 merge sort 和 quick sort，因此很少用于系统级别的排序算法。堆这种
*   数据结构更多的还是用于动态数据的维护，这是堆的"本职工作"。
* */

public class HeapSort {
    // 注：因为 MaxHeap 的类定义是 MaxHeap<E extends Comparable<E>>，所以下面的 static 方法中用到的泛型也必须有相应声明

    public static <E extends Comparable<E>> void sort1(E[] arr) {  // 第一种实现
        MaxHeap<E> heap = new MaxHeap<>(arr.length);
        for (int i = 0; i < arr.length; i++)
            heap.insert(arr[i]);
        for (int i = arr.length - 1; i >= 0; i--)
            arr[i] = heap.extractMax();
    }

    public static <E extends Comparable<E>> void sort2(E[] arr) {  // 第二种实现
        MaxHeap<E> heap = new MaxHeap<>(arr);
        for (int i = arr.length - 1; i >= 0; i--)
            arr[i] = heap.extractMax();
    }

    public static <E extends Comparable<E>> void sort3(E[] arr) {  // 第三种实现
        int n = arr.length;
        int lastNonLeafNodeIndex = (n - 1) / 2;

        for (int i = lastNonLeafNodeIndex; i >= 0; i--)  // heapify
            siftDown2(arr, n, i);

        for (int i = n - 1; i > 0; i--) {  // 从后往前遍历（第0个元素不用管）
            swap(arr, i, 0);            // 每次将第0个元素换到正确的排序位置上（例如最大值被换到数组末尾），同时最大堆被破坏
            siftDown2(arr, i, 0);      // 对第0个元素进行下沉以重建最大堆，注意：下沉范围 i（即最大堆元素个数）会每次减1
        }
    }

    // 比 MaxHeap 中的 siftDown 多了一个参数 n，用于控制下沉的范围
    private static <E extends Comparable<E>> void siftDown2(E[] arr, int n, int k) {  // 对第 k 个元素在前 n 个元素的范围内进行下沉，n 之后是已经排好序的元素
        while (k * 2 + 1 < n) {  // 注意这里是 < n
            int i = k * 2 + 1;
            if (i + 1 < n && arr[i + 1].compareTo(arr[i]) > 0)
                i += 1;
            if (arr[k].compareTo(arr[i]) >= 0)
                break;
            swap(arr, i, k);
            k = i;
        }
    }

    public static void main(String[] args) {
        Integer[] arr1 = generateRandomIntArr(10);
        Integer[] arr2 = arr1.clone();
        Integer[] arr3 = arr1.clone();
        log(arr1);

        sort1(arr1);
        log(arr1);
        sort2(arr2);
        log(arr2);
        sort3(arr3);
        log(arr3);

        // performance comparison
        Integer[] arr4 = generateRandomIntArr(100000);
        Integer[] arr5 = arr4.clone();
        Integer[] arr6 = arr4.clone();
        timeIt(arr4, HeapSort::sort1);
        timeIt(arr5, HeapSort::sort2);  // 比 sort1 快很多
        timeIt(arr6, HeapSort::sort3);  // 比 sort2 快一点
    }
}
