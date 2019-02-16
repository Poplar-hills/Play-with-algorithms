package BST;

import static Utils.Helpers.log;

/*
* 二分查找（Binary Search）：
*
* - 历史：二分查找法早在1946年就被提出，但直到1962年才出现第一个没有 bug 的实现。
* - 限制：二分查找要求数据集必须是顺序的（排序算法是很多其他算法中的一个子步骤）。
* - 复杂度：最简单粗暴的查找就是遍历，复杂度为 O(n)；而二分查找的复杂度为 O(logn)。
* */

public class BinarySearch {
    public static int sort(Comparable[] arr, Comparable e) {
        return binarySort(arr, e, 0, arr.length - 1);
    }

    private static int binarySort(Comparable[] arr, Comparable e, int l, int r) {
        if (l > r) return -1;
        int mid = (r - l) / 2 + l;  // 防止极端情况下的整形溢出
        if (e.compareTo(arr[mid]) < 0)
            return binarySort(arr, e, l, mid - 1);
        else if (e.compareTo(arr[mid]) > 0)
            return binarySort(arr, e, mid + 1, r);
        else
            return mid;
    }

    public static void main(String[] args) {
        Integer[] arr = {1, 3, 4, 7, 9, 10};
        log(sort(arr, 0));
        log(sort(arr, 3));
        log(sort(arr, 7));
        log(sort(arr, 18));
    }
}
