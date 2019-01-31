package SortingAdvanced;

/*
* - 快速排序被认为是20世纪最伟大的算法之一。
*
* - 快速排序过程：
*   1. 将第一个元素移动到正确的位置上；
*   2. 并使其前面的元素都小于该元素，其后面的元素都大于该元素；
*   3. 再分别对其前面的、后面的元素进行快速排序（递归过程）
*
* - partition 过程：
*      4  6  2  3  5  7  1  8   原始数组，轩第一个元素作为比较元素
*      4  6  2  3  5  7  1  8   因为 6 > 4，所以放着不动
*      4  2  6  3  5  7  1  8   因为 2 < 4，所以对2和6进行 swap（因为6是大于4的第一个元素）
*      4  2  3  6  5  7  1  8   因为 3 < 4，所以对3和6进行 swap
*      4  2  3  6  5  7  1  8   因为 5 < 4，所以放着不动
*      4  2  3  6  5  7  1  8   因为 7 < 4，所以放着不动
*      4  2  3  1  5  7  6  8   因为 1 < 4，所以对1和6进行 swap
*      4  2  3  1  5  7  6  8   因为 8 > 4，所以放着不动
*      1  2  3  4  6  5  7  8   最后对4和1进行 swap（因为1是小于4的最后一个元素）
*      |_____|  |  |________|
*         |     |      |
*        < v    v     > v
* */

import static Utils.Helpers.*;

public class QuickSort {
    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(Comparable[] arr, int l, int r) {
        if (l >= r) return;
        int p = partition(arr, l, r);
        sort(arr, l, p - 1);
        sort(arr, p + 1, r);
    }

    // 返回 p, 使得 arr[l...p-1] 中的元素都 < arr[p]，arr[p, r] 中的元素都 > arr[p]
    private static int partition(Comparable[] arr, int l, int r) {
        Comparable v = arr[l];
        int j = l;  // j 指向小于 v 的最后一个元素的位置

        for (int i = l + 1; i <= r; i++) {
            if (arr[i].compareTo(v) < 0)
                swap(arr, i, ++j);  // 与大于 v 的第一个元素进行 swap
        }

        swap(arr, l, j);  // 将 v 放到正确的位置（j）上
        return j;
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        log(arr);
        sort(arr);
        log(arr);

        Integer[] arr1 = generateRandomIntArr(1000000);
        Integer[] arr2 = arr1.clone();
        timeIt(arr1, QuickSort::sort);  // 比 MergeSort 快三分之一左右，原因分析见 QuickSortOptimised
        timeIt(arr2, MergeSort::sort);
    }
}
