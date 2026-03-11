import java.util.*;
import java.util.concurrent.*;

public class DNSCache {

    // DNS Entry class
    static class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime; // in milliseconds

        DNSEntry(String domain, String ipAddress, long ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final int capacity;
    private final long defaultTTL;
    private final Map<String, DNSEntry> cache;
    private long hits = 0;
    private long misses = 0;

    // Background thread executor
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    public DNSCache(int capacity, long defaultTTLSeconds) {
        this.capacity = capacity;
        this.defaultTTL = defaultTTLSeconds;

        // LinkedHashMap for LRU eviction
        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };

        // Schedule periodic cleanup of expired entries
        cleaner.scheduleAtFixedRate(this::cleanupExpiredEntries, 5, 5, TimeUnit.SECONDS);
    }

    // Resolve a domain
    public synchronized String resolve(String domain) {
        long start = System.nanoTime();
        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            long elapsed = (System.nanoTime() - start) / 1_000_000; // ms
            System.out.println("Cache HIT → " + entry.ipAddress + " (retrieved in " + elapsed + "ms)");
            return entry.ipAddress;
        } else {
            if (entry != null) {
                System.out.println("Cache EXPIRED → Querying upstream...");
                cache.remove(domain);
            } else {
                System.out.println("Cache MISS → Querying upstream...");
            }
            misses++;
            // Simulate upstream DNS query
            String ip = queryUpstreamDNS(domain);
            DNSEntry newEntry = new DNSEntry(domain, ip, defaultTTL);
            cache.put(domain, newEntry);
            long elapsed = (System.nanoTime() - start) / 1_000_000; // ms
            System.out.println(ip + " (TTL: " + defaultTTL + "s) retrieved in " + elapsed + "ms");
            return ip;
        }
    }

    // Simulate upstream DNS query
    private String queryUpstreamDNS(String domain) {
        // For demo, just generate a random IP
        Random rand = new Random(domain.hashCode());
        int part1 = 172;
        int part2 = rand.nextInt(256);
        int part3 = rand.nextInt(256);
        int part4 = rand.nextInt(256);
        return part1 + "." + part2 + "." + part3 + "." + part4;
    }

    // Cleanup expired entries
    private synchronized void cleanupExpiredEntries() {
        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> e = it.next();
            if (e.getValue().isExpired()) {
                it.remove();
                System.out.println("Expired entry removed: " + e.getKey());
            }
        }
    }

    // Cache statistics
    public void getCacheStats() {
        long total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);
        System.out.println("Cache Stats → Hit Rate: " + String.format("%.2f", hitRate) +
                "% | Hits: " + hits + " | Misses: " + misses +
                " | Current cache size: " + cache.size());
    }

    // Shutdown background thread
    public void shutdown() {
        cleaner.shutdown();
    }

    // Demo
    public static void main(String[] args) throws InterruptedException {
        DNSCache dnsCache = new DNSCache(5, 10); // max 5 entries, TTL 10s

        dnsCache.resolve("google.com");
        Thread.sleep(2000);
        dnsCache.resolve("google.com");
        dnsCache.resolve("example.com");
        dnsCache.resolve("openai.com");
        Thread.sleep(11000); // wait for TTL expiry
        dnsCache.resolve("google.com"); // should be expired
        dnsCache.getCacheStats();

        dnsCache.shutdown();
    }
}