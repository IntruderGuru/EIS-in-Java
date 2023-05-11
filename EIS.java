//Bartlomiej Chmiel
// Implementacja sortowania przez wstawianie z progiem oraz porównanie z tradycyjnym sortowaniem przez wstawianie

import java.util.Arrays;
import java.util.Random;

public class EIS {

    // Stałe konfiguracyjne
    // NUM_SIZES: Liczba rozmiarów tablic użytych do testów wydajności
    // SIZES: Tablica z rozmiarami tablic do testów
    // NUM_TRIALS: Liczba prób dla każdego rozmiaru tablicy
    // THRESHOLD: Wartość progu użyta w sortowaniu przez wstawianie z progiem
    private static final int NUM_SIZES = 10;
    private static final int[] SIZES = { 10000, 20000, 30000, 40000, 50000, 75000, 100000, 150000, 200000, 250000 };
    private static final int NUM_TRIALS = 10;
    private static final int THRESHOLD = 3;

    // Implementacja tradycyjnego sortowania przez wstawianie
    // Dla każdego elementu tablicy (zaczynając od drugiego) porównuje go z
    // poprzednimi elementami.
    // Jeżeli element jest mniejszy od poprzedniego, zamienia je miejscami.
    // Powtarza te kroki, aż do momentu, gdy element będzie większy od poprzednika.
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

    // Implementacja sortowania przez wstawianie z progiem
    // Działa podobnie do tradycyjnego sortowania przez wstawianie,
    // ale używa wartości progu do przyspieszenia procesu sortowania.
    // Sprawdza, czy element jest mniejszy od elementu w pozycji i / threshold.
    // Jeśli tak, przesuwa wszystkie elementy między tymi pozycjami o jeden w prawo,
    // a następnie wstawia element na odpowiedniej pozycji.
    // Iterujemy przez każdy element tablicy zaczynając od drugiego
    public static void thresholdSort(int[] arr, int threshold) {
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];
            int j;

            // Sprawdzamy, czy bieżący element jest mniejszy od elementu w pozycji i /
            // threshold
            if (arr[i] < arr[i / threshold]) {
                // Szukamy pozycji, na którą należy wstawić element
                for (j = i / threshold; j > 0; j--) {
                    if (arr[i] >= arr[j - 1]) {
                        break;
                    }
                }
                // Przesuwamy wszystkie elementy między znalezioną pozycją a bieżącą pozycją o
                // jeden w prawo
                System.arraycopy(arr, j, arr, j + 1, i - j);
                // Wstawiamy element na znalezionej pozycji
                // Uwaga: Dlaczego nie swap?
                // Jeśli chodzi o porównanie prędkości tych dwóch operacji, to zależy to od
                // wielu czynników, takich jak liczba elementów do skopiowania lub zamienienia
                // miejscami oraz specyfika danej implementacji. W ogólności jednak, operacja
                // swap jest zazwyczaj szybsza, ponieważ wymaga tylko trzech operacji
                // (przypisania), podczas gdy System.arraycopy() musi kopiować każdy element
                // indywidualnie. Ale jeżeli chodzi o przesuwanie większych fragmentów danych,
                // System.arraycopy() może okazać się szybsze.
                arr[j] = temp;
            } else {
                // Jeśli element nie jest mniejszy od elementu w pozycji i / threshold,
                // używamy standardowego podejścia sortowania przez wstawianie
                j = i - 1;

                while (j >= 0 && arr[j] > temp) {
                    arr[j + 1] = arr[j];
                    j--;
                }

                // Wstawiamy element na odpowiedniej pozycji
                arr[j + 1] = temp;
            }
        }
    }

    // Generowanie losowej tablicy o podanym rozmiarze
    // Tworzy nową tablicę o zadanym rozmiarze i wypełnia ją losowymi liczbami.
    public static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size);
        }

        return arr;
    }

    // Obliczanie średniego czasu wykonania dla sortowania z progiem i tradycyjnego
    // sortowania
    // Dla każdej próby kopiuje tablicę, sortuje ją używając wybranego algorytmu,
    // a następnie oblicza czas sortowania.
    // Zwraca średni czas wykonania na podstawie wszystkich prób.
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

    // Sprawdzanie czy tablica jest posortowana
    // Porównuje każdy element tablicy z następnym.
    // Jeżeli wszystkie elementy są posortowane rosnąco, zwraca prawdę, w przeciwnym
    // razie zwraca fałsz.
    public static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }

        return true;
    }

    // Główna funkcja programu
    // Testuje oba algorytmy sortowania dla różnych rozmiarów tablic.
    // Oblicza stosunek czasu sortowania z progiem do tradycyjnego sortowania.
    // Wypisuje wyniki dla każdego przypadku oraz oblicza średnią poprawę dla
    // wszystkich rozmiarów.
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
                    "Rozmiar: %d, stosunek czasu sortowania z progiem do czasu sortowania przez wstawianie: %.2f\n",
                    size, ratio);
            int[] arrCopy1 = Arrays.copyOf(arr, arr.length);
            int[] arrCopy2 = Arrays.copyOf(arr, arr.length);
            insertionSort(arrCopy1);
            thresholdSort(arrCopy2, THRESHOLD);

            if (isSorted(arrCopy1) && isSorted(arrCopy2)) {
                System.out.println("Obie tablice są posortowane.");
            } else {
                System.out.println("Błąd: Co najmniej jedna z tablic nie jest posortowana.");
            }
        }

        double avgRatio = Arrays.stream(ratios).average().orElse(0);
        int percent = (int) Math.round((1 - avgRatio) * 100);
        System.out.println("Średnia poprawa dla wszystkich rozmiarów: " + percent + "%");
    }
}
