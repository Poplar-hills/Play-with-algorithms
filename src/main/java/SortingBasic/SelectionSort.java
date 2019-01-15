package SortingBasic;

import java.util.Arrays;
import java.util.Random;

/*
 * 选择排序
 * - 复杂度：O(n^2)
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

    private static void swap(Object[] arr, int i, int j) {
        Object temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int size = 10;
        Random r = new Random();
        Integer[] arr1 = new Integer[size];
        Character[] arr2 = new Character[size];

        for (int i = 0; i < size; i++) {
            arr1[i] = r.nextInt(size);
            arr2[i] = (char) (r.nextInt(26) + 'a');
        }

        System.out.println(Arrays.toString(arr1));
        sort(arr1);
        System.out.println(Arrays.toString(arr1) + '\n');

        System.out.println(Arrays.toString(arr2));
        sort(arr2);
        System.out.println(Arrays.toString(arr2));
    }
}
