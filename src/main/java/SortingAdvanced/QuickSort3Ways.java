package SortingAdvanced;

/*
* 三路快速排序（Three pivot quicksort / 3-way quick sort）：
*
* - 对于包含大量重复元素的数据集，还有一种更经典的优化方式，就是三路快速排序。
* - 三路快排的思路是将 == v 的元素单独放在中间，不再与 < v 和 > v 的元素混在一起。因此在 partition 结束之后，
*   数组会被分成 < v, == v, > v 的三段，并且所有 == v 的元素都已经被放在了正确的位置上（< v 和 > v 中的元素分
*   别还存在乱序）。因此之后就只需分别对 < v 和 > v 的元素进行递归即可。
* - 动画演示 SEE: https://coding.imooc.com/lesson/71.html#mid=1461（0'0''）
*
* - 在性能上，三路快排在处理包含大量重复元素的数据集时要比前几种快排效率高出很多，而对于完全随机和近乎有序的数组
*   的排序性能上稍稍慢于前几种，但完全可以接受。这也是为什么一般通用型的快速排序都会选择三路排序的方案。
*
* - 如果面试中碰到"谈谈快速排序"这种题目就可以针对 1.完全随机 2.近乎有序 3.包含大量重复元素 这三种情况的数据集
*   分别讨论每种实现的优劣。
*
* - 算法背后的思想：
*   - Merge sort 和 Quick sort 都使用了分治的思想 —— 分而治之，将原问题分割成同等结构的子问题，当子问题被逐一解
*     决后，原问题就已经被解决了。
*   - Merge sort 和 Quick sort 都是将原问题分割成2个子问题，但是他们分别代表了分治思想的两种实现方法：
*     1. Merge sort 在"分"的问题上没有过多考虑，直接一刀切分成两部分然后递归排序。它的重点在于分完之后如何"治"，
*        即如何将两部分 merge 起来完成排序任务。
*     2. Quick sort 则是重在如何"分" —— 整个 partition 就是在寻找标定点 pivot 的过程，找到之后才开始"分"。这样
*        分完之后在"治"（也就是合）的过程就不必做过多考虑了。
 * */

import java.util.Random;

import static Utils.Helpers.*;

public class QuickSort3Ways {
    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(Comparable[] arr, int l, int r) {
        if (l >= r) return;
        int[] pivots = partition(arr, l, r);
        sort(arr, l, pivots[0]);  // 继续对 arr[l...lt]（即 < v 的所有元素）排序
        sort(arr, pivots[1], r);  // 继续对 arr[gt...r]（即 > v 的所有元素）排序
    }

    private static int[] partition(Comparable[] arr, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;
        swap(arr, l, vIndex);
        Comparable v = arr[l];

        int i = l + 1;
        int lt = l;  // 指向 < v 的最后一个元素；因为要使得 arr[l+1...lt] < v，因此初始取值 l 才可以使得 arr[l+1...lt] 为空
        int gt = r + 1;  // 指向 > v 的第一个元素；因为要使得 arr[gt...r] > v，因此初始取值 r+1 才可以使得 arr[gt...r] 为空

        while (i < gt) {
            if (arr[i].compareTo(v) < 0) {
                swap(arr, lt + 1, i);  // 与 < v 的最后一个元素的后一个元素 swap
                lt++;
                i++;
            }
            else if (arr[i].compareTo(v) > 0) {
                swap(arr, gt - 1, i);  // 与 > v 的第一个元素的前一个元素 swap
                gt--;  // 此时 i 不用自增。因为 swap 之后 i 指向的是之前 gt-1 指向的元素，即一个还未被处理过的元素，因此继续处理即可
            }
            else {  // arr[i] == v 的情况
                i++;
            }
        }
        swap(arr, l, lt);  // 最后让 pivot 元素成为 == v 的第一个元素。此时所有 == v 的元素都已放到了正确的位置上
        lt--;  // swap 之后 lt 指向 == v 的第一个元素，因此自减使其指向 < v 的最后一个元素
        return new int[]{lt, gt};
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
        timeIt(arr1, QuickSort::sort);
        timeIt(arr2, QuickSort2::sort);
        timeIt(arr3, QuickSort2Ways::sort);
        timeIt(arr4, QuickSort3Ways::sort);  // 三路快排对于包含大量重复的数据集的排序效率是最高的
    }
}
