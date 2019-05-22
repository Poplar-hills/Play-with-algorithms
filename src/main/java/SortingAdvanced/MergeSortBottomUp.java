package SortingAdvanced;

import java.util.Arrays;

import static Utils.Helpers.*;

/*
* 自底向上的归并排序：
*
* - 归并排序的另一种思路：
*   - 自底向上（bottom-up）进行归并，而不是自顶向下（top-down）；
*   - 通过迭代（iterative）实现，而不需要通过递归（recursive）实现。
*
*     1 2 3 4 5 6 7 8
*            ⬆          - 每组4个元素，两组两组归并
*     2 3 6 8|1 4 5 7
*            ⬆          - 每组2个元素，两组两组归并
*     6 8|2 3|1 5|4 7
*            ⬆          - 每组1个元素，两组两组归并
*     8|6|2|3|1|5|7|4
*
*   - 另一种表达：
*
*     1  2  3  4  5  6  7  8
*     +  +  +  +  +  +  +  +    - 第一次遍历，每组1个元素，两组两组归并
*     +--+  +--+  +--+  +--+    - 第二次遍历，每组2个元素，两组两组归并
*     +--------+  +--------+    - 第三次遍历，每组4个元素，两组两组归并
*     +--------------------+    - 第四次遍历，每组8个元素，两组两组归并
*
* - 比较：复杂度都是 O(nlogn)，都可以在1秒内轻松处理100万数量级的数据。但是统计性能上，自顶向下的递归的实现稍好一点。
 * */

public class MergeSortBottomUp {
    public static void sort(Comparable[] arr) {
        for (int step = 1; step <= arr.length; step *= 2) {          // 产生二分的 step 序列：1, 2, 4, 8, ... 对应每组元素个数
            for (int i = 0; i + step < arr.length; i += step * 2) {  // 每次对两个 step 内的元素（即 arr[i...i+step-1] 和 arr[i+step...i+2*step-1]）
                int l = i;                                           // 进行归并，而 i + step < arr.length 保证了第二段 step 中至少有元素存在。
                int mid = i + step - 1;
                int r = Math.min(i + 2 * step - 1, arr.length - 1);  // 剩余元素长度可能不够 step，因此取 min
                if (arr[mid].compareTo(arr[mid + 1]) > 0)            // 和 MergeSort 中进行相同的优化
                    merge(arr, l, mid, r);
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