package SortingAdvanced;

import java.util.Random;

import static Utils.Helpers.*;

/*
* 练习：从有 n 个元素的数组中找到第 k 小的元素
*
* - 该问题有多种解法：
*   1. Sort：最直接的办法就是先将数组排序，然后取出第 k 个元素。复杂度是 O(nlogn)。
*   2. HeapSelect：即先创建一个最小堆，然后调用 extractMin 方法 k 次。复杂度是 O(n + klogn)。
*   3. QuickSelect：平均复杂度是 O(n)，最坏情况下是 O(n^2)。
*
* - 其中 QuickSelect 解法：
*   - 思路：partition 方法返回的 p 就是元素 v 最后应该被放到的索引位置。比如 arr = [4, 2, 5, 1, 3]，
*     k = 4（因为索引从0开始，所以 k = 4 就是要找到第3小的元素）。此时若 partition 中 v 取到了3，
*     则 partition 结束后得到 [1, 2, 3, 5, 4]，p == 2。而因为 k > p，因此只需要再在 arr[p+1...r]
*     中去递归查找即可，arr[l...p-1] 范围内的元素就可以不用管了。
*   - 因此可见 QuickSelect 只是做了局部快排。
*   - 复杂度分析：n + n/2 + n/4 + n/8 + ... + 1（每次递归时查找范围都会减半，但注意是在平均情况下），
*     等比数列求和可得 2n，因此是复杂度是 O(2n)。
* */

public class Exercise_KthSmallestElement {
    public static Comparable quickSelect(Comparable[] arr, int k) {
        return quickSelect(arr, 0, arr.length - 1, k - 1);  // k 是从0开始索引的, 即最小的元素是第0小元素, 如果希望 k 的语意是从1开始的, 只需要让 k - 1
    }

    private static Comparable quickSelect(Comparable[] arr, int l, int r, int k) {
        if (l == r) return arr[l];

        int p = partition(arr, l, r);

        if (k < p)  // 如果 k < p, 只需要在左半部分 arr[l...p-1] 中找第 k 小元素即可，而右半部分就不用管了
            return quickSelect(arr, l, p - 1, k);
        if (k > p)  // 如果 k > p, 只需要在右半部分 arr[p+1...r] 中找第 k-p-1 小元素即可，而左半部分就不用管了
            return quickSelect(arr, p + 1, r, k);
        else        // 如果当 partition 返回的切点 p == k 时，直接返回 arr[k] 即可，因为此时 arr[k] 已经被放到了正确的位置上
            return arr[k];
    }

    // 和 QuickSort 中的 partition 一样
    private static int partition(Comparable[] arr, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;
        swap(arr, l, vIndex);
        Comparable v = arr[l];
        int j = l;
        for (int i = l + 1; i <= r; i++) {
            if (arr[i].compareTo(v) < 0)
                swap(arr, i, ++j);
        }
        swap(arr, l, j);
        return j;
    }

    public static void main(String[] args) {
        Character[] arr1 = {4, 2, 5, 1, 3};
        log(quickSelect(arr1, 4));
    }
}
