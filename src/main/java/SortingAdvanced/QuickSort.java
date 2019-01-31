package SortingAdvanced;

/*
* - 快速排序被认为是20世纪最伟大的算法之一。
*
* - 快速排序过程：
*   1. 将第一个元素移动到正确的位置上；
*   2. 并使其前面的元素都小于该元素，其后面的元素都大于该元素；
*   3. 再分别对其前面的、后面的元素进行快速排序（递归过程）
*
* - partition 过程：
*      4  6  2  3  5  7  1  8   原始数组，轩第一个元素作为比较元素
*      4  6  2  3  5  7  1  8   因为 6 > 4，所以放着不动
*      4  2  6  3  5  7  1  8   因为 2 < 4，所以对2和6进行 swap（因为6是大于4的第一个元素）
*      4  2  3  6  5  7  1  8   因为 3 < 4，所以对3和6进行 swap
*      4  2  3  6  5  7  1  8   因为 5 < 4，所以放着不动
*      4  2  3  6  5  7  1  8   因为 7 < 4，所以放着不动
*      4  2  3  1  5  7  6  8   因为 1 < 4，所以对1和6进行 swap
*      4  2  3  1  5  7  6  8   因为 8 > 4，所以放着不动
*      1  2  3  4  6  5  7  8   最后对4和1进行 swap（因为1是小于4的最后一个元素）
*      |_____|  |  |________|
*         |     |      |
*        < v    v     > v
*
* - 性能分析：
*   - 虽然归并排序和快速排序都是 O(nlogn) 级别的，但是快速排序之所以更快是因为快速排序的那个常数更小。具体表现在：
*     1. 快速排序每一层的比较次数比归并排序少。
*     2. 更不用提归并排序的 merge 过程，还需要一个辅助空间，将所有的元素都挪一边，而快速排序则完全是原地的。
*   - 但是当面对近乎有序的数组时，未经优化的快速排序的性能比归并排序低很多，并且对于十万百万级别的数据量会出现栈溢
*     出的问题，这是因为这两种算法都是将数组进行二分后再分别处理，但在分法不同：
*     1. 归并排序是标准的二分，因此最后得到的递归树的平衡性是很好的；
*     2. 而快速排序是根据一个标定元素进行 partition，分出的两个区域很可能大小不一样，因此最后得到的递归树的平衡
*        性较差。同时，树的高度也不一定是 log(n)，因为在最差情况下（数组完全有序），partition 选取的的标定元素就
*        是数组中的最小值，因此二分结果是左边区域完全没有元素，右边区域包含所有元素的情况：
*                  [1  2  3  4]
*                      /    \
*                    []    [2  3  4]
*                            /   \
*                          []   [3  4]
*                                /  \
*                              []   [4]
*        这时，快速排序会退化成 O(n^2) 的复杂度（整棵树的高度是 n，对每层处理的复杂度是 O(n)），因此性能低下。
*
* */

import static Utils.Helpers.*;

public class QuickSort {
    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(Comparable[] arr, int l, int r) {
        if (l >= r) return;
        int p = partition(arr, l, r);
        sort(arr, l, p - 1);
        sort(arr, p + 1, r);
    }

    // 返回 p, 使得 arr[l...p-1] 中的元素都 < arr[p]，arr[p, r] 中的元素都 > arr[p]
    private static int partition(Comparable[] arr, int l, int r) {
        Comparable v = arr[l];
        int j = l;  // j 指向小于 v 的最后一个元素的位置

        for (int i = l + 1; i <= r; i++) {
            if (arr[i].compareTo(v) < 0)
                swap(arr, i, ++j);  // 与大于 v 的第一个元素进行 swap
        }

        swap(arr, l, j);  // 将 v 放到正确的位置（j）上
        return j;
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        log(arr);
        sort(arr);
        log(arr);

        Integer[] arr1 = generateRandomIntArr(1000000);
        Integer[] arr2 = arr1.clone();
        timeIt(arr1, QuickSort::sort);  // 这一版的 quick sort 已经比 merge sort 快将近三分之一了
        timeIt(arr2, MergeSort::sort);

        Integer[] arr3 = generateNearlyOrderedArr(10000, 0);
        Integer[] arr4 = arr3.clone();
        timeIt(arr3, QuickSort::sort);
        timeIt(arr4, MergeSortForNearlyOrderedArr::sort);  // 比普通的 MergeSort 快一些
    }
}
