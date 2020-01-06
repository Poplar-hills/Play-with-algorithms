package SortingAdvanced;

import MinimumSpanningTree.AuxiliaryDataStructure.MinHeap;

import java.util.Collections;
import java.util.PriorityQueue;

import static Utils.Helpers.log;

/*
 * 练习：从有 n 个元素的数组中找到第 k 小的元素 - 采用堆排序求解
 *
 * - 注意是找第 k 小的元素（不是第 k 大的元素，与 L215_LargestElementInArray 不同）
 *
 * - 下面代码中的泛型语法 SEE: https://stackoverflow.com/questions/20938318/sorting-an-array-of-comparable-interface
 * */

public class Exercise_KthSmallestElement_HeapSelect {
    // 解法1：使用最小堆为数组整体排序，然后再 extractMin k-1 次
    // 复杂度：在有 heapify 方法的情况下为 O(n+klogn)，否则为 O(nlogn)。
    public static <T extends Comparable<T>> T minHeapSelect(T[] arr, int k) {
        MinHeap<T> heap = new MinHeap<>(arr);  // heapify，复杂度 O(n)
        for (int i = 0; i < k - 1; i++)        // extractMin k 次，复杂度 O(klogn)
            heap.extractMin();
        return heap.extractMin();
    }

    // 解法2：使用最大堆，保持堆大小为 k，遍历完成后堆顶就是第 k 小的元素
    // 复杂度：往堆中添加时 O(nlong)，extractMax n-k 次是 O((n-k)logn) ∴ 整体为 O((2n-k)logn)。
    public static <T extends Comparable<T>> T maxHeapSelect(T[] arr, int k) {
        PriorityQueue<T> pq = new PriorityQueue<>(k + 1, Collections.reverseOrder());  // 给 PriorityQueue 配置 comparator 用作最大堆
        for (T e : arr) {
            pq.add(e);          // PriorityQueue 没有 heapify 方法，需要手动 insert，复杂度为 O(nlogn)
            if (pq.size() > k)  // 当堆大小 > k 时 extractMax，不断将最小的 k 个元素留在堆中，复杂度为 O((n-k)logn)
                pq.poll();
        }
        return pq.poll();       // 最后再 extractMax 得到的即是第 k 小的元素（比它大的都已经 extract 出去了）
    }

    public static void main(String[] args) {
        Integer[] arr1 = {4, 2, 5, 1, 3, 6, 7, 8};
        Character[] arr2 = {'b', 'd', 'e', 'c', 'a'};
        log(minHeapSelect(arr1, 2));
        log(minHeapSelect(arr2, 2));

        Integer[] arr3 = {4, 2, 5, 1, 3, 6, 7, 8};
        Character[] arr4 = {'b', 'd', 'e', 'c', 'a'};
        log(maxHeapSelect(arr3, 2));
        log(maxHeapSelect(arr4, 2));
    }
}
