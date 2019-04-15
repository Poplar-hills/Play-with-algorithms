package SortingAdvanced;

import java.util.Random;

import static Utils.Helpers.*;

/*
 * 练习：从有 n 个元素的数组中找到第 k 小的元素 - 采用三路快排求解
 * */

public class Exercise_KthSmallestElement_3Ways {
    public static Comparable quickSelect(Comparable[] arr, int k) {
        return select(arr, 0, arr.length - 1, k - 1);  // k 是从0开始索引的, 即最小的元素是第0小元素, 如果希望 k 的语意是从1开始的, 只需要让 k - 1
    }

    private static Comparable select(Comparable[] arr, int l, int r, int k) {
        if (l == r) return arr[l];
        int[] ps = partition(arr, l, r);
        if (k <= ps[0])
            return select(arr, l, ps[0], k);
        else if (k >= ps[1])
            return select(arr, ps[1], r, k);
        else
            return arr[ps[0] + 1];  // 即 arr[lt + 1]，lt + 1 即 == v 的元素
    }

    private static int[] partition(Comparable[] arr, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;
        swap(arr, l, vIndex);
        Comparable v = arr[l];
        int lt = l;
        int gt = r + 1;
        int i = l + 1;

        while (i < gt) {
            if (arr[i].compareTo(v) < 0)
                swap(arr, i++, ++lt);
            else if (arr[i].compareTo(v) > 0)
                swap(arr, i, --gt);
            else
                i++;
        }
        swap(arr, lt, l);
        lt--;
        return new int[] {lt, gt};
    }

    public static void main(String[] args) {
        Integer[] arr1 = {4, 2, 5, 1, 3, 6, 7, 8};
        log(quickSelect(arr1, 2));

        Character[] arr2 = {'b', 'd', 'e', 'c', 'a'};
        log(quickSelect(arr2, 2));
    }
}
