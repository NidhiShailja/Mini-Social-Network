import java.util.*;

/**
 * Mini Social Network (Console)
 * Concepts: Graph (adjacency list), BFS, Hashing (HashMap), Linked List
 */
public class MiniSocialNetwork {

    // ---------- LINKED LIST NODE: one post ----------
    static class Post {
        int id;
        String author;
        String content;
        Post next;               // next OLDER post

        Post(int id, String author, String content) {
            this.id = id;
            this.author = author;
            this.content = content;
        }
    }

    // ---------- USER: holds head of his own posts linked list ----------
    static class User {
        String name;
        Post head;               // newest post is always at the head

        User(String name) {
            this.name = name;
        }
    }

    // ---------- DATA STRUCTURES ----------
    // HashMap: username -> User (fast lookup, no duplicates)
    static HashMap<String, User> users = new HashMap<>();
    // Directed graph as adjacency list: username -> list of users he follows
    static HashMap<String, List<String>> graph = new HashMap<>();
    // Global post counter (IDs start from 1)
    static int postCounter = 1;

    // ---------- 1. REGISTER ----------
    static void register(String name) {
        if (name.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        if (users.containsKey(name)) {
            System.out.println("User " + name + " already exists.");
            return;
        }
        users.put(name, new User(name));
        graph.put(name, new ArrayList<>());
        System.out.println("Users registered.");
    }

    // ---------- 2. FOLLOW ----------
    static void follow(String a, String b) {
        if (!users.containsKey(a)) {
            System.out.println("User " + a + " not found.");
            return;
        }
        if (!users.containsKey(b)) {
            System.out.println("User " + b + " not found.");
            return;
        }
        if (a.equals(b)) {
            System.out.println("You cannot follow yourself.");
            return;
        }
        if (graph.get(a).contains(b)) {
            System.out.println(a + " already follows " + b + ".");
            return;
        }
        graph.get(a).add(b);                       // add edge a -> b
        System.out.println(a + " now follows " + b + ".");
    }

    // ---------- 3. CREATE POST ----------
    static void createPost(String name, String content) {
        if (!users.containsKey(name)) {
            System.out.println("User " + name + " not found.");
            return;
        }
        if (content.isEmpty()) {
            System.out.println("Post cannot be empty.");
            return;
        }
        User u = users.get(name);
        Post p = new Post(postCounter++, name, content);
        p.next = u.head;           // insert at head -> newest first
        u.head = p;
        System.out.println("Post " + p.id + " created.");
    }

    // ---------- 4. VIEW FEED (merge linked lists, newest first) ----------
    static void viewFeed(String name) {
        if (!users.containsKey(name)) {
            System.out.println("User " + name + " not found.");
            return;
        }

        // pointers: current post of each followed user
        List<Post> current = new ArrayList<>();
        for (String f : graph.get(name)) {
            Post head = users.get(f).head;
            if (head != null) current.add(head);
        }

        if (current.isEmpty()) {
            System.out.println("Your feed is empty.");
            return;
        }

        boolean first = true;
        while (!current.isEmpty()) {
            // find the list whose current post has the highest ID (newest)
            int best = 0;
            for (int i = 1; i < current.size(); i++) {
                if (current.get(i).id > current.get(best).id) best = i;
            }
            Post p = current.get(best);
            String line = "[Post " + p.id + "] " + p.author + ": " + p.content;
            System.out.println(first ? "Feed: " + line : line);
            first = false;

            // move that list's pointer forward
            if (p.next != null) current.set(best, p.next);
            else current.remove(best);
        }
    }

    // ---------- 5. SUGGESTIONS using BFS (depth up to 2) ----------
    static void suggest(String name) {
        if (!users.containsKey(name)) {
            System.out.println("User " + name + " not found.");
            return;
        }

        Map<String, Integer> depth = new LinkedHashMap<>();
        Map<String, String> parent = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        depth.put(name, 0);
        queue.add(name);

        while (!queue.isEmpty()) {
            String cur = queue.poll();
            if (depth.get(cur) == 2) continue;     // do not go deeper than 2

            for (String nb : graph.get(cur)) {
                if (!depth.containsKey(nb)) {      // not visited yet
                    depth.put(nb, depth.get(cur) + 1);
                    parent.put(nb, cur);
                    queue.add(nb);
                }
            }
        }

        // Users at depth 2 are followed by someone I follow, and I don't follow them
        boolean found = false;
        for (Map.Entry<String, Integer> e : depth.entrySet()) {
            if (e.getValue() == 2) {
                System.out.println("Suggested: " + e.getKey()
                        + " (followed by " + parent.get(e.getKey()) + ")");
                found = true;
            }
        }
        if (!found) System.out.println("No suggestions available.");
    }

    // ---------- EXTRA: list all users ----------
    static void listUsers() {
        if (users.isEmpty()) {
            System.out.println("No users registered yet.");
            return;
        }
        for (String u : users.keySet()) {
            System.out.println(u + " follows " + graph.get(u));
        }
    }

    // ---------- DEMO: runs the 4 test cases from the problem statement ----------
    static void clearAll() {
        users.clear();
        graph.clear();
        postCounter = 1;
    }

    static void runDemo() {
        System.out.println("\n=== Test Case 1: Normal Input ===");
        clearAll();
        register("Riya");
        register("Aman");
        follow("Riya", "Aman");
        createPost("Aman", "Hello DSA");
        viewFeed("Riya");

        System.out.println("\n=== Test Case 2: Boundary / Minimum Input ===");
        clearAll();
        register("Karan");
        viewFeed("Karan");

        System.out.println("\n=== Test Case 3: Invalid Input ===");
        clearAll();
        register("Riya");
        follow("Riya", "Neha");

        System.out.println("\n=== Test Case 4: Different Valid Scenario ===");
        clearAll();
        register("Riya");
        register("Aman");
        register("Karan");
        follow("Riya", "Aman");
        follow("Aman", "Karan");
        createPost("Aman", "Hello DSA");     // Post 1
        createPost("Karan", "Graphs rock");  // (a) Post 2
        suggest("Riya");                     // (b)
        follow("Riya", "Karan");             // (c)
        viewFeed("Riya");

        clearAll();
        System.out.println("\n=== Demo finished (data cleared) ===");
    }

    // ---------- MAIN MENU ----------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== MINI SOCIAL NETWORK =====");
            System.out.println("1. Register user(s)");
            System.out.println("2. Follow a user");
            System.out.println("3. Create a post");
            System.out.println("4. View feed");
            System.out.println("5. Suggest people to follow");
            System.out.println("6. List all users");
            System.out.println("7. Run demo (4 test cases)");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter username(s) (comma separated for many): ");
                    String[] names = sc.nextLine().split(",");
                    for (String n : names) {
                        register(n.trim());
                    }
                    break;
                case "2":
                    System.out.print("Who is following (your name): ");
                    String a = sc.nextLine().trim();
                    System.out.print("Whom to follow: ");
                    String b = sc.nextLine().trim();
                    follow(a, b);
                    break;
                case "3":
                    System.out.print("Your username: ");
                    String author = sc.nextLine().trim();
                    System.out.print("Write your post: ");
                    String text = sc.nextLine().trim();
                    createPost(author, text);
                    break;
                case "4":
                    System.out.print("Your username: ");
                    viewFeed(sc.nextLine().trim());
                    break;
                case "5":
                    System.out.print("Your username: ");
                    suggest(sc.nextLine().trim());
                    break;
                case "6":
                    listUsers();
                    break;
                case "7":
                    runDemo();
                    break;
                case "0":
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 0-7.");
            }
        }
    }
}