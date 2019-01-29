package SortingBasic;

import java.util.Arrays;
import java.util.List;

import static Utils.Helpers.*;

/*
 * 插入排序（Insertion Sort）
 * - 插入排序的两个性质：
 *   1. 可以提前终止循环（相对于选择排序来说）
 *   2. 当数据越接近有序数组时，性能优势越明显（甚至比 O(nlogn) 的算法还高）
 *
 * - 插入排序在最差情况下和选择排序一样都是 O(n^2) 的复杂度，但是它们有一点最大不同：
 *   - 选择排序每次都会完整遍历后面的所有元素才能找到最小的。
 *   - 而插入排序可以提前终止循环（只要发现前一个元素小于当前元素就说明找到了应该插入的位置，可以停止循环）。因此插入排序
 *     的性能应该比选择排序更好，但是要看其具体实现：
 *     1. 如果实现如 sort1，每次都要 swap 的话，则性能还不如选择排序，因为一次 swap 有三次赋值 + 三次数组索引访问的开销。
 *        而择排序中每次只是比较，最后才赋值，因此开销小得多。
 *     2. 如果实现如 sort2，每次只比较大小，只有当找到应该插入的位置后才赋值，则性能开销就会小很多。
 *
 * - 通过 sort2 的优化之后，当数据越接近有序数组时，需要赋值的次数就越少，因此相对于其他排序算法的性能优势就越明显，甚至比
 *   O(nlogn) 的排序算法还要快。这种对近乎有序的数据进行排序其实是个很常见的需求，比如系统日志一般情况下都是按时间顺序生成
 *   的，但中间某几条的产生过程中可能碰到错误或者执行时间过长，因此成为了乱序的日志，此时用插入排序是最快的。这就是插入排序
 *   的实际意义。
 *
 * - 复杂度分析：
 *   - 最好情况下，数组完全有序，此时运行插入排序将是 O(n) 的复杂度，因为内层循环不需要执行。
 *   - 最坏情况下，数组完全逆序，此时运行插入排序将是 O(n^2) 的复杂度，因为要插入第2个元素时要与前1个元素比较，要插入第3个
 *     元素时要与前2个元素比较，插入第 n 个元素，要与前 n-1 个元素比较。因此比较次数是 1 + 2 + 3 + ... + (n - 1)，等差
 *     数列求和，结果为 (n^2 - n) / 2，所以复杂度为 O(n^2)。
 * */

public class InsertionSrot {
    public static void sort1(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++)
            for (int j = i; j > 0 && arr[j].compareTo(arr[j - 1]) < 0; j--)
                swap(arr, j, j - 1);
    }

    public static void sort2(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++) {
            Comparable e = arr[i];  // 复制当前元素
            int j = i;              // j 表示 e 应该插入的位置
            for (; j > 0 && arr[j - 1].compareTo(e) > 0; j--)  // 如果前一个元素 > e，则将前一个元素替换当前元素
                arr[j] = arr[j - 1];
            arr[j] = e;             // 如果前一个元素 < e，则说明找到了 e 应该插入的位置，循环结束，将 e 赋值到该位置上
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

        // 功能测试
        log(arr1);
        sort1(arr1);
        sort2(arr2);
        log(arr1);
        log(arr2);

        // 性能测试
        Integer[] arr3 = generateRandomIntArr(50000);
        timeIt(arr3, InsertionSrot::sort1);
        timeIt(arr3.clone(), InsertionSrot::sort2);
    }
}
