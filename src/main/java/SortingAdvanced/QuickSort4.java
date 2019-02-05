package SortingAdvanced;

/*
* 三路快速排序（Three pivot quicksort / 3-way quick sort）：
*
* - 对于包含大量重复元素的数据集，还有一种更经典的优化方式，就是三路快速排序。
* - 三路快排的思路是将 == v 的元素单独放在中间，不再与 < v 和 > v 的元素混在一起。因此在 partition 结束之后，
*   数组会被分成 < v, == v, > v 的三段，并且所有 == v 的元素都已经被放在了正确的位置上（< v 和 > v 中的元素分
*   别还存在乱序）。因此之后就只需分别对 < v 和 > v 的元素进行递归即可。
* - 动画演示 SEE: https://coding.imooc.com/lesson/71.html#mid=1461（0'0''）
* */

import java.util.Random;

import static Utils.Helpers.*;

public class QuickSort4 {
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
        timeIt(arr3, QuickSort3::sort);
        timeIt(arr4, QuickSort4::sort);  // 三路快排对于包含大量重复的数据集的排序效率是最高的
    }
}
