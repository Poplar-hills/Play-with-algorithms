package SortingBasic;

import SortingAdvanced.*;

import static Utils.Helpers.*;

/*
 * 插入排序（Insertion Sort）：
 *
 * - 过程：
 *   1. 从前往后遍历元素（只遍历一次）
 *   2. 当发现后一个元素 < 前一个元素时，将后一个元素往前移动，直到前一个元素不再小于它时停止移动并插入进去。
 *
 * - 特性总结：
 *   - 复杂度为 O(n^2)，和选择排序、冒泡排序一样。
 *   - 可以提前终止比较循环（这是插入排序最大的特点）。
 *   - 因为可以提前终止循环，当数据越接近有序数组时，性能优势越明显（甚至比 O(nlogn) 的算法还快）。
 *
 * - 关于提前终止循环：
 *   - 在选择排序中，只有完整遍历后面的所有未排序的元素后才能找到最小元素；
 *   - 而在插入排序中，只要发现前一个元素不大于当前元素就说明找到了应该插入的位置，可以终止循环。
 *   - 因此插入排序的性能应该比选择排序更好，但是也要看其具体实现：
 *     1. 若实现如 sort，每次都要 swap 的话，则性能反而劣于选择排序（因为一次 swap 有三次赋值 + 三次数组索引访问；
 *        而择排序中每次只是比较，最后才赋值，因此开销小得多）。
 *     2. 若实现如 sort2，每次只比较大小，只有当找到应该插入的位置后才赋值，则性能会好很多。
 *   - 通过 sort2 的优化之后，当数据越接近有序数组时，需要赋值的次数就越少，因此相对于其他排序算法的性能优势就越明显，
 *     甚至比 O(nlogn) 的排序算法还要快。这种对近乎有序的数据进行排序其实是个很常见的需求，比如系统日志一般情况下都是
 *     按时间顺序生成的，但中间某几条的产生过程中可能碰到错误或者执行时间过长，因此成为了乱序的日志，此时用插入排序是最
 *     快的。这就是插入排序的实际意义。
 *
 * - 复杂度分析：
 *   - 最好情况下，数组完全有序，此时运行插入排序将是 O(n) 的复杂度，因为内层循环不需要执行。
 *   - 最坏情况下，数组完全逆序，此时运行插入排序将是 O(n^2) 的复杂度，因为要插入第2个元素时要与前1个元素比较，要插入第3个
 *     元素时要与前2个元素比较，插入第 n 个元素，要与前 n-1 个元素比较。因此比较次数是 1 + 2 + 3 + ... + (n - 1)，等差
 *     数列求和，结果为 (n^2 - n) / 2，所以复杂度为 O(n^2)。
 * */

public class InsertionSort {
    public static void sort1(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++)
            for (int j = i; j > 0 && arr[j].compareTo(arr[j - 1]) < 0; j--)
                swap(arr, j, j - 1);
    }

    public static void sort2(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++) {
            Comparable e = arr[i];    // 复制当前元素
            int j = i;                // 因为找到 e 应插入的位置的时候即是循环终止的时候，因此循环的索引的最终值 j 即是要插入的位置
            for (; j > 0 && arr[j - 1].compareTo(e) > 0; j--)
                arr[j] = arr[j - 1];  // 在循环结束之前，若前一个元素 > e，则将前一个元素复制到当前位置
            arr[j] = e;               // 若前一个元素 <= e，循环结束，此时找到了 e 应该插入的位置
        }
    }

    public static void sortRange(Comparable[] arr, int l, int r) {  // 对数组中的某一区间进行插入排序
        for (int i = l; i <= r; i++) {
            Comparable e = arr[i];
            int j = i;
            for (; j > 0 && arr[j - 1].compareTo(e) > 0; j--)
                arr[j] = arr[j - 1];
            arr[j] = e;
        }
    }

    public static void main(String[] args) {
        Integer[] arr1 = generateRandomIntArr(20);
        Integer[] arr2 = arr1.clone();
        log(arr1);

        // 功能测试
        sort1(arr1);
        sort2(arr2);
        log(arr1);
        log(arr2);

        // 性能测试
        Integer[] arr3 = generateRandomIntArr(10000);
        Integer[] arr4 = arr3.clone();
        timeIt(arr3, InsertionSort::sort1);
        timeIt(arr4, InsertionSort::sort2);

        Integer[] arr5 = generateNearlyOrderedArr(100000, 10);  // numOfSwap 趋近于0时，插入排序的复杂度趋近于 O(n)
        Integer[] arr6 = arr5.clone();
        Integer[] arr7 = arr5.clone();
        timeIt(arr5, MergeSort::sort);
        timeIt(arr6, QuickSort3Ways::sort);
        timeIt(arr7, InsertionSort::sort2);  // 对于近乎有序的数据集，插入排序比归并排序、快速排序都快
    }
}
