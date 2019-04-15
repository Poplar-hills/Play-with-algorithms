package SortingAdvanced;

import MinimumSpanningTree.AuxiliaryDataStructure.MinHeap;

import java.util.Collections;
import java.util.PriorityQueue;

import static Utils.Helpers.log;

/*
 * 练习：从有 n 个元素的数组中找到第 k 小的元素 - 采用堆排序求解
 * */

public class Exercise_KthSmallestElement_HeapSelect {
    public static Comparable minHeapSelect(Comparable[] arr, int k) {  // 使用最小堆，extractMin k-1 次，总复杂度为 O(n+klogn)
        MinHeap<Comparable> heap = new MinHeap<>(arr);  // heapify，复杂度 O(n)
        for (int i = 0; i < k - 1; i++)                 // extractMin k 次，复杂度 O(klogn)
            heap.extractMin();
        return heap.extractMin();
    }

    public static Comparable maxHeapSelect(Comparable[] arr, int k) {  // 使用最大堆，extractMax n-k 次，总复杂度为 O((2n-k)logn)
        PriorityQueue<Comparable> pq = new PriorityQueue<>(k + 1, Collections.reverseOrder());  // 将 PriorityQueue 用作最大堆
        for (Comparable e : arr) {  // PriorityQueue 没有 heapify 方法，需要手动 insert，这步复杂度为 O(nlogn)
            pq.add(e);
            if (pq.size() > k)      // 当堆中元素个数 > k 时 extractMax，不断将小的元素留在堆中，这步复杂度为 O((n-k)logn)
                pq.poll();
        }
        return pq.poll();           // 最后再 extractMax 得到的即是第 k 小的元素（比它小的都已经 extract 出去了）
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
