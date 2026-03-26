import java.util.*;

class Transaction {
    String id;
    double fee;
    String timestamp; // simple string (HH:mm)

    public Transaction(String id, double fee, String timestamp) {
        this.id = id;
        this.fee = fee;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return id + ":" + fee + "@" + timestamp;
    }
}

public class week3 {

    // Bubble Sort (by fee) - stable
    public static void bubbleSort(List<Transaction> list) {
        int n = list.size();
        int swaps = 0;
        int passes = 0;

        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            passes++;

            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).fee > list.get(j + 1).fee) {
                    // swap
                    Transaction temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);

                    swaps++;
                    swapped = true;
                }
            }

            // Early termination
            if (!swapped) break;
        }

        System.out.println("Bubble Sort Result: " + list);
        System.out.println("Passes: " + passes + ", Swaps: " + swaps);
    }

    // Insertion Sort (by fee + timestamp) - stable
    public static void insertionSort(List<Transaction> list) {
        int shifts = 0;

        for (int i = 1; i < list.size(); i++) {
            Transaction key = list.get(i);
            int j = i - 1;

            // Compare by fee, then timestamp
            while (j >= 0 && compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
                shifts++;
            }
            list.set(j + 1, key);
        }

        System.out.println("Insertion Sort Result: " + list);
        System.out.println("Total shifts: " + shifts);
    }

    // Comparator: fee first, then timestamp
    private static int compare(Transaction t1, Transaction t2) {
        if (t1.fee != t2.fee) {
            return Double.compare(t1.fee, t2.fee);
        }
        return t1.timestamp.compareTo(t2.timestamp);
    }

    // Detect high-fee outliers (> 50)
    public static void detectOutliers(List<Transaction> list) {
        System.out.print("High-fee outliers: ");
        boolean found = false;

        for (Transaction t : list) {
            if (t.fee > 50) {
                System.out.print(t + " ");
                found = true;
            }
        }

        if (!found) {
            System.out.print("none");
        }
        System.out.println();
    }

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("id1", 10.5, "10:00"));
        transactions.add(new Transaction("id2", 25.0, "09:30"));
        transactions.add(new Transaction("id3", 5.0, "10:15"));

        // Decide sorting strategy
        int size = transactions.size();

        if (size <= 100) {
            bubbleSort(transactions);
        } else if (size <= 1000) {
            insertionSort(transactions);
        } else {
            System.out.println("Use advanced sorting (not implemented)");
        }

        // For demonstration, run insertion sort separately
        insertionSort(new ArrayList<>(transactions));

        // Detect outliers
        detectOutliers(transactions);
    }
}