package SortingAdvanced;

import SortingBasic.InsertionSort;

import java.util.Arrays;

import static Utils.Helpers.*;

/*
* 归并排序（Merge Sort）：
*
* - 复杂度是 O(nlogn)
* - 归并排序递归地：
*   1. 将数组不断进行二分，直到不能再分（从上到下的过程）；
*   2. 不断将下一层的左右两部分归并到上一层（从下到上的过程）
*
*              不断进行二分                第一次归并                第二次归并                第三次归并
*   Level0   8 6 2 3 1 5 7 4          8 6 2 3 1 5 7 4          8 6 2 3 1 5 7 4          1 2 3 4 5 6 7 8
*   Level1   8 6 2 3|1 5 7 4   --->   8 6 2 3|1 5 7 4   --->   2 3 6 8|1 4 5 7   --->
*   Level2   8 6|2 3|1 5|7 4          6 8|2 3|1 5|4 7
*   Level3   8|6|2|3|1|5|7|4
*
*   另一种可视化方式：
*                                 8 6 2 3 1 5 7 4               ---
*         1st divide               /           \                 |
*                            8 6 2 3          1 5 7 4            |
*         2nd divide         /     \          /     \         分解过程
*                          8 6     2 3      1 5     7 4          |
*         3rd divide     /   \    /   \    /   \   /   \         |
*                       8    6   2    3   1    5  7    4        ---
*         1st conquer    \  /     \  /     \  /    \  /          |
*                        6 8      2 3      1 5     4 7           |
*         2nd conquer       \    /           \    /           归并过程
*                          2 3 6 8          1 4 5 7              |
*         3rd conquer           \            /                   |
*                              1 2 3 4 5 6 7 8                  ---
*
* - 归并排序的思想（重点）：
*   - 有 n 个元素的数组最多可进行 log(n) 次二分操作，共分出 log(n) 个层级（即树高为 log(n)）。
*   - 如果每层的排序和归并过程能在 O(n) 的复杂度内完成，则该算法的整体复杂度就是 O(nlogn)。
*   注：这个思想也是所有 O(nlogn) 复杂度的算法的来源 —— 通过二分获得 log(n) 个层级，在每层内使用 O(n) 的算法来做事情。
*
* - 优化：
*   在 sort 方法中，可以在 merge 之前加一个判断：如果 arr[mid] < arr[mid+1]，则说明 arr[mid+1, r] 中的所有元素都已
*   经大于 arr[l, mid] 中的所有元素，不需要再 merge 了。这是因为当两次 sort 完成之后，arr[l, mid] 和 arr[mid+1, r]
*   这两段区间已经各自是有序的了，此时若前者的最大值 < 后者的最小值，则说明两个区间之间已经有序。
* */

public class MergeSort {
    public static void sort(Comparable[] arr) {  // 归并排序中的递归是要对数组的每一段区域进行处理，因此设计递归函数时要传入左右边界
        sort(arr, 0, arr.length - 1);
    }

    // 递归地对 arr[l...r] 的范围（前闭后闭）进行排序
    private static void sort(Comparable[] arr, int l, int r) {
        if (l >= r) return;          // 异常及递归终止条件
        int mid = (r - l) / 2  + l;  // 也可以写成 (l + r) / 2，但是可能整型溢出
        sort(arr, l, mid);
        sort(arr, mid + 1, r);
        boolean alreadyOrdered = arr[mid].compareTo(arr[mid + 1]) > 0;
        if (alreadyOrdered)          // 若分解完之后该部分元素已经是有序的则不用再 merge（这个判断能带来不错的性能提升）
            merge(arr, l, mid, r);   // 递归到底后再从底往上进行合并
    }

    // 将 arr[l, mid 和 arr[mid + 1, r] 这两部分进行归并，此时这两部分都已经各自有序了
    private static void merge(Comparable[] arr, int l, int mid, int r) {
        // 创建辅助数组（空间换时间）
        Comparable[] aux = Arrays.copyOfRange(arr, l, r + 1);

        // 进行归并：需要3个索引 i, j, k
        int i = l, j = mid + 1;            // i 指向左半部分的起始索引 l；j 指向右半部分起始索引 mid + 1
        for (int k = l; k <= r; k++) {     // k 指向 arr[l, r] 中的每个位置
            if (i > mid) {                 // 如果左半部分元素已经全部处理完毕
                arr[k] = aux[j - l]; j++;  // 要减去 l 的偏移量（因为 aux 的范围是从 0 开始的）
            }
            else if (j > r) {              // 如果右半部分元素已经全部处理完毕
                arr[k] = aux[i - l]; i++;
            }
            else if (aux[i - l].compareTo(aux[j - l]) < 0) {  // 左半部分所指元素 < 右半部分所指元素
                arr[k] = aux[i - l]; i++;
            }
            else {                         // 左半部分所指元素 >= 右半部分所指元素
                arr[k] = aux[j - l]; j++;
            }
        }
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        log(arr);
        sort(arr);
        log(arr);

        // 性能测试
        Integer[] arr1 = generateRandomIntArr(50000);
        Integer[] arr2 = arr1.clone();
        timeIt(arr1, InsertionSort::sort2);
        timeIt(arr2, MergeSort::sort);  // 对5w个随机数，归并排序比插入排序快100多倍
    }
}
