package Utils;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.function.Function;

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

    // TODO: this is not working
    // TODO: is it possible to pass in a lambda that take an arr as the arguement and call it afterwards?
    public static double timeIt(Comparable[] arr, String sortClassName) {
        double secondsConsumed = 0.0;
        try {
            Class sortClass = Class.forName(sortClassName);
            Method sortMethod = sortClass.getMethod("sort1", Comparable[].class);

            double startTime = System.nanoTime();
            sortMethod.invoke(null, arr);
            double endTime = System.nanoTime();

            secondsConsumed = (endTime - startTime) / 1000000000.0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secondsConsumed;
    }

    public static Integer[] generateNearlyOrderedArr(int size, int numOfSwap) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++)
            arr[i] = i;

        Random r = new Random();
        for (int i = 0; i < numOfSwap; i++) {
            int index1 = r.nextInt(size);
            int index2 = index1;
            while (index1 == index2)
                index2 = r.nextInt(size);
            swap(arr, index1, index2);
        }

        return arr;
    }
}
