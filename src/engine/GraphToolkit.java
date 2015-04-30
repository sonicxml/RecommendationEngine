package engine;

import java.util.List;
import java.util.Map;

/**
 * This class provides access to all of the algorithms found
 * in the Centrality, Flow, and Search classes.  It is the external
 * API for the Graph Toolkit.
 *
 */
public class GraphToolkit {
    private GraphToolkit() {
        // This is not supposed to be instantiated
        throw new IllegalStateException();
    }

    /**
     * A BFS Implementation to find the shortest path from src to tgt. Since
     * this is BFS, the shortest path is in terms of length, not weights.
     *
     * @param g    the Graph
     * @param src  the node to start BFS from
     * @param tgt  the node to end BFS at
     * @param flow whether or not this is BFS on a flow graph
     * @return the shortest path from src to tgt.
     */
    public static List<Node> bfs(Graph g, Node src, Node tgt, boolean flow) {
        return Search.bfs(g, src, tgt, flow);
    }

    public static Map<Node, List<Integer>> dfs(Graph g, Node src) {
        return Search.dfs(g, src);
    }

    public static void getSCC(Graph g) {
        // TODO: Implement
        // Also return something
        // Implement Kosaraju's Algorithm for finding SCCs
    }


    /**
     * Finds all-pairs shortest paths by implementing the
     * Floyd-Warshall algorithm
     *
     * @param g the input graph
     * @return a map representing the all-pairs shortest path matrix
     */
    public static Map<Node, Map<Node, Double>> allPairsShortestPath(Graph g) {
        return Search.floydWarshall(g);
    }

    /**
     * Implement the Ford-Fulkerson algorithm for finding maximum flow on a
     * network.
     *
     * @param g   The graph to find
     * @param src the starting node
     * @param tgt the ending node
     * @return the maximum flow from src -> tgt on the graph g
     */
    public static int maxFlow(Graph g, Node src, Node tgt) {
        return Flow.maxFlow(g, src, tgt);
    }

    /**
     * Calculates the betweeness centrality for each node using Brandes'
     * algorithm.
     *
     * @param g the Graph whose values should be calculated
     * @return a map containing the betweeness centrality values
     */
    public static Map<Node, Double> btwCentrality(Graph g) {
        return Centrality.btwCentrality(g);
    }

    public static void commDetection(Graph g) {
        Centrality.commDetection(g);
    }

    /**
     * Find the Eigenvector Centrality of a graph
     * using the dampened PageRank formula
     *
     * @param g the graph to run PageRank on
     * @return a map from node id to rank
     */
    public static Map<Integer, Double> pageRank(Graph g) {
        return Centrality.pageRank(g);
    }
}
