package SortingAdvanced;

import java.util.Random;

import static Utils.Helpers.*;

/*
* 练习：从有 n 个元素的数组中找到第 k 小的元素
*
* - 该问题有多种解法：
*   1. Sort：最直接的办法就是先将数组排序，然后取出第 k 个元素，复杂度最低是 O(nlogn)。
*   2. HeapSelect：即先通过 heapify 将数组转换为一个最小堆（O(n) 的复杂度），然后调用 extractMin 方法 k 次，这样总体的复杂度是 O(n + klogn)。
*   3. QuickSelect：平均复杂度是 O(n)，最坏情况下是 O(n^2)。
*
* - 其中 QuickSelect 解法如下：
*   - 思路：
*     我们已知在快速排序中，partition 方法返回的是标定元素 pivot 最后应该被放置的索引位置，我们可根据这个信息来解决该问题。
*     比如有 arr = [4, 2, 5, 1, 3]，k = 4（即要找到第3小的元素（索引从0开始））。此时对数组进行 partition，若 pivot 取
*     到了3，则 partition 结束后数组为 [1, 2, 3, 5, 4]，并返回索引2。而因为 k > 2，因此只需要再在 arr[2+1...r] 的区间
*     中递归查找即可，而 arr[l...p-1] 区间内的元素就可以不用管了。
*   - 通过这种方式，我们简化了快速排序，即 QuickSelect 只是做了局部的 QuickSort，因此复杂度更低。
*   - 复杂度分析：n + n/2 + n/4 + n/8 + ... + 1（每次递归时查找范围都会减半，但注意是在平均情况下），等比数列求和可得 2n，
*     因此是复杂度是 O(2n)。
* */

public class Exercise_KthSmallestElement {
    public static Comparable quickSelect(Comparable[] arr, int k) {
        return sort(arr, 0, arr.length - 1, k - 1);
    }

    private static Comparable sort(Comparable[] arr, int l, int r, int k) {
        if (l == r) return arr[l];
        int[] ps = partition(arr, l, r);
        if (k >= ps[1])
            return sort(arr, ps[1], r, k);
        else if (k <= ps[0])
            return sort(arr, l, ps[0], k);
        else
            return arr[ps[0] + 1];
    }

    private static int[] partition(Comparable[] arr, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;
        Comparable v = arr[l];
        swap(arr, l, vIndex);
        int lt = l;
        int gt = r + 1;
        int i = l + 1;

        while (i < gt) {
            if (arr[i].compareTo(v) < 0) {
                swap(arr, i, lt + 1);
                lt++;
                i++;
            } else if (arr[i].compareTo(v) > 0) {
                swap(arr, i, gt - 1);
                gt--;
            } else {
                i++;
            }
        }
        swap(arr, lt, l);
        lt--;
        return new int[] {lt, gt};
    }

//    public static Comparable quickSelect(Comparable[] arr, int k) {
//        return quickSelect(arr, 0, arr.length - 1, k - 1);  // k 是从0开始索引的, 即最小的元素是第0小元素, 如果希望 k 的语意是从1开始的, 只需要让 k - 1
//    }
//
//    private static Comparable quickSelect(Comparable[] arr, int l, int r, int k) {
//        if (l == r) return arr[l];
//
//        int p = partition(arr, l, r);
//
//        if (k < p)  // 如果 k < p, 只需要在左半部分 arr[l...p-1] 中找第 k 小元素即可，而右半部分就不用管了
//            return quickSelect(arr, l, p - 1, k);
//        if (k > p)  // 如果 k > p, 只需要在右半部分 arr[p+1...r] 中找第 k-p-1 小元素即可，而左半部分就不用管了
//            return quickSelect(arr, p + 1, r, k);
//        else        // 如果当切点 p == k 时，直接返回 arr[k] 即可，因为此时 arr[k] 已经被放到了正确的位置上
//            return arr[k];
//    }
//
//    // 和 QuickSort 中的 partition 一样
//    private static int partition(Comparable[] arr, int l, int r) {
//        int vIndex = new Random().nextInt(r - l + 1) + l;
//        swap(arr, l, vIndex);
//        Comparable v = arr[l];
//        int j = l;
//        for (int i = l + 1; i <= r; i++) {
//            if (arr[i].compareTo(v) < 0)
//                swap(arr, i, ++j);
//        }
//        swap(arr, l, j);
//        return j;
//    }

    public static void main(String[] args) {
        Integer[] arr1 = {4, 2, 5, 1, 3, 6, 7, 8};
        log(quickSelect(arr1, 4));

        Character[] arr2 = {'b', 'd', 'e', 'c', 'a'};
        log(quickSelect(arr2, 4));
    }
}
