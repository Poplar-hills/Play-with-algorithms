package SortingBasic;

import java.util.Arrays;

import static SortingBasic.Helpers.generateRandomIntArr;
import static SortingBasic.Helpers.swap;

/**
 * 希尔排序（Shell Sort）：
 * - 是一种对插入排序的改进。插入排序对于近似有序的数组来说效率很高，可以达到接近 O(n) 的水平。但是平均情况下，插入
 *   排序效率还是比较低，主要原因是它一次只能将元素向前移一位。比如一个长度为 n 的数组，如果最小的元素如果恰巧在末尾，
 *   那么使用插入排序就需一步一步的向前比较和移动（共需要 n - 1 次）。
 *
 * - 排序过程：
 *   对于数组： [9, 1, 2, 5, 7, 4, 8, 6, 3, 5] 进行排序：
 *
 *   第1遍排序： 9  1  2  5  7  4  8  6  3  5    这遍排序中，gap1 = N / 2 = 5，即相隔距为5的元素组成一组，可以分为5组：
 *   gap = 5    |______________|                [9, 4], [1, 4], [2, 6], [5, 3], [7, 5]
 *                 |______________|             之后分别对每组进行插入排序。
 *                    |______________|
 *                       |______________|
 *                          |______________|
 *              4  1  2  3  5  9  8  6  5  7
 *
 *   第2遍排序： 4  1  2  3  5  9  8  6  5  7    这遍排序中，gap2 = gap1 / 2 = 2 (取整)，即相隔为2的元素组成一组，可以分为2组：
 *   gap = 2    |_____|_____|_____|_____|       [4, 2, 5, 8, 5], [1, 3, 6, 7, 9]
 *                 |_____|_____|_____|_____|    之后分别对每组进行插入排序。
 *              2  1  4  3  5  6  5  7  8  9
 *
 *   第3遍排序： 2  1  4  3  5  6  5  7  8  9    这遍排序中，gap3 = gap2 / 2 = 1，即相隔距为1的元素组成一组，只有一组。
 *   gap = 1    |__|__|__|__|__|__|__|__|__|    最后对这组进行插入排序，完成整个排序过程。
 *              1  2  3  4  5  5  6  7  8  9
 *
 * - 图解排序过程 SEE：http://www.cnblogs.com/idorax/p/6579332.html
 *
 * - 希尔排序为什么快：
 *   1. 插入排序的问题在于一次只能将元素向前移一位，因此在大数据量时很慢。而希尔排序通过将所有元素划分在几个区域内来提升
 *      插入排序的效率，这样可以让元素一次性的朝最终位置迈进一大步。
 *   2. 由于 gap 是递减的，最初几次 gap 取值较大，因此每个分组中的元素个数较少，排序速度较快；待到后期，gap 取值逐渐变
 *      小，子序列中的对象个数逐渐变多，但由于前面工作的基础，大多数对象已经基本有序，所以排序速度依然很快。
 * */

public class ShellSort {
    public static void sort(Comparable[] arr) {
        int n = arr.length;
        int h = 1;  // h 即为 gap
        while (h < n / 3) h = 3 * h + 1;  // 计算 gap: 1, 4, 13, 40, 121, 364, 1093...

        while (h >= 1) {  // h-sort the array
            for (int i = h; i < n; i++) {  // 对 arr[i], arr[i-h], arr[i-2*h], arr[i-3*h]... 使用插入排序
                Comparable e = arr[i];
                int j = i;
                for ( ; j >= h && e.compareTo(arr[j - h]) < 0 ; j -= h)
                    arr[j] = arr[j - h];
                arr[j] = e;
            }
            h /= 3;
        }
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
