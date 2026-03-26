import java.util.*;

class Client {
    String name;
    int riskScore;
    double accountBalance;

    public Client(String name, int riskScore, double accountBalance) {
        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return name + ":" + riskScore + "($" + accountBalance + ")";
    }
}

public class week3{

    // 🔹 Bubble Sort (Ascending by riskScore)
    public static void bubbleSort(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    // swap
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    swaps++;
                    swapped = true;
                }
            }

            // Early stop if already sorted
            if (!swapped) break;
        }

        System.out.println("Bubble Sort (Ascending): " + Arrays.toString(arr));
        System.out.println("Total Swaps: " + swaps);
    }

    // 🔹 Insertion Sort (Descending by riskScore, then accountBalance)
    public static void insertionSortDesc(Client[] arr) {
        int n = arr.length;
        int shifts = 0;

        for (int i = 1; i < n; i++) {
            Client key = arr[i];
            int j = i - 1;

            // Descending: higher riskScore first
            // If equal riskScore → higher balance first
            while (j >= 0 && compareDesc(arr[j], key) < 0) {
                arr[j + 1] = arr[j];
                j--;
                shifts++;
            }

            arr[j + 1] = key;
        }

        System.out.println("Insertion Sort (Descending): " + Arrays.toString(arr));
        System.out.println("Total Shifts: " + shifts);
    }

    // Comparator for descending order
    private static int compareDesc(Client c1, Client c2) {
        if (c1.riskScore != c2.riskScore) {
            return Integer.compare(c1.riskScore, c2.riskScore);
        }
        return Double.compare(c1.accountBalance, c2.accountBalance);
    }

    // 🔹 Get Top K High-Risk Clients
    public static void topKClients(Client[] arr, int k) {
        System.out.print("Top " + k + " risks: ");
        for (int i = 0; i < Math.min(k, arr.length); i++) {
            System.out.print(arr[i].name + "(" + arr[i].riskScore + ") ");
        }
        System.out.println();
    }

    public static void main(String[] args) {

        Client[] clients = {
                new Client("clientC", 80, 5000),
                new Client("clientA", 20, 2000),
                new Client("clientB", 50, 3000)
        };

        // Copy arrays to preserve original data
        Client[] bubbleArray = Arrays.copyOf(clients, clients.length);
        Client[] insertionArray = Arrays.copyOf(clients, clients.length);

        // 🔹 Bubble Sort (Ascending)
        bubbleSort(bubbleArray);

        // 🔹 Insertion Sort (Descending)
        insertionSortDesc(insertionArray);

        // 🔹 Top K highest risk clients
        topKClients(insertionArray, 3);
    }
}