package SortingAdvanced;

import SortingBasic.InsertionSort;

import java.util.Arrays;

import static Utils.Helpers.*;

/*
* 归并排序优化：
*
* - MergeSort 中实现的归并排序已经是 O(nlogn) 的复杂度，快于插入排序的 O(n^2)，但是对于接近有序的数组仍然比插入排序慢。
*   因为当数组接近有序时，归并排序不会像插入排序那样退化成接近 O(n) 的复杂度，因此会慢一些。
* - 对归并排序的一个常用的优化策略是在 sort 方法中，根据当前 arr[l, r] 中的元素个数 r - l 进行算法切换：当元素个数大于
*   某一水平时再采用归并排序，否则直接使用插入排序。这是因为当数据量较小时，整个数组近乎有序的概率比较大，因此使用插入更快。
* - 注：这种根据元素个数进行算法切换的优化对于很多高级排序算法都适用。另外，在 Java 中 HashMap 的实现里，初始哈希表中的
*   每个位置对应的是一个链表，当哈希冲突到达一定程度时，会转换成红黑树。
* */

public class MergeSort2 {
    public static <T extends Comparable<T>> void sort(T[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static <T extends Comparable<T>> void sort(T[] arr, int l, int r) {
        if (r - l <= 15) {  // 当元素个数 >= 15时，直接采用插入排序（这个优化只会提升对于近有序数组的排序性能）
            InsertionSort.sortRange(arr, l, r);
            return;
        }

        int mid = (r - l) / 2 + l;
        sort(arr, l, mid);
        sort(arr, mid + 1, r);

        if (arr[mid].compareTo(arr[mid + 1]) > 0)
            merge(arr, l, mid, r);
    }

    private static <T extends Comparable<T>> void merge(T[] arr, int l, int mid, int r) {
        T[] aux = Arrays.copyOfRange(arr, l, r + 1);
        int i = l, j = mid + 1;

        for (int k = l; k <= r; k++) {
            if (i > mid)
                arr[k] = aux[j++ - l];
            else if (j > r)
                arr[k] = aux[i++ - l];
            else if (aux[i - l].compareTo(aux[j - l]) < 0)
                arr[k] = aux[i++ - l];
            else
                arr[k] = aux[j++ - l];
        }
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(20);
        log(arr);
        sort(arr);
        log(arr);

        /* 注意：下面两组测试会互相影响，可能是因为 JVM 的优化机制导致的，因此需要注掉一组单独运行另一组 */

        // 性能测试（随机数组）
//        Integer[] arr1 = generateRandomIntArr(10000);
//        Integer[] arr2 = arr1.clone();
//        timeIt(arr1, MergeSort::sort);
//        timeIt(arr2, MergeSort2::sort);  // 反而比普通的 MergeSort 慢非常多（why？？？？？）

        // 性能测试（几乎有序的数组）
        Integer[] arr3 = generateNearlyOrderedArr(100000, 10);
        Integer[] arr4 = arr3.clone();
        Integer[] arr5 = arr3.clone();
        timeIt(arr3, MergeSort::sort);
        timeIt(arr4, MergeSort2::sort);  // 比普通的 MergeSort 快不少
        timeIt(arr5, InsertionSort::sort2);  // InsertionSort 还是最快的（但数据集不同结果会不同）
    }
}
