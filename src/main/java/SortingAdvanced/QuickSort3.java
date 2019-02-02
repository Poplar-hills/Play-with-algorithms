package SortingAdvanced;

import SortingBasic.InsertionSort;
import com.sun.scenario.effect.Merge;

import static Utils.Helpers.*;

public class QuickSort3 {
    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(Comparable[] arr, int l, int r) {

    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(10);
        log(arr);
        sort(arr);
        log(arr);

        Integer[] arr1 = generateRandomArrayFromRange(100000, 0, 10);  // 数据集的所有元素都在0-10之间
        Integer[] arr2 = arr1.clone();
        timeIt(arr1, MergeSort::sort);
        timeIt(arr2, QuickSort2::sort);  // 对于包含大量重复的数据集，QuickSort2 比 MergeSort 慢了十几倍
//        timeIt(arr2, QuickSort3::sort);
    }
}
