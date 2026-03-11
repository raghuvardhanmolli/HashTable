import java.util.*;
import java.nio.file.*;
import java.io.IOException;

public class PlagiarismDetector {

    // n-gram size
    private final int N_GRAM_SIZE;

    // n-gram -> set of document IDs
    private Map<String, Set<String>> nGramIndex;

    public PlagiarismDetector(int nGramSize) {
        this.N_GRAM_SIZE = nGramSize;
        this.nGramIndex = new HashMap<>();
    }

    // Extract n-grams from text
    private List<String> extractNGrams(String text) {
        String[] words = text.toLowerCase().replaceAll("[^a-z0-9 ]", "").split("\\s+");
        List<String> nGrams = new ArrayList<>();

        for (int i = 0; i <= words.length - N_GRAM_SIZE; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N_GRAM_SIZE; j++) {
                if (j > 0) sb.append(" ");
                sb.append(words[i + j]);
            }
            nGrams.add(sb.toString());
        }
        return nGrams;
    }

    // Index a document
    public void indexDocument(String docId, String content) {
        List<String> nGrams = extractNGrams(content);

        for (String nGram : nGrams) {
            nGramIndex.computeIfAbsent(nGram, k -> new HashSet<>()).add(docId);
        }

        System.out.println("Document " + docId + " indexed with " + nGrams.size() + " n-grams.");
    }

    // Analyze a new document for plagiarism
    public void analyzeDocument(String docId, String content) {
        List<String> nGrams = extractNGrams(content);

        // Count matches per existing document
        Map<String, Integer> matches = new HashMap<>();

        for (String nGram : nGrams) {
            Set<String> docs = nGramIndex.get(nGram);
            if (docs != null) {
                for (String existingDoc : docs) {
                    matches.put(existingDoc, matches.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        // Print similarity
        for (Map.Entry<String, Integer> entry : matches.entrySet()) {
            int matchCount = entry.getValue();
            double similarity = matchCount * 100.0 / nGrams.size();
            String status = similarity > 50 ? "PLAGIARISM DETECTED" : "suspicious";

            System.out.println("Found " + matchCount + " matching n-grams with \"" +
                    entry.getKey() + "\" → Similarity: " +
                    String.format("%.1f", similarity) + "% (" + status + ")");
        }
    }

    // Load file content
    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    // Demo
    public static void main(String[] args) throws IOException {
        PlagiarismDetector detector = new PlagiarismDetector(5); // 5-grams

        // Index previous essays
        detector.indexDocument("essay_089.txt", "The quick brown fox jumps over the lazy dog repeatedly for testing.");
        detector.indexDocument("essay_092.txt", "A fast brown fox jumps over lazy dogs for checking plagiarism.");

        // Analyze new essay
        String newEssay = "The quick brown fox jumps over lazy dogs for testing plagiarism detection purposes.";
        detector.analyzeDocument("essay_123.txt", newEssay);
    }
}