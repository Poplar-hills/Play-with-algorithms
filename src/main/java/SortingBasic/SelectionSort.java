package SortingBasic;

import static Utils.Helpers.*;

/*
 * 选择排序（Selection Sort）：
 *
 * - 过程：进行 n-1 次迭代，每次迭代从未排序的元素中选出最小的，放到正确的位置上。
 * - 复杂度：O(n^2)。这个复杂度的排序算法一般没有什么实际价值（但插入排序有点不同）。
 * */

public class SelectionSort {
    public static void sort(Comparable[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {  // 只需要 n-1 次迭代即可完成排序（比如 [2,6,4] 只需2次迭代）
            int minIndex = i;
            for (int j = i; j < arr.length; j++)
                if (arr[j].compareTo(arr[minIndex]) < 0)
                    minIndex = j;
            swap(arr, i, minIndex);
        }
    }

    public static void main(String[] args) {
        Integer[] arr1 = generateRandomIntArr(5);
        Character[] arr2 = generateRandomCharArr(5);

        log(arr1);
        sort(arr1);
        log(arr1);

        log(arr2);
        sort(arr2);
        log(arr2);
    }
}
