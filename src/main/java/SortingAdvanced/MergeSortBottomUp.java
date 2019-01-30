package SortingAdvanced;

import java.util.Arrays;

import static Utils.Helpers.*;

/*
* - 归并排序的另一种实现思路：不用自顶向下的递归来实现归并，自底向上的迭代同样可以实现。
*       8|6|2|3|1|5|7|4
*       6 8|2 3|1 5|4 7
*       2 3 6 8|1 4 5 7
*       1 2 3 4 5 6 7 8
* - 两种实现的比较：
*   - 复杂度都是 O(nlogn)，都可以在1秒内轻松处理100万数量级的数据。但是统计性能上，自顶向下的递归的实现稍好一点。
*   - 而自底向上的迭代的实现有一个重要的特性 —— 没有使用数组的随机访问来获取元素，因此可以适用于对链表的排序（？？？）。
 * */

public class MergeSortBottomUp {
    public static void sort(Comparable[] arr) {
        for (int step = 1; step <= arr.length; step *= 2) {   // 模拟二分操作
            for (int i = 0; i + step < arr.length; i += step * 2) {  // 每次对两个 step 内的元素进行归并
                int mid = i + step - 1;
                int r = Math.min(i + step * 2 - 1, arr.length - 1);  // 剩余元素长度可能不够 step，因此取 min
                if (arr[mid].compareTo(arr[mid + 1]) > 0)     // 和 MergeSort 中进行相同的优化
                    merge(arr, i, mid, r);
            }
        }
    }

    private static void merge(Comparable[] arr, int l, int mid, int r) {  // merge 方法不变
        Comparable[] aux = Arrays.copyOfRange(arr, l, r + 1);
        int i = l, j = mid + 1;

        for (int k = l; k <= r; k++) {
            if (i > mid) {
                arr[k] = aux[j - l]; j++;
            }
            else if (j > r) {
                arr[k] = aux[i - l]; i++;
            }
            else if (aux[i - l].compareTo(aux[j - l]) < 0) {
                arr[k] = aux[i - l]; i++;
            }
            else {
                arr[k] = aux[j - l]; j++;
            }
        }
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        log(arr);
        sort(arr);
        log(arr);

        Integer[] arr1 = generateRandomIntArr(100000);
        Integer[] arr2 = arr1.clone();
        timeIt(arr1, MergeSortBottomUp::sort);
        timeIt(arr2, MergeSort::sort);
    }
}