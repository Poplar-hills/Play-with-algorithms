package SortingAdvanced;

import SortingBasic.InsertionSort;

import static Utils.Helpers.*;

/*
* 快速排序（Quick Sort）：
*
* - 被认为是20世纪最伟大的算法之一。
*
* - 算法思路：
*   1. 选择一个元素作为标定元素（pivot）；
*   2. 将 pivot 移动到正确的位置上，并使其前面的元素都 < pivot，其后面的元素都 > pivot；
*   3. 再对 pivot 之前和之后的元素分别递归地进行快速排序。
*   注：其中第2步叫做 partition 过程，是快排的核心思想，要记住！Exercise_kthSmallestElement 用的就是该思想。
*
* - partition 过程：
*   - 通常使用数组中的第一个元素作为 pivot，记作 v。
*   - 当 partition 过程进行时，数组结构如下：
*     [ v|---- ≤v ----|---- >v ----|...... ]
*       l            j              i    r
*     - 这里除了 l 和 r 还需要2个中间索引：
*       1. i 指向当前正在访问的元素；
*       2. j 指向 <= v 的最后一个元素。
*       这几个索引使得 arr[l+1...j] 区间中的元素都 <= v；arr[j+1...i-1] 区间中的元素都 > v。
*     - 对于若正在访问的元素 arr[i]：
*       1. 若 arr[i] > v，则放着不动就可以归入 > v 的区间；
*       2. 若 arr[i] <= v，则 swap(arr[i], arr[j+1]] 即可归入 <= v 的区间。
*     - 完整过程如下：
*       4  6  5  2  3  7  1  8   以4作为 pivot，访问 6，∵ 6 > v，∴ 放着不动，i++
*       lj i
*       4  6  5  2  3  7  1  8   访问 5，∵ 5 > v，∴ 放着不动，i++
*       lj    i
*       4  6  5  2  3  7  1  8   访问 2，∵ 2 <= v，∴ swap(i, j+1)，即对2和6进行 swap，i++，j++
*       lj       i
*       4  2  5  6  3  7  1  8   访问 3，∵ 3 <= v，∴ swap(i, j+1)，即对3和5进行 swap，i++，j++
*       l  j        i
*       4  2  3  6  5  7  1  8   访问 7，∵ 7 > v，∴ 放着不动，i++
*       l     j        i
*       4  2  3  6  5  7  1  8   访问 1，∵ 1 <= v，∴ swap(i, j+1)，即对1和6进行 swap，i++，j++
*       l     j           i
*       4  2  3  1  5  7  6  8   访问 8，∵ 8 > v，∴ 放着不动，i++，循环终止，swap(l, j)
*       l        j           i
*       1  2  3  4  5  7  6  8   第一次 partition 结束
*       |_____|  |  |________|
*          |     |      |
*         < v    v     > v
*
* - 复杂度分析：
*   - 快速排序中不断递归的过程实际上也是不断二分（非标准二分）的过程，因此是 O(logn) 的复杂度；
*   - 每次递归都会执行 partition 方法，该方法中会对范围内的元素进行遍历，因此是 O(n) 的复杂度；
*   - 综合起来就是 O(nlogn) 的复杂度。
* */

public class QuickSort {
    public static <T extends Comparable<T>> void sort(T[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static <T extends Comparable<T>> void sort(T[] arr, int l, int r) {
        if (l >= r) return;
        int p = partition(arr, l, r);
        sort(arr, l, p - 1);
        sort(arr, p + 1, r);
    }

    private static <T extends Comparable<T>> int partition(T[] arr, int l, int r) {
        T v = arr[l];          // 标定元素 pivot
        int j = l;                      // j 指向 <= v 的最后一个元素，∵ 最初没有元素 <= v，∴ 指向 l

        for (int i = l + 1; i <= r; i++)
            if (arr[i].compareTo(v) <= 0)
                swap(arr, i, j++ + 1);  // 与 > v 的第一个元素进行 swap

        swap(arr, l, j);                // 将 v 放到正确的位置上
        return j;                       // 返回 v 的索引
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        log(arr);
        sort(arr);
        log(arr);

        Integer[] arr1 = generateRandomIntArr(100000);
        Integer[] arr2 = arr1.clone();
        Integer[] arr3 = arr1.clone();
        timeIt(arr1, QuickSort::sort);       // 比 MergeSort 快不少，原因分析见 QuickSort2
        timeIt(arr2, MergeSort::sort);
        timeIt(arr3, InsertionSort::sort2);  // 对于完全随机的数据集，插入排序完全不是归并排序和快排的对手，不在一个量级上，慢几百倍
    }
}
