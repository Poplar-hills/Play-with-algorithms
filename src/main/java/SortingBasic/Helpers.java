package SortingBasic;

import java.util.Random;

public class Helpers {
    public static void swap(Object[] arr, int i, int j) {
        Object temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static Integer[] generateRandomIntArr(int size) {
        Random r = new Random();
        Integer[] arr = new Integer[size];

        for (int i = 0; i < size; i++)
            arr[i] = r.nextInt(size);

        return arr;
    }

    public static Character[] generateRandomCharArr(int size) {
        Random r = new Random();
        Character[] arr = new Character[size];

        for (int i = 0; i < size; i++)
            arr[i] = (char) (r.nextInt(26) + 'a');

        return arr;
    }
}
