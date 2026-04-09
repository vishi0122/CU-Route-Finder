import java.util.*;

public class Dijkstra {

    public static class Result {
        public List<String> path;
        public int totalDistance;
        public Map<String, Integer> distMap;
        public int nodesVisited;

        public Result(List<String> path, int totalDistance, Map<String, Integer> distMap, int nodesVisited) {
            this.path = path;
            this.totalDistance = totalDistance;
            this.distMap = distMap;
            this.nodesVisited = nodesVisited;
        }
    }

    public static Result findShortestPath(Graph graph, String source, String destination) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        int nodesVisited = 0;

        // Priority queue: (distance, node)
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        List<String> locations = new ArrayList<>(graph.getLocations());

        for (String loc : locations) dist.put(loc, Integer.MAX_VALUE);
        dist.put(source, 0);
        pq.offer(new int[]{0, locations.indexOf(source)});

        // Map index to name for PQ
        Map<Integer, String> idxToName = new HashMap<>();
        Map<String, Integer> nameToIdx = new HashMap<>();
        for (int i = 0; i < locations.size(); i++) {
            idxToName.put(i, locations.get(i));
            nameToIdx.put(locations.get(i), i);
        }

        // Re-run with string-based PQ for clarity
        PriorityQueue<Map.Entry<Integer, String>> queue =
            new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getKey));
        queue.offer(new AbstractMap.SimpleEntry<>(0, source));

        dist.clear();
        for (String loc : locations) dist.put(loc, Integer.MAX_VALUE);
        dist.put(source, 0);

        while (!queue.isEmpty()) {
            Map.Entry<Integer, String> entry = queue.poll();
            String u = entry.getValue();
            if (visited.contains(u)) continue;
            visited.add(u);
            nodesVisited++;

            if (u.equals(destination)) break;

            for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(u).entrySet()) {
                String v = neighbor.getKey();
                int weight = neighbor.getValue();
                if (!visited.contains(v)) {
                    int newDist = dist.get(u) + weight;
                    if (newDist < dist.get(v)) {
                        dist.put(v, newDist);
                        prev.put(v, u);
                        queue.offer(new AbstractMap.SimpleEntry<>(newDist, v));
                    }
                }
            }
        }

        List<String> path = reconstructPath(prev, source, destination);
        int totalDist = dist.getOrDefault(destination, -1);
        return new Result(path, totalDist, dist, nodesVisited);
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
