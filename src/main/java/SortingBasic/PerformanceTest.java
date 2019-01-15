package SortingBasic;

import java.lang.reflect.Method;
import java.util.Random;

public class PerformanceTest {
    public static double test(Comparable[] arr, String sortClassName) {
        double secondsConsumed = 0.0;
        try {
            Class sortClass = Class.forName(sortClassName);
            Method sortMethod = sortClass.getMethod("sort", Comparable[].class);

            double startTime = System.nanoTime();
            sortMethod.invoke(null, arr);
            double endTime = System.nanoTime();

            secondsConsumed = (endTime - startTime) / 1000000000.0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secondsConsumed;
    }

    public static void main(String[] args) {
        int size = 10000;
        Integer[] arr1 = new Integer[size];
        Integer[] arr2 = new Integer[size];
        Random r = new Random();

        for (int i = 0; i < size; i++) {
            arr1[i] = r.nextInt(size);
            arr2[i] = r.nextInt(size);
        }

        System.out.println("Selection sort: " + test(arr1, "SelectionSort"));
//        System.out.println("Insertion sort: " + test(arr2, "InsertionSort"));
    }
}
