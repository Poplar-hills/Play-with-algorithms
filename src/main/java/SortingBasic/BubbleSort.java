package SortingBasic;

import java.util.Arrays;

import static SortingBasic.Helpers.*;

public class BubbleSort {
    public static void sort(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr.length - i - 1; j++)
                if (arr[j].compareTo(arr[j + 1]) > 0)
                    swap(arr, j, j + 1);
    }

    public static void main(String[] args) {
        Integer[] arr = generateRandomIntArr(5);
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
