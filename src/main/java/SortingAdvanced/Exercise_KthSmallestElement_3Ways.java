package SortingAdvanced;

import java.util.Random;

import static Utils.Helpers.*;

/*
 * 练习：从有 n 个元素的数组中找到第 k 小的元素 - 采用三路快排求解
 *
 * - 下面代码中的泛型语法 SEE: https://stackoverflow.com/questions/20938318/sorting-an-array-of-comparable-interface
 * */

public class Exercise_KthSmallestElement_3Ways {
    public static <T extends Comparable<T>> T quickSelect(T[] arr, int k) {
        return select(arr, 0, arr.length - 1, k - 1);  // k-1 是为了让语义更自然（"第1小"就是最小，"第2小"就是第2小，没有"第0小"）
    }

    private static <T extends Comparable<T>> T select(T[] arr, int l, int r, int k) {
        if (l == r) return arr[l];
        int[] ps = partition(arr, l, r);
        if (k <= ps[0])
            return select(arr, l, ps[0], k);
        else if (k >= ps[1])
            return select(arr, ps[1], r, k);
        else
            return arr[ps[0] + 1];  // 即 arr[lt + 1]，lt + 1 即 == v 的元素
    }

    private static <T extends Comparable<T>> int[] partition(T[] arr, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;
        swap(arr, l, vIndex);
        T v = arr[l];
        int lt = l, gt = r + 1;

        for (int i = l + 1; i < gt; ) {
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
