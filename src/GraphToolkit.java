import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class GraphToolkit {
    private GraphToolkit() {
        // This is not supposed to be instantiated
        throw new IllegalStateException();
    }

    public static Set<Node> bfs(Graph g, Node src) {
        if (g == null || src == null) {
            throw new IllegalArgumentException();
        }

        // The nodes we've explored
        HashSet<Node> explored = new HashSet<>();
        explored.add(src);

        // Keep track of nodes to be visited
        Queue<Node> q = new LinkedList<>();
        q.add(src);

        while (!q.isEmpty()) {
            Node user = q.poll();
            Set<Edge> following = user.getAdjacent();
            for (Edge e : following) {
                Node u = e.getTgt();
                if (!explored.contains(u)) {
                    explored.add(u);
                    q.add(u);
                }
            }
        }

        // If there is no path
        return explored;
    }

    public static void DFS(Graph g, Node src) {
        // TODO: Implement
        // Also return something
    }

    public static void topSort(Graph g) {
        // TODO: Implement
        // Also return something
        // Make sure to check for cycles, since you need a DAG to run topsort
    }

    public static void getSCC(Graph g) {
        // TODO: Implement
        // Also return something
        // Implement Kosaraju's Algorithm for finding SCCs
    }

    public static void networkFlow(Graph g) {
        // TODO: Implement
        // Also return something
        // Implement the Edmonds-Karp algorithm
    }

    public static void btwCentrality(Graph g) {
        // TODO: Implement
        // Also return something
        // Implement Brandes' algorithm for betweenness centrality
    }

    public static void commDetection(Graph g) {
        // TODO: Implement
        // Also return something
        // Implement the Girvan-Newman algorithm for community detection
    }

    public static void pageRank(Graph g) {
        // TODO: Implement
        // Also return something
        // Implement the mathematical method of PageRank
    }

    public static void shortestPath(Graph g, Node src) {
        // TODO: Implement
        // Also return something
        // Implement Dijkstra's algorithm for SSSP
    }
}
