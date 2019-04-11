package SortingSpecialised;

import static Utils.Helpers.log;

/*
* 计数排序（Counting sort）
*
* - 计数排序不是比较排序（通过比较元素之间的大小进行排序，如归并排序、快排等），但排序的速度却快于任何比较排序算法。
* - 时间复杂度为 O(n+k)，n 是数组元素个数，k 是数组中的最大值。
*   - 因为 k 跟 n 之间没有任何关联，因此算法性能取决于
* - 空间复杂度胃 O(k)，因为要开辟 k+1 大小的辅助空间。
* - 适合于元素取值范围较小的集合的排序。
*
* - 实现：
*   1. 代码如 countingSort1，构造 buckets 数组，其中有 arr 中最大值+1个元素，然后填充 arr。
*   2. 但1中的实现有缺陷，对于 arr = [95, 94, 91, 98, 99] 这样的数组来说，仍然需要构造大小为100的 buckets 数组，因为 arr 中
*      最小元素为 91，因此从0到90的位置都浪费掉了，空间效率低下。对此优化方式是，以 arr 最大元素和最小元素之差+1作为 bucket 数组的长
*      度，同时用最小元素作为填充 arr 时的偏移量，代码见 countingSort2。
* */

public class CountingSort {
    public static void sort1(int[] arr) {
        countingSort1(arr, findMax(arr));
    }

    private static int findMax(int[] arr) {
        int max = arr[0];
        for (int i = 0; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];
        return max;
    }

    public static void countingSort1(int[] arr, int max) {
        int[] buckets = new int[max + 1];

        for (int i = 0; i < arr.length; i++)  // 统计 arr 中每个元素的出现次数，复杂度为 O(n)
            buckets[arr[i]]++;

        int i = 0;  // 填充 arr 的索引
        for (int n = 0; n < buckets.length; n++)
            for (int j = 0; j < buckets[n]; j++)
                arr[i++] = n;
    }

    public static void main(String[] args) {
        int[] arr = new int[] {4, 5, 2, 8, 2, 5, 5, 2, 5, 8};
        sort1(arr);
        log(arr);
    }
}
