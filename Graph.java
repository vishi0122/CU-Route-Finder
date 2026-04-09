import java.util.*;

public class Graph {
    private Map<String, Map<String, Integer>> adjList;
    private Map<String, int[]> coordinates; // x, y for GUI

    public Graph() {
        adjList = new LinkedHashMap<>();
        coordinates = new LinkedHashMap<>();
        buildCUCampus();
    }

    private void buildCUCampus() {
        // Add all CU campus locations with GUI coordinates
        addLocation("Main Gate",           100, 500);
        addLocation("Admin Block",         220, 420);
        addLocation("UIET Block",          340, 330);
        addLocation("UIET Lab",            460, 280);
        addLocation("Library",             300, 200);
        addLocation("CSE Block",           450, 180);
        addLocation("MBA Block",           200, 300);
        addLocation("Punjabi Block",       150, 200);
        addLocation("Sports Complex",      600, 450);
        addLocation("Boys Hostel",         700, 300);
        addLocation("Girls Hostel",        680, 180);
        addLocation("Cafeteria",           400, 420);
        addLocation("Medical Center",      250, 480);
        addLocation("Auditorium",          500, 350);
        addLocation("Research Block",      560, 220);

        // Add edges (roads) with distances in meters
        addEdge("Main Gate",       "Admin Block",     150);
        addEdge("Main Gate",       "Medical Center",  200);
        addEdge("Admin Block",     "MBA Block",       180);
        addEdge("Admin Block",     "UIET Block",      220);
        addEdge("Admin Block",     "Cafeteria",       250);
        addEdge("MBA Block",       "Punjabi Block",   160);
        addEdge("MBA Block",       "Library",         200);
        addEdge("UIET Block",      "UIET Lab",        130);
        addEdge("UIET Block",      "Library",         170);
        addEdge("UIET Block",      "Auditorium",      190);
        addEdge("UIET Lab",        "CSE Block",       150);
        addEdge("UIET Lab",        "Research Block",  200);
        addEdge("Library",         "CSE Block",       180);
        addEdge("Library",         "Punjabi Block",   140);
        addEdge("CSE Block",       "Research Block",  160);
        addEdge("CSE Block",       "Girls Hostel",    220);
        addEdge("Research Block",  "Boys Hostel",     250);
        addEdge("Research Block",  "Girls Hostel",    180);
        addEdge("Auditorium",      "Sports Complex",  200);
        addEdge("Auditorium",      "Cafeteria",       160);
        addEdge("Sports Complex",  "Boys Hostel",     220);
        addEdge("Cafeteria",       "Sports Complex",  300);
        addEdge("Medical Center",  "Cafeteria",       180);
        addEdge("Punjabi Block",   "Library",         130);
        addEdge("Girls Hostel",    "Boys Hostel",     150);
    }

    public void addLocation(String name, int x, int y) {
        adjList.putIfAbsent(name, new LinkedHashMap<>());
        coordinates.put(name, new int[]{x, y});
    }

    public void addEdge(String from, String to, int weight) {
        adjList.get(from).put(to, weight);
        adjList.get(to).put(from, weight);
    }

    public Set<String> getLocations() {
        return adjList.keySet();
    }

    public Map<String, Integer> getNeighbors(String node) {
        return adjList.getOrDefault(node, new HashMap<>());
    }

    public int[] getCoordinates(String node) {
        return coordinates.getOrDefault(node, new int[]{0, 0});
    }

    public Map<String, Map<String, Integer>> getAdjList() {
        return adjList;
    }

    public Map<String, int[]> getAllCoordinates() {
        return coordinates;
    }
}
