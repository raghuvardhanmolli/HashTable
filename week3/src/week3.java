import java.util.*;

public class week3 {

    // ---------------- LINEAR SEARCH ----------------
    static class LinearResult {
        int firstIndex = -1;
        int lastIndex = -1;
        int comparisons = 0;
    }

    public static LinearResult linearSearch(String[] arr, String target) {
        LinearResult res = new LinearResult();

        for (int i = 0; i < arr.length; i++) {
            res.comparisons++;

            if (arr[i].equals(target)) {
                if (res.firstIndex == -1) {
                    res.firstIndex = i;
                }
                res.lastIndex = i;
            }
        }
        return res;
    }

    // ---------------- BINARY SEARCH ----------------
    static class BinaryResult {
        int index = -1;
        int count = 0;
        int comparisons = 0;
    }

    public static BinaryResult binarySearch(String[] arr, String target) {
        BinaryResult res = new BinaryResult();

        int low = 0, high = arr.length - 1;

        while (low <= high) {
            res.comparisons++;

            int mid = (low + high) / 2;

            int cmp = arr[mid].compareTo(target);

            if (cmp == 0) {
                res.index = mid;

                // Count duplicates (expand left & right)
                int left = mid, right = mid;

                // Count left side
                while (left >= 0 && arr[left].equals(target)) {
                    res.count++;
                    left--;
                    res.comparisons++;
                }

                // Count right side
                right = mid + 1;
                while (right < arr.length && arr[right].equals(target)) {
                    res.count++;
                    right++;
                    res.comparisons++;
                }

                return res;
            }
            else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return res;
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        // Unsorted logs
        String[] logs = {"accB", "accA", "accB", "accC"};

        String target = "accB";

        // Linear Search
        LinearResult linear = linearSearch(logs, target);
        System.out.println("Linear Search:");
        System.out.println("First Index: " + linear.firstIndex);
        System.out.println("Last Index: " + linear.lastIndex);
        System.out.println("Comparisons: " + linear.comparisons);

        // Sort logs for Binary Search
        Arrays.sort(logs);

        System.out.println("\nSorted Logs: " + Arrays.toString(logs));

        // Binary Search
        BinaryResult binary = binarySearch(logs, target);
        System.out.println("Binary Search:");
        System.out.println("Found Index: " + binary.index);
        System.out.println("Count: " + binary.count);
        System.out.println("Comparisons: " + binary.comparisons);
    }
}