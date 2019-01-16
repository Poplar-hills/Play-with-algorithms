package SortingBasic;

import java.util.Arrays;
import java.util.Random;

import static SortingBasic.Helpers.*;

/*
 * 选择排序
 * - 复杂度：O(n^2)。
 * */

public class SelectionSort {
    public static void sort(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            for (int j = i; j < arr.length; j++)
                if (arr[j].compareTo(arr[minIndex]) < 0)
                    minIndex = j;
            swap(arr, i, minIndex);
        }
    }

    public static void main(String[] args) {
        Integer[] arr1 = generateRandomIntArr(5);
        Character[] arr2 = generateRandomCharArr(5);

        System.out.println(Arrays.toString(arr1));
        sort(arr1);
        System.out.println(Arrays.toString(arr1) + '\n');

        System.out.println(Arrays.toString(arr2));
        sort(arr2);
        System.out.println(Arrays.toString(arr2));
    }
}
