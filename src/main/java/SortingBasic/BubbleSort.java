package SortingBasic;

import java.util.Arrays;

import static Utils.Helpers.*;

/**
 * 冒泡排序（Bubble Sort）：
 *
 * - 过程：
 *   - 演示 SEE: https://algorithms.tutorialhorizon.com/optmized-bubble-sort-java-implementation/（这个是对的，
 *     手机应用 Algorithms 上的是错的）。
 *   - It compares each pair of adjacent items and swaps them if they are in the wrong order.
 *   - As the name suggests, the lighter (smaller) elements ‘bubble up’ to the top, or say, the heavy (bigger)
 *     elements sink down to the bottom.
 *   - 插入排序是把小的元素往数组前面移动，而冒泡排序是把大的元素往数组后面移动。
 *
 * - Pros: Very simple, just comparing and swapping elements.
 * - Cons: The complexity is O(n^2), as all the pairs are compared, even when the original array is already sorted.
 *   （这点跟选择排序类似，不能像插入排序那样提前结束循环）。
 * - 因为复杂度高（比同是 O(n^2) 的插入排序还要慢），所以冒泡排序不是一种实用的排序，几乎不会被用在实际工程中。
 */

public class BubbleSort {
    public static void sort1(Comparable[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {  // 外层循环控制排序遍数，只需要 n-1 次迭代即可完成排序（比如 [4,3,2,1] 只需3次迭代）
            for (int j = 1; j < arr.length - i; j++) {  // 内层循环控制一遍排序中的比较次数。最后 i 个元素是已经排过序的，不需要再比较，因此减去
                if (arr[j - 1].compareTo(arr[j]) > 0)
                    swap(arr, j - 1, j);
            }
        }
    }

    /**
     * 在 sort1 的实现中，每一遍排序都会比较所有元素 pair，不论当时数组是否已经是有序的了（即不能提前结束）。
     * 针对这点进行优化：提前结束的条件就是在一遍排序中是否 swap 过元素，如果没有则说明此时的数组已经是有序的了。
     */
    public static void sort2(Comparable[] arr) {
        boolean hasSwapped = true;
        for (int i = 0; i < arr.length && hasSwapped; i++) {
            hasSwapped = false;
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    swap(arr, j, j + 1);
                    hasSwapped = true;
                }
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

        // 性能测试
        Integer[] arr3 = generateRandomIntArr(10000);
        Integer[] arr4 = arr3.clone();
        timeIt(arr3, BubbleSort::sort1);
        timeIt(arr4, BubbleSort::sort2);  // 对于完全随机的数据集比 sort1 稍慢

        Integer[] arr5 = generateNearlyOrderedArr(10000, 10);  // numOfSwap 越小，sort2 比 sort1 快的越多
        Integer[] arr6 = arr5.clone();
        timeIt(arr5, BubbleSort::sort1);
        timeIt(arr6, BubbleSort::sort2);  // 对于近乎有序的数据集比 sort1 快很多
    }
}
