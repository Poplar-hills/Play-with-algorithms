package SortingBasic;

import java.util.Arrays;
import java.util.Random;

import static SortingBasic.Helpers.*;

/*
 * 插入排序（Insertion Sort）
 * - 插入排序的两个性质：
 *   1. 可以提前终止循环（相对于选择排序来说）
 *   2. 当数据越接近有序数组时，性能优势越明显（甚至比 O(nlogn) 的算法还高）
 *
 * - 插入排序在最差情况下和选择排序一样都是 O(n^2) 的复杂度，但是它们有一点最大不同：
 *   - 选择排序每次都会完整遍历后面的所有元素才能找到最小的。
 *   - 而插入排序可以提前终止循环（只要发现前一个元素小于当前元素就说明找到了应该插入的位置，可以停止循环）。
 *   因此插入排序的性能应该比选择排序更好，但是要看其具体实现：
 *   1. 如果实现如 sort1，每次都要 swap 的话，则性能还不如选择排序，因为一次 swap 有三次赋值 + 三次数组
 *      索引访问的开销。而择排序中每次只是比较，最后才赋值，因此开销小得多。
 *   2. 如果实现如 sort2，每次只比较大小，只有当找到应该插入的位置后才赋值，则性能开销就会小很多。
 *   通过 sort2 的优化之后，当数据越接近有序数组时，需要赋值的次数就越少，因此相对于其他排序算法的性能优势
 *   就越明显。对接近有序的数据进行排序其实是个很常见的需求，比如，
 * */

public class InsertionSrot {
    public static void sort1(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++)
            for (int j = i; j > 0 && arr[j - 1].compareTo(arr[j]) > 0; j--)
                swap(arr, j - 1, j);
    }

    public static void sort2(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++) {
            Comparable e = arr[i];  // 先将元素复制一份
            int j = i;              // j 表示 e 应该插入的位置
            for (; j > 0 && arr[j - 1].compareTo(e) > 0; j--)  // 如果前一个元素 > e，则将前一个元素替换当前元素
                arr[j] = arr[j - 1];
            arr[j] = e;             // 如果前一个元素 < e，则说明找到了 e 应该插入的位置，循环结束，将 e 赋值到该位置上
        }
    }

    public static void main(String[] args) {
        Integer[] arr1 = generateRandomIntArr(5);
        Integer[] arr2 = arr1.clone();

        System.out.println(Arrays.toString(arr1));
        sort1(arr1);
        sort2(arr2);
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));
    }
}
