package SortingAdvanced;

import SortingBasic.InsertionSort;

import java.util.Random;

import static Utils.Helpers.*;

/*
* 双路快速排序（Dual-pivot quick sort / 2-way quick sort）：
*
* - QuickSort 和 QuickSort2 都是一路快排，也叫做 naive quick sort。
* - 虽然 QuickSort2 对于近似有序的数组进行了优化，但对于包含大量重复元素的数组，naive quick sort 还是会退化成 O(n^2) 的复杂度，
*   并且可能栈溢出。这是因为大量重复的元素会让 partition 过程中出现很多 arr[i] == v 的情况，
*     -> 从而使得过多的 arr[i] 被归入 < v 的区间（∵ arr[i] < v 时会进行 swap）；
*       -> 从而使得 partition 结果极为不平衡（< v 与 > v 区间比例失衡）；
*         -> 从而使递归深度趋近于数组的数据个数 n；
*           -> 从而最终导致复杂度趋近 O(n^2)、栈溢出。
*             -> 这也是大多数语言的标准库中都不会使用 naive quick sort 的原因。
*
* - 双路快排和三路快排要解决的就是这个重复元素的问题
*   - 他们的性能不如 naive quick sort，因为代码中有更多的判断，但是并不会慢很多。
*   - 双路快排过程 SEE：https://coding.imooc.com/lesson/71.html#mid=1460（3'33''）
*
* - 实际上双路快排是快速排序的标准实现，因为它对于任意情况（完全随机、近乎有序、大量重复）的数据，都不会退化成 O(n^2)。
*   因此平时我们一说起快速排序算法（Quick Sort），指的就是双路快排。
*
* - 注意：
*   partition 内部的两个嵌套 while 语句的条件只能是 arr[i] < v 和 arr[j] > v，而不能是 arr[i] <= v 和 arr[j] >= v。
*   这是因为对于 [0, 1, 0, 0, 0, 0, 0, 0] 这样的数组，如果 pivot 选择的是0，则 arr[i] <= 0 和 arr[j] >= 0 会让
*   partition 最后得到的分点靠近数组的最左端，产生极度不平衡的递归子树（类似 QuickSort2 里的图示），而 arr[i] < v 和
*   arr[j] > v 则不会，因为得到的分点在数组中间。
* */

public class QuickSort2Ways {
    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(Comparable[] arr, int l, int r) {
        if (r - l <= 15) {
            InsertionSort.sortRange(arr, l, r);
            return;
        }
        int p = partition(arr, l, r);
        sort(arr, l, p - 1);
        sort(arr, p + 1, r);
    }

    private static int partition(Comparable[] arr, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;
        swap(arr, l, vIndex);
        Comparable v = arr[l];

        // 让 arr[l+1, i) 中的元素都 <= v；让 arr(j, r] 中的元素都 >= v
        int i = l + 1, j = r;
        while (true) {
            while (i <= r && arr[i].compareTo(v) < 0) i++;  // 当循环结束时，i 指向从左边起第一个 arr[i] > v 的元素
            while (j >= l + 1 && arr[j].compareTo(v) > 0) j--;  // 当循环结束时，j 指向从左右边起第一个 arr[j] < v 的元素
            if (i > j) break;  // 此时本次 partition 完成，即 arr[l+1, i) 中的元素都 <= v；arr(j, r] 中的元素都 >= v
            swap(arr, i, j);
            i++;
            j--;
        }
        swap(arr, l, j);  // 上面循环结束后 j 停在从左往右最后一个 <= v 的元素索引上（因为 i > j 时 break 循环），因此要交换 l 和 j 来把 v 放到正确的位置上。
        return j;
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(20);
        log(arr);
        sort(arr);
        log(arr);

        Integer[] arr1 = generateRandomArrayFromRange(100000, 0, 10);  // 包含大量重复的数据集
        Integer[] arr2 = arr1.clone();
        Integer[] arr3 = arr1.clone();
        Integer[] arr4 = arr1.clone();
        timeIt(arr1, MergeSort::sort);
        timeIt(arr2, QuickSort::sort);   // QuickSort 比 MergeSort 慢了十几倍
        timeIt(arr3, QuickSort2::sort);  // QuickSort2 也是一样慢
        timeIt(arr4, QuickSort2Ways::sort);  // QuickSort2Ways 的性能就非常好了
    }
}
