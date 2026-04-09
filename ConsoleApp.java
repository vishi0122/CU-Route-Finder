import java.util.*;

public class ConsoleApp {

    private static Graph graph = new Graph();
    private static Scanner scanner = new Scanner(System.in);

    public static void run() {
        printBanner();
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": findRoute(); break;
                case "2": compareAlgorithms(); break;
                case "3": showAllLocations(); break;
                case "4": showAdjacencyList(); break;
                case "5": launchGUI(); break;
                case "0": System.out.println("\n👋 Thank you for using CU Route Finder! Goodbye!\n"); return;
                default:  System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║      CU CAMPUS ROUTE FINDER — JAVA PROJECT           ║");
        System.out.println("║      Chandigarh University Navigation System         ║");
        System.out.println("║      Algorithms: Dijkstra | BFS | DFS | A*           ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    private static void printMenu() {
        System.out.println("\n┌──────────────────────────────┐");
        System.out.println("│         MAIN MENU            │");
        System.out.println("├──────────────────────────────┤");
        System.out.println("│  1. Find Route               │");
        System.out.println("│  2. Compare All Algorithms   │");
        System.out.println("│  3. Show All Locations       │");
        System.out.println("│  4. Show Campus Map (Text)   │");
        System.out.println("│  5. Launch GUI Visualizer    │");
        System.out.println("│  0. Exit                     │");
        System.out.println("└──────────────────────────────┘");
        System.out.print("Enter your choice: ");
    }

    private static void findRoute() {
        List<String> locations = new ArrayList<>(graph.getLocations());
        showLocationList(locations);

        String source = getLocation("Enter SOURCE location name: ", locations);
        if (source == null) return;
        String dest = getLocation("Enter DESTINATION location name: ", locations);
        if (dest == null) return;

        if (source.equals(dest)) {
            System.out.println("⚠️  Source and destination are the same!");
            return;
        }

        System.out.println("\nChoose Algorithm:");
        System.out.println("  1. Dijkstra (Shortest Distance)");
        System.out.println("  2. BFS (Fewest Stops)");
        System.out.println("  3. DFS (Depth-First)");
        System.out.println("  4. A* (Heuristic-based)");
        System.out.print("Choice: ");
        String algoChoice = scanner.nextLine().trim();

        System.out.println("\n" + "─".repeat(55));
        switch (algoChoice) {
            case "1": printDijkstraResult(source, dest); break;
            case "2": printBFSResult(source, dest); break;
            case "3": printDFSResult(source, dest); break;
            case "4": printAStarResult(source, dest); break;
            default: System.out.println("❌ Invalid algorithm choice.");
        }
    }

    private static void compareAlgorithms() {
        List<String> locations = new ArrayList<>(graph.getLocations());
        showLocationList(locations);

        String source = getLocation("Enter SOURCE location name: ", locations);
        if (source == null) return;
        String dest = getLocation("Enter DESTINATION location name: ", locations);
        if (dest == null) return;

        if (source.equals(dest)) {
            System.out.println("⚠️  Source and destination are the same!");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.printf("║  Route: %-44s║%n", source + " → " + dest);
        System.out.println("╠══════════════════════════════════════════════════════╣");

        // Dijkstra
        Dijkstra.Result dRes = Dijkstra.findShortestPath(graph, source, dest);
        System.out.println("║  📍 DIJKSTRA (Greedy + Priority Queue)              ║");
        printResultRow(dRes.path, dRes.totalDistance, dRes.nodesVisited);

        System.out.println("╠══════════════════════════════════════════════════════╣");

        // BFS
        BFSDFSAlgo.Result bfsRes = BFSDFSAlgo.bfs(graph, source, dest);
        System.out.println("║  📍 BFS (Breadth-First Search)                      ║");
        printResultRow(bfsRes.path, bfsRes.totalDistance, bfsRes.nodesVisited);

        System.out.println("╠══════════════════════════════════════════════════════╣");

        // DFS
        BFSDFSAlgo.Result dfsRes = BFSDFSAlgo.dfs(graph, source, dest);
        System.out.println("║  📍 DFS (Depth-First Search)                        ║");
        printResultRow(dfsRes.path, dfsRes.totalDistance, dfsRes.nodesVisited);

        System.out.println("╠══════════════════════════════════════════════════════╣");

        // A*
        AStarAlgo.Result aRes = AStarAlgo.findPath(graph, source, dest);
        System.out.println("║  📍 A* (A-Star Heuristic)                           ║");
        printResultRow(aRes.path, aRes.totalDistance, aRes.nodesVisited);

        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  🏆 BEST (shortest distance): Dijkstra / A*         ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    private static void printResultRow(List<String> path, int dist, int visited) {
        if (path.isEmpty()) {
            System.out.println("║  ❌ No path found                                   ║");
        } else {
            System.out.printf("║  Distance : %-40s║%n", dist + " meters");
            System.out.printf("║  Nodes Visited : %-37s║%n", visited);
            System.out.printf("║  Stops    : %-40s║%n", path.size());
            System.out.printf("║  Path     : %-40s║%n", truncate(String.join(" → ", path), 40));
        }
    }

    private static void printDijkstraResult(String src, String dest) {
        Dijkstra.Result res = Dijkstra.findShortestPath(graph, src, dest);
        System.out.println("🔵 DIJKSTRA'S ALGORITHM — Shortest Path");
        printFullResult(res.path, res.totalDistance, res.nodesVisited);
    }

    private static void printBFSResult(String src, String dest) {
        BFSDFSAlgo.Result res = BFSDFSAlgo.bfs(graph, src, dest);
        System.out.println("🟢 BFS ALGORITHM — Fewest Hops Path");
        printFullResult(res.path, res.totalDistance, res.nodesVisited);
    }

    private static void printDFSResult(String src, String dest) {
        BFSDFSAlgo.Result res = BFSDFSAlgo.dfs(graph, src, dest);
        System.out.println("🟡 DFS ALGORITHM — Depth-First Path");
        printFullResult(res.path, res.totalDistance, res.nodesVisited);
    }

    private static void printAStarResult(String src, String dest) {
        AStarAlgo.Result res = AStarAlgo.findPath(graph, src, dest);
        System.out.println("🔴 A* ALGORITHM — Heuristic Shortest Path");
        printFullResult(res.path, res.totalDistance, res.nodesVisited);
    }

    private static void printFullResult(List<String> path, int dist, int visited) {
        if (path.isEmpty()) {
            System.out.println("❌ No path found between the given locations.");
            return;
        }
        System.out.println("  Total Distance : " + dist + " meters");
        System.out.println("  Nodes Explored : " + visited);
        System.out.println("  Number of Stops: " + path.size());
        System.out.println("  Route:");
        for (int i = 0; i < path.size(); i++) {
            if (i == 0) System.out.println("    🚦 " + path.get(i) + " (START)");
            else if (i == path.size() - 1) System.out.println("    🏁 " + path.get(i) + " (END)");
            else System.out.println("    ➡️  " + path.get(i));
        }
    }

    private static void showAllLocations() {
        System.out.println("\n📍 ALL CU CAMPUS LOCATIONS:");
        System.out.println("─".repeat(40));
        List<String> locs = new ArrayList<>(graph.getLocations());
        for (int i = 0; i < locs.size(); i++) {
            System.out.printf("  %2d. %s%n", (i + 1), locs.get(i));
        }
    }

    private static void showLocationList(List<String> locations) {
        System.out.println("\n📍 Available Locations:");
        for (int i = 0; i < locations.size(); i++) {
            System.out.printf("  %2d. %s%n", (i + 1), locations.get(i));
        }
    }

    private static void showAdjacencyList() {
        System.out.println("\n🗺️  CU CAMPUS MAP (Adjacency List):");
        System.out.println("─".repeat(55));
        for (Map.Entry<String, Map<String, Integer>> entry : graph.getAdjList().entrySet()) {
            System.out.printf("  %-20s → ", entry.getKey());
            List<String> connections = new ArrayList<>();
            for (Map.Entry<String, Integer> nb : entry.getValue().entrySet()) {
                connections.add(nb.getKey() + "(" + nb.getValue() + "m)");
            }
            System.out.println(String.join(", ", connections));
        }
    }

    private static void launchGUI() {
        System.out.println("\n🖥️  Launching GUI Visualizer...");
        GraphGUI.launch();
    }

    private static String getLocation(String prompt, List<String> locations) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        // Try exact match first
        for (String loc : locations) {
            if (loc.equalsIgnoreCase(input)) return loc;
        }
        // Try number
        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < locations.size()) return locations.get(idx);
        } catch (NumberFormatException ignored) {}
        // Try partial match
        for (String loc : locations) {
            if (loc.toLowerCase().contains(input.toLowerCase())) return loc;
        }
        System.out.println("❌ Location not found: " + input);
        return null;
    }

    private static String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }
}
