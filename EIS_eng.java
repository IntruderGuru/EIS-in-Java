//Bartlomiej Chmiel
// Implementation of insertion sort with a threshold and comparison with traditional insertion sort

import java.util.Arrays;
import java.util.Random;

public class EIS_eng {
    // Configuration constants
    // NUM_SIZES: Number of array sizes used for performance tests
    // SIZES: Array with the sizes of arrays for tests
    // NUM_TRIALS: Number of trials for each array size
    // THRESHOLD: Threshold value used in insertion sort with threshold
    private static final int NUM_SIZES = 10;
    private static final int[] SIZES = { 10000, 20000, 30000, 40000, 50000, 75000, 100000, 150000, 200000, 250000 };
    private static final int NUM_TRIALS = 10;
    private static final int THRESHOLD = 3;

    // Traditional insertion sort implementation
    // For each element of the array (starting from the second one), it compares it
    // with
    // the previous elements.
    // If the element is smaller than the previous one, it swaps them.
    // It repeats these steps until the element is larger than its predecessor.
    public static void insertionSort(int[] arr) {
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }

    // Implementation of insertion sort with a threshold
    // Works similarly to the traditional insertion sort,
    // but uses a threshold value to speed up the sorting process.
    // Checks if the element is smaller than the element at position i / threshold.
    // If so, it moves all elements between these positions one to the right,
    // and then inserts the element at the appropriate position.
    // We iterate through each element of the array starting from the second
    public static void thresholdSort(int[] arr, int threshold) {
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];
            int j;

            // We check if the current element is smaller than the element at position i /
            // threshold
            if (arr[i] < arr[i / threshold]) {
                // We search for the position to insert the element
                for (j = i / threshold; j > 0; j--) {
                    if (arr[i] >= arr[j - 1]) {
                        break;
                    }
                }
                // We move all elements between the found position and the current position one
                // to the right
                System.arraycopy(arr, j, arr, j + 1, i - j);
                // We insert the element at the found position
                arr[j] = temp;
            } else {
                // If the element is not smaller than the element at position i / threshold,
                // we use the standard approach of insertion sort
                j = i - 1;

                while (j >= 0 && arr[j] > temp) {
                    arr[j + 1] = arr[j];
                    j--;
                }
                // We insert the element at the appropriate position
                arr[j + 1] = temp;
                // Note: Why not swap?
                // When it comes to comparing the speed of these two operations, it depends on
                // many factors, such as the number of elements to copy or swap
                // and the specifics of a given implementation. In general, however, the swap
                // operation is usually faster because it only requires three operations
                // (assignments), while System.arraycopy() has to copy each element
                // individually. But when it comes to shifting larger chunks of data,
                // System.arraycopy() may be faster.
            }
        }
    }

    // Generating a random array of the given size
    // It creates a new array of the specified size and fills it with random
    // numbers.
    public static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size);
        }

        return arr;
    }

    // Calculating the average execution time for threshold sort and traditional
    // sort
    // For each trial, it copies the array, sorts it using the selected algorithm,
    // and then calculates the sorting time.
    // Returns the average execution time based on all trials.
    private static long averageExecutionTime(int[] arr, int numTrials, boolean useThresholdSort) {
        long totalTime = 0;

        for (int i = 0; i < numTrials; i++) {
            int[] arrCopy = Arrays.copyOf(arr, arr.length);

            if (useThresholdSort) {
                long startTime = System.nanoTime();
                thresholdSort(arrCopy, THRESHOLD);
                long endTime = System.nanoTime();
                totalTime += endTime - startTime;
            } else {
                long startTime = System.nanoTime();
                insertionSort(arrCopy);
                long endTime = System.nanoTime();
                totalTime += endTime - startTime;
            }

        }

        return totalTime / numTrials;
    }

    // Checking if the array is sorted
    // Compares each element of the array with the next one.
    // If all elements are sorted in ascending order, it returns true, otherwise, it
    // returns false.

    public static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }

        return true;
    }

    // Main function of the program
    // Tests both sorting algorithms for different array sizes.
    // Calculates the ratio of the threshold sort time to the traditional sort time.
    // Prints the results for each case and calculates the average improvement for
    // all sizes.
    public static void main(String[] args) {
        double[] ratios = new double[NUM_SIZES];

        for (int k = 0; k < NUM_SIZES; k++) {
            int size = SIZES[k];
            int[] arr = generateRandomArray(size);
            long insertionSortTime = averageExecutionTime(arr, NUM_TRIALS, false);
            long thresholdSortTime = averageExecutionTime(arr, NUM_TRIALS, true);
            double ratio = (double) thresholdSortTime / insertionSortTime;
            ratios[k] = ratio;
            System.out.printf(
                    "Size: %d, ratio of threshold sort time to insertion sort time: %.2f\n",
                    size, ratio);
            int[] arrCopy1 = Arrays.copyOf(arr, arr.length);
            int[] arrCopy2 = Arrays.copyOf(arr, arr.length);
            insertionSort(arrCopy1);
            thresholdSort(arrCopy2, THRESHOLD);

            if (isSorted(arrCopy1) && isSorted(arrCopy2)) {
                System.out.println("Both arrays are sorted.");
            } else {
                System.out.println("Error: At least one of the arrays is not sorted.");
            }
        }

        double avgRatio = Arrays.stream(ratios).average().orElse(0);
        int percent = (int) Math.round((1 - avgRatio) * 100);
        System.out.println("Average improvement for all sizes: " + percent + "%");
    }
}
