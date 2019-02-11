package Heap;

import static Utils.Helpers.*;

public class HeapSort {
    public static void sort1(Comparable[] arr) {
        MaxHeap<Comparable> heap = new MaxHeap<>(arr.length);
        for (int i = 0; i < arr.length; i++)
            heap.add(arr[i]);
        for (int i = arr.length - 1; i >= 0; i--)
            arr[i] = heap.extractMax();
    }

    public static void sort2(Comparable[] arr) {
        MaxHeap<Comparable> heap = new MaxHeap<>(arr);
        for (int i = arr.length - 1; i >= 0; i--)
            arr[i] = heap.extractMax();
    }

    public static void main(String[] args) {
        Integer[] arr1 = generateRandomIntArr(10);
        Integer[] arr2 = arr1.clone();
        log(arr1);
        sort1(arr1);
        log(arr1);
        sort2(arr2);
        log(arr2);

        // performance comparasion
        Integer[] arr3 = generateRandomIntArr(100000);
        Integer[] arr4 = arr3.clone();
        timeIt(arr3, HeapSort::sort1);
        timeIt(arr4, HeapSort::sort2);  // 通过 heapify 生成的最大堆要比通过往一个空堆里 add 快
    }
}
