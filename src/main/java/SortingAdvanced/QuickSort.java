package SortingAdvanced;

/*
* 快速排序（Quick Sort）：
*
* - 被认为是20世纪最伟大的算法之一。
*
* - 算法思路：
*   1. 选择一个元素作为标定元素（pivot）；
*   2. 将 pivot 移动到正确的位置上，并使其前面的元素都 < pivot，其后面的元素都 > pivot；
*   3. 再对 pivot 之前和之后的元素分别递归地进行快速排序。
*   注：其中第2步叫做 partition 过程，是快速排序的核心步骤。
*
* - partition 过程：
*   - 通常使用数组中的第一个元素作为 pivot，记作 v。
*   - 当 partition 过程进行时，数组结构如下：
*     [ v|---- <v ----|---- >v ----|...... ]
*       l            j              i
*     - 这里需要3个索引：l 是 v 的索引；j 是 < v 的最后一个元素的索引；i 是当前正在访问的元素的索引。
*       即 arr[l+1...j] 区间中的元素都 < v；arr[j+1...i-1] 区间中的元素都 > v。
*     - 对于若正在访问的元素 arr[i]：
*       1. 若 arr[i] > v，则放着不动就可以归入 > v 的区间；
*       2. 若 arr[i] <= v，则 swap(arr[i], arr[j+1]] 即可归入 < v 的区间。
*     - 完整过程如下：
*       4  6  2  3  5  7  1  8   以4作为 pivot，访问 6，∵ 6 > v，∴ 放着不动，i++
*       lj i
*       4  6  2  3  5  7  1  8   访问 2，∵ 2 < v，∴ 对2和6进行 swap，i++，j++
*       lj    i
*       4  2  6  3  5  7  1  8   访问 3，∵ 3 < v，∴ 对3和6进行 swap，i++，j++
*       l  j     i
*       4  2  3  6  5  7  1  8   访问 5，∵ 5 > v，∴ 放着不动，i++
*       l     j     i
*       4  2  3  6  5  7  1  8   访问 7，∵ 7 > v，∴ 放着不动，i++
*       l     j        i
*       4  2  3  6  5  7  1  8   访问 1，∵ 1 < v，∴ 对1和6进行 swap，i++，j++
*       l     j           i
*       4  2  3  1  5  7  6  8   访问 8，∵ 8 > v，∴ 放着不动，i++，循环终止，对 l 和 j 所指元素进行 swap
*       l        j           i
*       1  2  3  4  5  7  6  8   一次 partition 结束
*       |_____|  |  |________|
*          |     |      |
*         < v    v     > v
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

    // 返回 p, 使得 arr[l...p-1] 中的元素都 < arr[p]；arr[p+1...r] 中的元素都 > arr[p]
    private static int partition(Comparable[] arr, int l, int r) {
        Comparable v = arr[l];      // 标定元素 pivot
        int j = l;                  // j 指向小于 v 的最后一个元素，即 arr[l+1...j] < v；arr[j+1...r] > v。最开始没有元素 < v，因此指向 l
        for (int i = l + 1; i <= r; i++) {
            if (arr[i].compareTo(v) < 0)
                swap(arr, i, ++j);  // 与大于 v 的第一个元素进行 swap
        }
        swap(arr, l, j);            // 将 v 放到正确的位置（j）上
        return j;                   // 返回 v 的索引
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(20);
        log(arr);
        sort(arr);
        log(arr);

        Integer[] arr1 = generateRandomIntArr(1000000);
        Integer[] arr2 = arr1.clone();
        timeIt(arr1, QuickSort::sort);  // 比 MergeSort 快三分之一左右，原因分析见 QuickSort2
        timeIt(arr2, MergeSort::sort);
    }
}
