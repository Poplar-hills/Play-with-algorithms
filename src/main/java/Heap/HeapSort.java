package Heap;

import static Utils.Helpers.*;

public class HeapSort {
    public static void sort1(Comparable[] arr) {  // 通过往一个空堆中 add 元素，再 extractMax 实现排序
        MaxHeap<Comparable> heap = new MaxHeap<>(arr.length);
        for (int i = 0; i < arr.length; i++)
            heap.add(arr[i]);
        for (int i = arr.length - 1; i >= 0; i--)
            arr[i] = heap.extractMax();
    }

    public static void sort2(Comparable[] arr) {  // 通过 heapify 数组来生成堆，再 extractMax 实现排序
        MaxHeap<Comparable> heap = new MaxHeap<>(arr);
        for (int i = arr.length - 1; i >= 0; i--)
            arr[i] = heap.extractMax();
    }

    public static void sort3(Comparable[] arr) {  // 不 new MaxHeap，而是原地进行堆排序
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
    private static void siftDown2(Comparable[] arr, int n, int k) {  // 对第 k 个元素在前 n 个元素的范围内进行下沉，n 之后是已经排好序的元素
        while (k * 2 + 1 < n) {  //
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
