import java.util.*;

public class socialmedia {

    // username -> userId
    static HashMap<String, Integer> userDatabase = new HashMap<>();

    // username -> attempt count
    static HashMap<String, Integer> attemptFrequency = new HashMap<>();

    // Check username availability
    public static boolean checkAvailability(String username) {

        // Track attempts
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        return !userDatabase.containsKey(username);
    }

    // Suggest alternative usernames
    public static List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;

            if (!userDatabase.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // replace underscore with dot
        String dotVersion = username.replace("_", ".");
        if (!userDatabase.containsKey(dotVersion)) {
            suggestions.add(dotVersion);
        }

        return suggestions;
    }

    // Get most attempted username
    public static String getMostAttempted() {

        String mostAttempted = "";
        int max = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > max) {
                max = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Existing usernames
        userDatabase.put("john_doe", 101);
        userDatabase.put("admin", 102);
        userDatabase.put("player1", 103);

        System.out.println("Enter username to check:");
        String username = sc.nextLine();

        boolean available = checkAvailability(username);

        if (available) {
            System.out.println(username + " is available");
        } else {

            System.out.println(username + " is already taken");

            System.out.println("Suggested alternatives:");
            List<String> suggestions = suggestAlternatives(username);

            for (String s : suggestions) {
                System.out.println(s);
            }
        }

        System.out.println("Most attempted username: " + getMostAttempted());

        sc.close();
    }
}