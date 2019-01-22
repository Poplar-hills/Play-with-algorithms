package SortingAdvanced;

import java.util.Arrays;

import static SortingBasic.Helpers.generateRandomIntArr;

public class MergeSort {
    public static void sort(Comparable[] arr) {
        mergeSort(arr, 0, arr.length - 1);
    }

    // 递归地对 arr[l...r] 的范围（前闭后闭）进行排序
    private static void mergeSort(Comparable[] arr, int l, int r) {
        if (l >= r)
            return;

        int mid = (r - l) / 2  + l;  // 也可以写成 (l + r) / 2，但是可能整型溢出
        mergeSort(arr, l, mid);
        mergeSort(arr, mid + 1, r);
        merge(arr, l, mid, r);
    }

    // 将 arr[l, mid] 和 arr[mid + 1, r] 这两部分进行归并
    private static void merge(Comparable[] arr, int l, int mid, int r) {
        // 创建辅助数组

        // 进行归并
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
