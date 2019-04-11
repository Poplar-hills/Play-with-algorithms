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
* */

public class CountingSort2 {
    public static void sort(int[] arr) {
        countingSort(arr, findMinAndMax(arr));
    }

    private static int[] findMinAndMax(int[] arr) {
        int min = arr[0], max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
            if (arr[i] < min) min = arr[i];
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
                arr[i++] = n + min;  // 填充 arr 时把偏移量加回来
    }

    public static void main(String[] args) {
        int[] arr = new int[] {40, 50, 20, 80, 20, 50, 50, 20, 50, 80};
        sort(arr);
        log(arr);
    }
}
