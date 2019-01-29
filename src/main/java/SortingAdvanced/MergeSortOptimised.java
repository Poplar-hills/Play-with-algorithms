package SortingAdvanced;

import SortingBasic.InsertionSrot;

import java.util.Arrays;

import static Utils.Helpers.generateRandomIntArr;
import static Utils.Helpers.log;

/*
* - MergeSort 中实现的归并排序已经是 O(nlogn) 的复杂度，快于插入排序的 O(n^2)，但是对于接近有序的数组仍然比插入排序慢。
*   因为当数组接近有序时，归并排序不会插入排序那样退化成接近 O(n) 的复杂度。
* - 归并排序可优化的地方：
*   1. 在 sort 方法中，可以在 merge 之前加一个判断：如果 arr[mid] < arr[mid+1]，则说明 arr[mid+1, r] 中的所有元素都已
*      经大于 arr[l, mid] 中的所有元素，不需要再 merge 了。这是因为当两次 sort 完成之后，arr[l, mid] 和 arr[mid+1, r]
*      这两段区间已经各自是有序的了，此时若前者的最大值 < 后者的最小值，则说明两个区间之间已经有序。
*   2. 在 sort 方法中，根据当前 arr[l, r] 中的元素个数 r - l 进行算法切换：当元素个数大于某一水平时再采用归并排序，否则直接
*      使用插入排序。这是因为当数据量较小时，整个数组近乎有序的概率比较大，因此使用插入会更快。
*      注：这种根据元素个数进行算法切换的优化对于很多高级排序算法都适用。例如 Java 中 HashMap 的实现，初始哈希表中的每个位
*      置对应的是一个链表，当哈希冲突到达一定程度时，会转换成红黑树。
* */

public class MergeSortOptimised {
    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(Comparable[] arr, int l, int r) {
        if (r - l <= 15) {  // 优化 2：当元素个数 >= 15时，直接采用插入排序
            InsertionSrot.sortRange(arr, l, r);
            return;
        }

        int mid = (r - l) / 2 + l;
        sort(arr, l, mid);
        sort(arr, mid + 1, r);

        if (arr[mid].compareTo(arr[mid + 1]) < 0)  // 优化 1
            merge(arr, l, mid, r);
    }

    private static void merge(Comparable[] arr, int l, int mid, int r) {
        Comparable[] aux = Arrays.copyOfRange(arr, l, r + 1);
        int i = l, j = mid + 1;

        for (int k = l; k <= r; k++) {
            if (i > mid) {
                arr[k] = aux[j - l]; j++;
            }
            else if (j > r) {
                arr[k] = aux[i - l]; i++;
            }
            else if (aux[i - l].compareTo(aux[j - l]) < 0) {
                arr[k] = aux[i - l]; i++;
            }
            else {
                arr[k] = aux[j - l]; j++;
            }
        }
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        log(arr);
        sort(arr);
        log(arr);
    }
}
