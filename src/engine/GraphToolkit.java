package engine;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

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
     * @param g      the Graph
     * @param srcID  the node to start BFS from
     * @param tgtID  the node to end BFS at
     * @param flow   whether or not this is BFS on a flow graph
     * @return       the shortest path from src to tgt.
     */
    public static List<Integer> bfs(Graph g, int srcID, int tgtID, boolean flow) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        Node src = g.getNodeByID(srcID);
        Node tgt = g.getNodeByID(tgtID);
        
        if (src == null || tgt == null) {
            throw new IllegalArgumentException();
        }
        
        return convertList(Search.bfs(g, src, tgt, flow));
    }
    
	/**
	 * Function performs DFS on a given graph and returns 
	 * a map of nodes in the graph to a list of start and
	 * finish times. Function does not restart DFS if
	 * all nodes have not been visited yet.
	 * 
	 * @param g the Graph
	 * @param src the start Node
	 * @return A mapping of node to start and finish times
	 */
    public static Map<Integer, List<Integer>> dfsTree(Graph g, int srcID) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        Node src = g.getNodeByID(srcID);
        
        if (src == null) {
            throw new IllegalArgumentException();
        }
        
        return convertIntMap(Search.dfsTree(g, src));
    }
    
	/**
	 * Function performs DFS on given graph and returns
	 * a Map of nodes in the graph to a list of integers. 
	 * Function restarts DFS if any node in graph has
	 * not been visited yet. 
	 * 
	 * @param g the Graph
	 * @param src the node to start DFS
	 * @return mapping of nodes to start and finish
	 * times. 
	 */
    public static Map<Integer, List<Integer>> dfsForest(Graph g, int srcID) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        Node src = g.getNodeByID(srcID);
        
        if (src == null) {
            throw new IllegalArgumentException();
        }
        
        return convertIntMap(Search.dfsForest(g, src));
    }

    /**
     * Method for returning the strongly connected components of
     * a given Graph, using Kosaraju's algorithm.
     * 
     * @param g  the Graph on which to run Kosaraju's
     * @return   the Set of connected components
     */
    public static Set<Set<Integer>> getSCC(Graph g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        return Connectivity.getSCC(g);
    }
    
	/**
	 * Function performs a topological sort on the vertices
	 * of a given graph by performing DFS and arrange
	 * vertices by descending finish times. 
	 * 
	 * @param g the Graph
	 * @return a topological sort of the vertices in the
	 * graph. 
	 */
    public static List<Integer> topSort(Graph g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        return convertList(Search.topSort(g)); 
    }

    /**
     * Finds single-source shortest paths by implementing the Bellman-Ford
     * algorithm
     *
     * @param g the input graph
     * @param src the starting node
     * @return a map with shortest paths distances from src to every other node
     */
    public static Map<Integer, Double> singleSourceShortestPath(Graph g, int srcID) {
        if (g == null) {
            throw new IllegalArgumentException("Null input");
        }

        Node src = g.getNodeByID(srcID);
        if (src == null) {
            throw new IllegalArgumentException();
        }
        
        return convertMap(Search.bellmanFord(g, src));
    }

    /**
     * Finds all-pairs shortest paths by implementing the
     * Floyd-Warshall algorithm
     *
     * @param g the input graph
     * @return a map representing the all-pairs shortest path matrix
     */
    public static Map<Integer, Map<Integer, Double>> allPairsShortestPath(Graph g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        return convertFloyd(Search.floydWarshall(g));
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
    public static int maxFlow(Graph g, int srcID, int tgtID) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        Node src = g.getNodeByID(srcID);
        Node tgt = g.getNodeByID(tgtID);
        if (src == null || tgt == null) {
            throw new IllegalArgumentException();
        }
        
        return Flow.maxFlow(g, src, tgt);
    }

    /**
     * Calculates the betweeness centrality for each node using Brandes'
     * algorithm.
     *
     * @param g the Graph whose values should be calculated
     * @return a map containing the betweeness centrality values
     */
    public static Map<Integer, Double> btwCentrality(Graph g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        return convertMap(Centrality.btwCentrality(g));
    }

    public static void commDetection(Graph g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
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
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        return Centrality.pageRank(g);
    }
    
    private static Map<Integer, Double> convertMap(Map<Node, Double> nodeMap) {
        if (nodeMap == null) {
            throw new IllegalArgumentException();
        }
        
        Map<Integer, Double> result = new HashMap<>();
        
        for (Node node : nodeMap.keySet()) {
            result.put(node.getID(), nodeMap.get(node));
        }
        
        return result;
    }
    
    private static Map<Integer, List<Integer>> convertIntMap(Map<Node, List<Integer>> nodeMap) {
        if (nodeMap == null) {
            throw new IllegalArgumentException();
        }
        
        Map<Integer, List<Integer>> result = new HashMap<>();
        
        for (Node node : nodeMap.keySet()) {
            result.put(node.getID(), nodeMap.get(node));
        }
        
        return result;
    }
    
    private static List<Integer> convertList(List<Node> nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException();
        }
        
        List<Integer> result = new LinkedList<>();
        
        for (Node node : nodes) {
            result.add(node.getID());
        }
        
        return result;
    }
    
    private static Map<Integer, Map<Integer, Double>> convertFloyd(Map<Node, 
            Map<Node, Double>> nodeMap) {
        if (nodeMap == null) {
            throw new IllegalArgumentException();
        }
        
        Map<Integer, Map<Integer, Double>> result = new HashMap<>();
        for (Node node : nodeMap.keySet()) {
            result.put(node.getID(), convertMap(nodeMap.get(node)));
        }
        
        return result;
    }
}
