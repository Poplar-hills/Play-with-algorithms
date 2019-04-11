package SortingSpecialised;

import static Utils.Helpers.log;

/*
* 计数排序（Counting Sort）- 实现1
*
* - 计数排序不是比较排序（通过比较元素之间的大小进行排序，如归并排序、快排等），但排序的速度却快于任何比较排序算法。
* - 时间复杂度为 O(n+k)，n 是数组元素个数，k 是数组中的最大值（因为 k 跟 n 之间没有任何关联，因此不能合并）。
* - 空间复杂度为 O(k)，因为要开辟 k+1 大小的辅助空间。
* - 计数排序的局限性：
*   1. 只适合于元素取值范围较小的集合，若取值范围过大，不但时间复杂度变高，而且严重消耗空间。
*   2. 只适合于整数排序，若是小数或者非数字但可比较的元素则不适合。
*   注：这些局限性在桶排序中做了弥补。
* */

public class CountingSort1 {
    public static void sort(int[] arr) {
        countingSort(arr, findMax(arr));
    }

    private static int findMax(int[] arr) {
        int max = arr[0];
        for (int i = 0; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];
        return max;
    }

    public static void countingSort(int[] arr, int max) {
        int[] buckets = new int[max + 1];

        for (int i = 0; i < arr.length; i++)  // 统计 arr 中每个元素的出现次数，复杂度为 O(n)
            buckets[arr[i]]++;

        int i = 0;  // 填充 arr 的索引。因为最后 arr 的所有位置都会被填充，即 arr 被遍历，因此复杂度为 O(n)
        for (int n = 0; n < buckets.length; n++)  // 遍历 bucket 数组，复杂度为 O(k)
            for (int j = 0; j < buckets[n]; j++)
                arr[i++] = n;
    }

    public static void main(String[] args) {
        int[] arr = new int[] {4, 5, 2, 8, 2, 5, 5, 2, 5, 8};
        sort(arr);
        log(arr);
    }
}
