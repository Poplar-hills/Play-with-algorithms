package SortingAdvanced;

import SortingBasic.InsertionSort;

import java.util.Random;

import static Utils.Helpers.*;

/*
* 随机化快速排序（Random Pivot Quick Sort）：
*
* - 快速排序性能分析：
*   - 虽然归并排序和快速排序都是 O(nlogn) 级别的，但是快速排序之所以更快是因为其复杂度中的常数更小。具体表现在：
*     1. 快速排序每一层的比较次数比归并排序少。
*     2. 归并排序的 merge 过程需要创建辅助空间，将所有的元素都拷贝一遍，而快速排序则完全是原地交换的。
*   - 但是当面对近乎有序的数组时，未经优化的快速排序的性能比归并排序低很多，并且对于十万百万级别的数据量会出现栈溢
*     出的问题，这是因为这两种算法虽然都是将数组进行二分后再分别处理，但分法不同：
*     1. 归并排序是标准的二分，因此最后得到的递归树的平衡性是很好的；
*     2. 而快速排序是根据一个标定元素（pivot）进行 partition，分出的两个区域很可能大小不一样，因此最后得到的递归
*        树的平衡性较差。同时，树的高度也不一定是 log(n)，因为在最差情况下（数组完全有序），partition 选取的的标
*        定元素就是数组中的最小值，因此二分结果是左边区域完全没有元素，右边区域包含所有元素的情况：
*                  [0  1  2  3]
*                      /    \
*                    []    [1  2  3]
*                            /   \
*                          []   [2  3]
*                                /  \
*                              []   [3]
*        这时，快速排序会退化成 O(n^2) 的复杂度（整棵树的高度是 n，对每层处理的复杂度是 O(n)），因此性能低下。
*
* - 性能优化：
*   1. 对于问题2，改进方案是不再指定第一个元素作为标定元素，而采用随机选取，这样每一层选到最小元素的概率是 1/n，
*      因此算法复杂度退化成 O(n^2) 的概率就是 (1/n)^n（即所有层都选到最小元素），这种概率已经接近于零。
*   2. 快速排序同样可以采用在数据量较少时采用插入排序的方式进行优化。
* */

public class QuickSort2 {
    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(Comparable[] arr, int l, int r) {  // 与 QuickSort2 中的一致
        if (r - l <= 15) {  // 优化2
            InsertionSort.sortRange(arr, l, r);
            return;
        }
        int p = partition(arr, l, r);
        sort(arr, l, p - 1);
        sort(arr, p + 1, r);
    }

    private static int partition(Comparable[] arr, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;  // 优化1：随机选取标定元素的索引
        swap(arr, l, vIndex);  // 将标定元素换到最前面，这样后面就可以像标准的快排一样进行了
        Comparable v = arr[l];
        int j = l;
        for (int i = l + 1; i <= r; i++) {
            if (arr[i].compareTo(v) <= 0)
                swap(arr, i, ++j);
        }
        swap(arr, l, j);
        return j;
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(20);
        log(arr);
        sort(arr);
        log(arr);

        Integer[] arr1 = generateNearlyOrderedArr(10000, 0);  // 对于十万级近似有序的数据 QuickSort 会栈溢出
        Integer[] arr2 = arr1.clone();
        Integer[] arr3 = arr1.clone();
        Integer[] arr4 = arr1.clone();
        timeIt(arr1, QuickSort::sort);       // 比 MergeSort2 慢几十倍
        timeIt(arr2, QuickSort2::sort);      // 比 QuickSort 快了非常多，但还比 MergeSort2 慢一点
        timeIt(arr3, MergeSort2::sort);      // 第二快的
        timeIt(arr4, InsertionSort::sort2);  // 最快的
    }
}
