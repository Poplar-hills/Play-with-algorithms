package SortingAdvanced;

import java.util.Arrays;

import static Utils.Helpers.*;

/*
* 练习：寻找数组中的逆序对
*
* - 逆序对是衡量一个数据集有序程度的指标。
* - 使用归并排序（及其中的分治思想）解决该问题，将算法复杂度控制在 O(nlogn) 层级上。
* - 归并排序的分治思路是：在"分"的问题上不做过多考虑，直接简单快速的进行二分。它的重点在于"治"，以及何将"治"完之后的子问题
*   的解合并起来形成原问题的解。
* */

public class Exercise_InversionCount {
    public static int count1(Comparable[] arr) {  // 解法一：brute force，复杂度为 O(n^2)
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++)
                if (arr[i].compareTo(arr[j]) > 0)
                    count++;
        }
        return count;
    }

    public static int count2(Comparable[] arr) {  // 解法二：分治，复杂度为 O(nlogn)
        return count2(arr, 0, arr.length - 1);
    }

    private static int count2(Comparable[] arr, int l, int r) {
        if (l >= r) return 0;
        int mid = (r - l) / 2 + l;
        int c1 = count2(arr, l, mid);  // 递归计算每个子问题的答案
        int c2 = count2(arr, mid + 1, r);
        return c1 + c2 + merge(arr, l, mid, r);  // 在归并阶段将每个子问题的答案合并（即相加）在一起
    }

    private static int merge(Comparable[] arr, int l, int mid, int r) {
        Comparable[] aux = Arrays.copyOfRange(arr, l, r + 1);
        int i = l, j = mid + 1;
        int count = 0;

        for (int k = l; k <= r; k++) {
            if (i > mid) {
                arr[k] = aux[j - l]; j++;
            }
            else if (j > r) {
                arr[k] = aux[i - l]; i++;
            }
            else if (aux[i - l].compareTo(aux[j - l]) <= 0) {  // 注意此处条件包含 =0 的情况（即相等的两个元素不构成逆序对）
                arr[k] = aux[i - l]; i++;
            }
            else {  // 右半部分所指元素 < 左半部分所指元素
                arr[k] = aux[j - l]; j++;
                count += mid - i + 1;  // 因为此时右半部分所指元素较小，因此该元素和左半部分的所有未处理的元素都构成了逆序对，而此时左半部分未处理的元素个数为 mid-j+1
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Integer[] arr1 = {4, 2, 5, 1, 3, 3};
        Integer[] arr2 = arr1.clone();
        log(count1(arr1));
        log(count2(arr2));

        Integer[] arr3 = generateRandomIntArr(10000);
        Integer[] arr4 = arr3.clone();
        timeIt(arr3, Exercise_InversionCount::count1);
        timeIt(arr4, Exercise_InversionCount::count2);  // 性能远高于 count1
    }
}
