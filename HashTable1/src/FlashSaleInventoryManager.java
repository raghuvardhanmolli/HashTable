import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FlashSaleInventoryManager {

    // productId -> stock count
    private ConcurrentHashMap<String, AtomicInteger> stockMap;

    // Waiting list per product (FIFO)
    private Map<String, LinkedList<Integer>> waitingList;

    public FlashSaleInventoryManager() {
        stockMap = new ConcurrentHashMap<>();
        waitingList = new ConcurrentHashMap<>();
    }

    // Initialize product stock
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, new AtomicInteger(stock));
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock availability
    public int checkStock(String productId) {
        AtomicInteger stock = stockMap.get(productId);
        if (stock != null) {
            return stock.get();
        }
        return 0;
    }

    // Purchase item
    public String purchaseItem(String productId, int userId) {

        AtomicInteger stock = stockMap.get(productId);
        if (stock == null) {
            return "Product not found";
        }

        synchronized (stock) { // synchronize decrement for thread safety
            if (stock.get() > 0) {
                int remaining = stock.decrementAndGet();
                return "Success, " + remaining + " units remaining";
            } else {
                // Add to waiting list
                LinkedList<Integer> queue = waitingList.get(productId);
                queue.addLast(userId);
                int position = queue.size();
                return "Added to waiting list, position #" + position;
            }
        }
    }

    // Get waiting list for a product
    public List<Integer> getWaitingList(String productId) {
        LinkedList<Integer> queue = waitingList.get(productId);
        if (queue != null) {
            return new ArrayList<>(queue);
        }
        return Collections.emptyList();
    }

    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        // Initialize product stock
        manager.addProduct("IPHONE15_256GB", 5); // reduced to 5 for demo

        // Simulate purchases
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 101)); // Success
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 102)); // Success
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 103)); // Success
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 104)); // Success
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 105)); // Success
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 106)); // Waiting list

        System.out.println("\nCurrent stock: " + manager.checkStock("IPHONE15_256GB"));
        System.out.println("Waiting list: " + manager.getWaitingList("IPHONE15_256GB"));
    }
}