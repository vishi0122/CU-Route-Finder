import java.util.*;

public class BFSDFSAlgo {

    public static class Result {
        public List<String> path;
        public int totalDistance;
        public int nodesVisited;
        public String algorithmName;

        public Result(List<String> path, int totalDistance, int nodesVisited, String algorithmName) {
            this.path = path;
            this.totalDistance = totalDistance;
            this.nodesVisited = nodesVisited;
            this.algorithmName = algorithmName;
        }
    }

    // BFS - finds path with fewest hops (not necessarily shortest distance)
    public static Result bfs(Graph graph, String source, String destination) {
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        int nodesVisited = 0;

        queue.offer(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            nodesVisited++;

            if (curr.equals(destination)) break;

            for (String neighbor : graph.getNeighbors(curr).keySet()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    prev.put(neighbor, curr);
                    queue.offer(neighbor);
                }
            }
        }

        List<String> path = reconstructPath(prev, source, destination);
        int dist = calculateDistance(graph, path);
        return new Result(path, dist, nodesVisited, "BFS");
    }

    // DFS - explores as deep as possible before backtracking
    public static Result dfs(Graph graph, String source, String destination) {
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();
        int[] nodesVisited = {0};

        stack.push(source);

        while (!stack.isEmpty()) {
            String curr = stack.pop();
            if (visited.contains(curr)) continue;
            visited.add(curr);
            nodesVisited[0]++;

            if (curr.equals(destination)) break;

            for (String neighbor : graph.getNeighbors(curr).keySet()) {
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                    if (!prev.containsKey(neighbor)) {
                        prev.put(neighbor, curr);
                    }
                }
            }
        }

        List<String> path = reconstructPath(prev, source, destination);
        int dist = calculateDistance(graph, path);
        return new Result(path, dist, nodesVisited[0], "DFS");
    }

    public static int calculateDistance(Graph graph, List<String> path) {
        if (path == null || path.size() < 2) return -1;
        int total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Map<String, Integer> neighbors = graph.getNeighbors(path.get(i));
            Integer w = neighbors.get(path.get(i + 1));
            if (w == null) return -1;
            total += w;
        }
        return total;
    }

    private static List<String> reconstructPath(Map<String, String> prev, String source, String dest) {
        List<String> path = new ArrayList<>();
        String curr = dest;
        while (curr != null && !curr.equals(source)) {
            path.add(0, curr);
            curr = prev.get(curr);
        }
        if (curr != null) path.add(0, source);
        return path.size() > 1 ? path : new ArrayList<>();
    }
}
