package SortingSpecialised;

import static Utils.Helpers.log;

/*
* 计数排序（Counting Sort）- 实现2
*
* - CountingSort1 的缺陷：
*   对于 arr = [95, 94, 91, 98, 99] 这样的数组来说，仍然需要构造大小为100的 buckets 数组，因为 arr 中最小元素为 91，
*   因此从0到90的位置都浪费掉了，空间效率低下。
*
* - 对此优化方式:
*   以 arr 最大元素和最小元素之差+1作为 bucket 数组的长度，同时用最小元素作为填充 arr 时的偏移量，代码见 countingSort2。
*
* - 复杂度：
*   时间和空间复杂度仍然是 O(n+k) 和 O(k)，但其中 k 变成了最大元素和最小元素之差。
*
* - 局限性：
*   CountingSort1 和 CountingSort2 都不是稳定排序（即排序后，等值的两个元素的相对顺序可能会被颠倒），可以进行进一步优化，
*   SEE: 微信搜索"漫画：什么是计数排序？"。
* */

public class CountingSort2 {
    public static void sort(int[] arr) {
        countingSort(arr, findMinAndMax(arr));
    }

    private static int[] findMinAndMax(int[] arr) {
        int min = arr[0], max = arr[0];
        for (int n : arr) {
            if (n > max) max = n;
            if (n < min) min = n;
        }
        return new int[] {min, max};
    }

    public static void countingSort(int[] arr, int[] minAndMax) {
        int min = minAndMax[0], max = minAndMax[1];
        int[] buckets = new int[max - min + 1];  // 以 arr 最大元素和最小元素之差+1作为 bucket 数组的长度

        for (int i = 0; i < arr.length; i++)
            buckets[arr[i] - min]++;  // 构造 bucket 数组时索引减去偏移量

        int i = 0;
        for (int n = 0; n < buckets.length; n++)
            for (int j = 0; j < buckets[n]; j++)
                arr[i++] = min + n;  // 填充 arr 时把偏移量加回来
    }

    public static void main(String[] args) {
        int[] arr = new int[] {40, 50, 20, 80, 20, 50, 50, 20, 50, 80};
        sort(arr);
        log(arr);
    }
}
