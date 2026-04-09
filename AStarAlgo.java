import java.util.*;

public class AStarAlgo {

    public static class Result {
        public List<String> path;
        public int totalDistance;
        public int nodesVisited;

        public Result(List<String> path, int totalDistance, int nodesVisited) {
            this.path = path;
            this.totalDistance = totalDistance;
            this.nodesVisited = nodesVisited;
        }
    }

    // Euclidean distance heuristic using GUI coordinates
    private static double heuristic(Graph graph, String node, String goal) {
        int[] nc = graph.getCoordinates(node);
        int[] gc = graph.getCoordinates(goal);
        double dx = nc[0] - gc[0];
        double dy = nc[1] - gc[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static Result findPath(Graph graph, String source, String destination) {
        Map<String, Double> gScore = new HashMap<>(); // actual cost from start
        Map<String, Double> fScore = new HashMap<>(); // gScore + heuristic
        Map<String, String> prev = new HashMap<>();
        Set<String> closed = new HashSet<>();
        int nodesVisited = 0;

        for (String loc : graph.getLocations()) {
            gScore.put(loc, Double.MAX_VALUE);
            fScore.put(loc, Double.MAX_VALUE);
        }
        gScore.put(source, 0.0);
        fScore.put(source, heuristic(graph, source, destination));

        PriorityQueue<String> open = new PriorityQueue<>(
            Comparator.comparingDouble(n -> fScore.getOrDefault(n, Double.MAX_VALUE))
        );
        open.offer(source);

        while (!open.isEmpty()) {
            String current = open.poll();
            if (closed.contains(current)) continue;
            closed.add(current);
            nodesVisited++;

            if (current.equals(destination)) break;

            for (Map.Entry<String, Integer> neighborEntry : graph.getNeighbors(current).entrySet()) {
                String neighbor = neighborEntry.getKey();
                int weight = neighborEntry.getValue();
                if (closed.contains(neighbor)) continue;

                double tentativeG = gScore.get(current) + weight;
                if (tentativeG < gScore.get(neighbor)) {
                    prev.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    fScore.put(neighbor, tentativeG + heuristic(graph, neighbor, destination));
                    open.offer(neighbor);
                }
            }
        }

        List<String> path = reconstructPath(prev, source, destination);
        int totalDist = (int) gScore.getOrDefault(destination, -1.0);
        if (gScore.getOrDefault(destination, Double.MAX_VALUE) == Double.MAX_VALUE) totalDist = -1;
        return new Result(path, totalDist, nodesVisited);
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
