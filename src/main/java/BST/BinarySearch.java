package BST;

import static Utils.Helpers.log;

/*
* 二分查找（Binary Search）：
*
* - 历史：二分查找法早在1946年就被提出，但直到1962年才出现第一个没有 bug 的实现。
* - 限制：二分查找要求数据集必须是顺序的（排序算法是很多其他算法中的一个子步骤）。
* - 复杂度：
*     - 暴力查找就是遍历，复杂度为 O(n)；
*     - 而二分查找因为每次迭代都会抛弃一半的元素，在剩下元素中查找：
*       - 第1次 n 个元素，第2次 n/2 个元素，第3次 n/4 个元素，...， 第?次1个元素；
*       - 要想知道第?次后只剩下1个元素，相当于求 n 除以几次2之后等于1，即求以2为底的对数；
*       - 因此的复杂度为 O(logn)。
* */

public class BinarySearch {
    // 二分查找法
    public static int sort(Comparable[] arr, Comparable e) {
        return binarySort(arr, e, 0, arr.length - 1);
    }

    private static int binarySort(Comparable[] arr, Comparable e, int l, int r) {
        if (l > r) return -1;
        int mid = (r - l) / 2 + l;
        if (e.compareTo(arr[mid]) < 0)
            return binarySort(arr, e, l, mid - 1);
        if (e.compareTo(arr[mid]) > 0)
            return binarySort(arr, e, mid + 1, r);
        return mid;
    }

    // 二分查找法（非递归）
    public static int sortNR(Comparable[] arr, Comparable e) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int mid = (r - l) / 2 + l;
            if (e.compareTo(arr[mid]) < 0) r = mid - 1;
            else if (e.compareTo(arr[mid]) > 0) l = mid + 1;
            else return mid;
        }
        return -1;
    }

    /*
     * 基于二分查找法，在有序数组 arr 中查找 e：
     *  - 如果找到 e，返回最后一个 e 相应的索引；
     *  - 如果没有找到 e, 返回比 e 大的最小值的索引，如果这个最小值有多个，返回最小的索引；
     *  - 如果这个 e 比整个数组的最大值还大，则不存在这个 e 的 ceiling 值，返回整个数组的元素个数 n。
     */
    public static int ceiling(Comparable[] arr, Comparable e) {
        return ceiling(arr, e, 0, arr.length - 1);
    }

    private static int ceiling(Comparable[] arr, Comparable e, int l, int r) {
        if (l > r) return l;
        int mid = (r - l) / 2 + l;
        if (e.compareTo(arr[mid]) < 0)
            return ceiling(arr, e, l, mid - 1);
        else if (e.compareTo(arr[mid]) > 0)
            return ceiling(arr, e, mid + 1, r);
        else {
            int t = mid;
            while (arr[t] == arr[mid])
                t += 1;
            return t - 1;
        }
    }

    /*
     * 基于二分查找法，在有序数组 arr 中查找 e：
     *  - 如果找到 e，返回最后一个 e 相应的索引；
     *  - 如果没有找到 e, 返回比 e 小的最大值的索引，如果这个最大值有多个，返回最大的索引；
     *  - 如果这个 e 比整个数组的最小值还小，则不存在这个 e 的 floor 值，返回 -1。
     */
    public static int floor(Comparable[] arr, Comparable e) {
        return floor(arr, e, 0, arr.length - 1);
    }

    private static int floor(Comparable[] arr, Comparable e, int l, int r) {
        if (l > r) return r;
        int mid = (r - l) / 2 + l;
        if (e.compareTo(arr[mid]) < 0)
            return floor(arr, e, l, mid - 1);
        else if (e.compareTo(arr[mid]) > 0)
            return floor(arr, e, mid + 1, r);
        else {
            int t = mid;
            while (arr[t] == arr[mid])
                t -= 1;
            return t + 1;
        }
    }

    public static void main(String[] args) {
        Integer[] arr1 = {1, 3, 4, 7, 9, 10};
        log(String.format("%d, %d, %d", sort(arr1, 3), sort(arr1, 7), sort(arr1, 18)));
        log(String.format("%d, %d, %d", sortNR(arr1, 3), sortNR(arr1, 7), sortNR(arr1, 18)));

        Integer[] arr2 = {1, 2, 2, 2, 4, 5, 7, 9, 10};  // 数据集中有多个2，此时 ceiling 应返回最后一个2的索引3，floor 应返回第一个2的索引1
        log(String.format("ceiling: %d; floor: %d", ceiling(arr2, 2), floor(arr2, 2)));

        Integer[] arr3 = {1, 3, 4, 7, 9, 10};  // 数据集中没有5，此时 ceiling 应返回7的索引3，floor 应返回4的索引2
        log(String.format("ceiling: %d floor: %s", ceiling(arr3, 5), floor(arr3, 5)));

        Integer[] arr4 = {1, 3, 4, 7, 9, 10};  // 数据集中没有12，且12 > 数组最大值，此时 ceiling 应返回数组元素个数6，floor 应返回最大值的索引5
        log(String.format("ceiling: %d floor: %s", ceiling(arr4, 12), floor(arr4, 12)));

        Integer[] arr5 = {1, 3, 4, 7, 9, 10};  // 数据集中没有0，且0 < 数组最小值，此时 ceiling 应返回最小值的索引0，floor 应返回-1
        log(String.format("ceiling: %d floor: %s", ceiling(arr5, 0), floor(arr5, 0)));
    }
}
