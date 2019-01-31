package SortingAdvanced;

import static Utils.Helpers.*;

/*
* - 快速排序性能分析：
*   - 虽然归并排序和快速排序都是 O(nlogn) 级别的，但是快速排序之所以更快是因为快速排序的那个常数更小。具体表现在：
*     1. 快速排序每一层的比较次数比归并排序少。
*     2. 归并排序的 merge 过程，还需要一个辅助空间，将所有的元素都挪一边，而快速排序则完全是原地的。
*   - 但是当面对近乎有序的数组时，未经优化的快速排序的性能比归并排序低很多，并且对于十万百万级别的数据量会出现栈溢
*     出的问题，这是因为这两种算法都是将数组进行二分后再分别处理，但在分法不同：
*     1. 归并排序是标准的二分，因此最后得到的递归树的平衡性是很好的；
*     2. 而快速排序是根据一个标定元素进行 partition，分出的两个区域很可能大小不一样，因此最后得到的递归树的平衡
*        性较差。同时，树的高度也不一定是 log(n)，因为在最差情况下（数组完全有序），partition 选取的的标定元素就
*        是数组中的最小值，因此二分结果是左边区域完全没有元素，右边区域包含所有元素的情况：
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
*   1. 对于问题2，改进方案是不再指定第一个元素作为标定元素，而采用随机选取，这样每一层选到最小元素的概率是 1 / n，
*     因此算法复杂度退化成 O(n^2) 的概率就是 (1 / n)^n（即所有层都选到最小元素），这种概率已经接近于零。
*   2. 快速排序同样可以采用在数据量较少时采用插入排序的方式进行优化。
* */

public class QuickSortOptimised {
    public static void sort(Comparable[] arr) {

    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        log(arr);
        sort(arr);
        log(arr);

        Integer[] arr1 = generateNearlyOrderedArr(10000, 0);
        Integer[] arr2 = arr1.clone();
        Integer[] arr3 = arr1.clone();
        timeIt(arr1, QuickSort::sort);  // 比 MergeSortOptimised 慢几十倍
        timeIt(arr2, MergeSortOptimised::sort);
        timeIt(arr3, QuickSortOptimised::sort);
    }

}
