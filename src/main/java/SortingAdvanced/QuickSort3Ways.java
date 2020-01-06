package SortingAdvanced;

import java.util.Random;

import static Utils.Helpers.*;

/*
* 三路快速排序（3-way Quick Sort）：
*
* - 对于包含大量重复元素的数据集，还有一种更经典的优化方式 —— 三路快速排序。
*
* - 三路快排的 partition 过程：
*   - 思路是将 == v 的元素单独放在中间区间，不与 < v 和 > v 的元素混在一起：
*     [ v|--- <v ---|--- ==v ---|......|--- >v ---| ]
*       l          lt            i      gt       r
*   - 这里除了 l 和 r，还需要3个中间索引：
*     1. lt 指向 < v 最后一个的元素；
*     2. gt 指向 > v 的第一个元素；
*     3. i 指向正在访问的元素。
*   - 这样在 i 访问下一个元素的时候：
*     1. 若 arr[i] < v，则 swap(i, lt+1)；lt++；
*     2. 若 arr[i] == v，则放着不动；
*     3. 若 arr[i] > v，则 swap(i, gt-1)；gt--；
*   - 在 partition 结束之后，数组会被分成 < v, == v, > v 的三段，且所有 == v 的元素都已经被放在了正确的位置上，
*     只有 < v 和 > v 区间中的元素还存在乱序，因此只需分别对这两个区间进行递归即可。
*   - 动画演示 SEE: https://coding.imooc.com/lesson/71.html#mid=1461（0'17''）
*
* - 三路快排性能：
*   - 在处理包含大量重复元素的数据集时要比前几种快排效率高出很多；
*   - 对于完全随机和近乎有序的数组的排序性能上稍稍慢于前几种，但完全可以接受；
*   - 因此一般通用型的快速排序都会选择三路排序的方案。
*
* - 如果面试中碰到"谈谈快速排序"这种题目就可以针对 1.完全随机 2.近乎有序 3.包含大量重复元素 这三种情况的数据集
*   分别讨论每种实现的优劣。
* */

public class QuickSort3Ways {
    public static <T extends Comparable<T>> void sort(T[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static <T extends Comparable<T>> void sort(T[] arr, int l, int r) {
        if (l >= r) return;
        int[] ps = partition(arr, l, r);  // 与两路快排不同，三路快排中的 partition 返回两个索引（lt 和 gt）
        sort(arr, l, ps[0]);              // 对 arr[l...lt]（即 < v 的所有元素）进行递归排序
        sort(arr, ps[1], r);              // 对 arr[gt...r]（即 > v 的所有元素）进行递归排序
    }

    private static <T extends Comparable<T>> int[] partition(T[] arr, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;
        swap(arr, l, vIndex);
        T v = arr[l];

        int lt = l;      // 指向 < v 的最后一个元素；∵ 初始没有元素 < v ∴ 初值为 l（l 是标定元素 v 的位置）
        int gt = r + 1;  // 指向 > v 的第一个元素；∵ 初始没有元素 > v ∴ 初值为 r+1
        int i = l + 1;   // 指向当前被处理中的元素

        while (i < gt) {
            if (arr[i].compareTo(v) < 0)
                swap(arr, i++, ++lt);  // 与 < v 的最后一个元素的后一个元素 swap
            else if (arr[i].compareTo(v) > 0)
                swap(arr, i, --gt);    // 与 > v 的第一个元素的前一个元素 swap。此时 i 不用自增，因为 swap 之后 i 指向的还是一个还未被处理过的元素，因此继续处理即可
            else                       // arr[i] == v 的情况
                i++;
        }
        swap(arr, l, lt);  // 再将 pivot 放到正确的位置上（即所有 < v 的元素之后，在所有 == v 的元素之前）
        lt--;              // ∵ 把 pivot 放到了 lt 上 ∴ lt 需要 -1 才能继续指向 < v 的最后一个元素
        return new int[] {lt, gt};
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
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
