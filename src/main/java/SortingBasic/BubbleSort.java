package SortingBasic;

import java.util.Arrays;

import static Utils.Helpers.*;

/**
 * 冒泡排序（Bubble Sort）
 * - 排序过程解释 SEE: https://algorithms.tutorialhorizon.com/optmized-bubble-sort-java-implementation/（这个是对的，
 *   手机应用 Algorithms 上的是错的）。
 * - 插入排序是把小的元素往数组前面移动，而冒泡排序是把大的元素往数组后面移动。
 * - Pros: Very simple, all it does is compare all the adjacent elements and swap them if they are in wrong order.
 * - Cons: The complexity is O(n^2), as all the pairs are compared, even when the original array is already sorted.
 *   （这点跟选择排序类似，因为不能像插入排序那样提前结束循环）
 * - 冒泡排序不是一种实用的排序，因为它的是 O(n^2) 量级的复杂度，并且比同是 O(n^2) 的插入排序还要差，因此几乎不会被用在实际工程中。
 */

public class BubbleSort {
    public static void sort1(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++)  // 外层循环控制排序遍数
            for (int j = 0; j < arr.length - i - 1; j++)  // 内层循环控制一遍排序中的比较次数。要减 i 是因为最后 i 个元素是已经排过序的，不需要再比较
                if (arr[j].compareTo(arr[j + 1]) > 0)     // 比较相邻的两个元素，将大的元素往后换
                    swap(arr, j, j + 1);
    }

    /**
     * 在 sort1 的实现中，每一遍排序都会比较所有元素 pair，不论当时数组是否已经是有序的了（即不能提前结束）。
     * 针对这点进行优化：提前结束的条件就是某一遍排序中是否 swap 过元素，如果没有则说明此时的数组已经是有序的了。
     */
    public static void sort2(Comparable[] arr) {
        boolean hasSwapped = true;
        for (int i = 0; i < arr.length && hasSwapped; i++) {
            hasSwapped = false;
            for (int j = 0; j < arr.length - i - 1; j++)
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    swap(arr, j, j + 1);
                    hasSwapped = true;
                }
        }
    }

    public static void main(String[] args) {
        Integer[] arr1 = generateRandomIntArr(5);
        Integer[] arr2 = arr1.clone();

        log(Arrays.toString(arr1));
        sort1(arr1);
        sort2(arr2);
        log(Arrays.toString(arr1));
        log(Arrays.toString(arr2));
    }
}
