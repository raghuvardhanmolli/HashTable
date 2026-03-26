import java.util.*;

public class week3 {

    // ---------------- LINEAR SEARCH ----------------
    static class LinearResult {
        boolean found = false;
        int comparisons = 0;
    }

    public static LinearResult linearSearch(int[] arr, int target) {
        LinearResult res = new LinearResult();

        for (int i = 0; i < arr.length; i++) {
            res.comparisons++;
            if (arr[i] == target) {
                res.found = true;
                return res;
            }
        }
        return res;
    }

    // ---------------- BINARY SEARCH FLOOR & CEILING ----------------
    static class BinaryResult {
        Integer floor = null;
        Integer ceiling = null;
        int insertionIndex = -1;
        int comparisons = 0;
    }

    public static BinaryResult binarySearchBounds(int[] arr, int target) {
        BinaryResult res = new BinaryResult();

        int low = 0, high = arr.length - 1;

        while (low <= high) {
            res.comparisons++;

            int mid = (low + high) / 2;

            if (arr[mid] == target) {
                res.floor = arr[mid];
                res.ceiling = arr[mid];
                res.insertionIndex = mid;
                return res;
            }
            else if (arr[mid] < target) {
                res.floor = arr[mid];  // candidate floor
                low = mid + 1;
            } else {
                res.ceiling = arr[mid]; // candidate ceiling
                high = mid - 1;
            }
        }

        // insertion point = low (lower_bound)
        res.insertionIndex = low;

        return res;
    }

    // ---------------- LOWER BOUND ----------------
    public static int lowerBound(int[] arr, int target) {
        int low = 0, high = arr.length;

        while (low < high) {
            int mid = (low + high) / 2;

            if (arr[mid] < target)
                low = mid + 1;
            else
                high = mid;
        }
        return low;
    }

    // ---------------- UPPER BOUND ----------------
    public static int upperBound(int[] arr, int target) {
        int low = 0, high = arr.length;

        while (low < high) {
            int mid = (low + high) / 2;

            if (arr[mid] <= target)
                low = mid + 1;
            else
                high = mid;
        }
        return low;
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        int[] unsorted = {50, 10, 100, 25};
        int[] sorted = {10, 25, 50, 100};

        int target = 30;

        // Linear Search
        LinearResult linear = linearSearch(unsorted, target);
        System.out.println("Linear Search Found: " + linear.found);
        System.out.println("Linear Comparisons: " + linear.comparisons);

        // Binary Search Floor & Ceiling
        BinaryResult binary = binarySearchBounds(sorted, target);
        System.out.println("\nBinary Search:");
        System.out.println("Floor: " + binary.floor);
        System.out.println("Ceiling: " + binary.ceiling);
        System.out.println("Insertion Index: " + binary.insertionIndex);
        System.out.println("Comparisons: " + binary.comparisons);

        // Lower & Upper Bound
        int lb = lowerBound(sorted, target);
        int ub = upperBound(sorted, target);

        System.out.println("\nLower Bound Index: " + lb);
        System.out.println("Upper Bound Index: " + ub);
    }
}