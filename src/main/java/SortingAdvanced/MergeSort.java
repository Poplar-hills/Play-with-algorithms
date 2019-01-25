package SortingAdvanced;

import java.util.Arrays;

import static Utils.Helpers.*;

/*
* 归并排序（Merge Sort）
* - 复杂度是 O(nlogn)
* - 归并排序递归地：
*   1. 将数组进行二分（从上到下）
*   2. 将二分后的左右两部分分别进行排序
*   3. 将排序后的左右两部分归并到上一层（从下到上）
*
*              不断进行二分                第一次归并                第二次归并                第三次归并
*   Level0   8 6 2 3 1 5 7 4          8 6 2 3 1 5 7 4          8 6 2 3 1 5 7 4          1 2 3 4 5 6 7 8
*   Level1   8 6 2 3|1 5 7 4   --->   8 6 2 3|1 5 7 4   --->   2 3 6 8|1 4 5 7   --->
*   Level2   8 6|2 3|1 5|7 4          6 8|2 3|1 5|4 7
*   Level3   8|6|2|3|1|5|7|4
*
* - 归并排序的思想：
*   - 有 n 个元素的数组可以进行 log(n) 次二分操作，共分出 log(n) 个层级。
*   - 如果每层的排序和归并过程能在 O(n) 的复杂度内完成，则该算法的整体复杂度就是 O(nlogn)。
*   注：这个思想也是所有 O(nlogn) 复杂度的算法的来源 —— 通过二分获得 log(n) 个层级，在每层内使用 O(n) 的算法来做事情。
*
* - 归并排序的优化：
*   对于近乎有序的数组，归并排序要慢于插入排序，因为归并排序不能像插入排序那样退化成 O(n) 的复杂度。但是仍然可以进行优化：
*   1.
* */

public class MergeSort {
    public static void sort(Comparable[] arr) {  // 归并排序中的递归是要对数组的每一段区域进行处理，因此设计递归函数时要传入左右边界
        sort(arr, 0, arr.length - 1);
    }

    // 递归地对 arr[l...r] 的范围（前闭后闭）进行排序
    private static void sort(Comparable[] arr, int l, int r) {
        if (l >= r) return;          // 递归终止条件
        int mid = (r - l) / 2  + l;  // 也可以写成 (l + r) / 2，但是可能整型溢出
        sort(arr, l, mid);
        sort(arr, mid + 1, r);
        merge(arr, l, mid, r);       // 递归到底后再从底往上进行归并（每次归并）
    }

    // 将 arr[l, mid] 和 arr[mid + 1, r] 这两部分进行归并
    private static void merge(Comparable[] arr, int l, int mid, int r) {
        // 创建辅助数组（空间换时间）
        Comparable[] aux = Arrays.copyOfRange(arr, l, r + 1);

        // 进行归并：需要3个索引 i, j, k
        int i = l, j = mid + 1;         // i 指向左半部分的起始索引 l；j 指向右半部分起始索引 mid + 1
        for (int k = l; k <= r; k++) {  // k 指向 arr[l, r] 中的每个位置
            if (i > mid) {              // 如果左半部分元素已经全部处理完毕
                arr[k] = aux[j - l]; j++;
            }
            else if (j > r) {           // 如果右半部分元素已经全部处理完毕
                arr[k] = aux[i - l]; i++;
            }
            else if (aux[i - l].compareTo(aux[j - l]) < 0) {  // 左半部分所指元素 < 右半部分所指元素
                arr[k] = aux[i - l]; i++;
            }
            else {                      // 左半部分所指元素 >= 右半部分所指元素
                arr[k] = aux[j - l]; j++;
            }
        }
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        log(Arrays.toString(arr));
        sort(arr);
        log(Arrays.toString(arr));
    }
}
