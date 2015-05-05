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
     * Wrapper for BFS.
     * @see Search#bfs(Graph, Node, Node, boolean)
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
	 * Wrapper for DFS that outputs a DFS Tree.
     * @see Search#dfsTree(Graph, Node)
	 * 
	 * @param g the Graph
	 * @param srcID the start Node's ID
	 * @return A DFS tree mapping of nodes to start and finish times
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
	 * Wrapper for DFS that outputs a DFS Forest.
     * @see Search#dfsForest(Graph, Node)
	 * 
	 * @param g the Graph
	 * @param srcID the node to start DFS
	 * @return A DFS Forest mapping of nodes to start and finish
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
     * Wrapper for Kosaraju's algorithm.
     * @see Connectivity#getSCC(Graph)
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
	 * Wrapper for topological sort.
     * @see Search#topSort(Graph)
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
     * Wrapper for the Bellman-Ford algorithm.
     * @see Search#bellmanFord(Graph, Node)
     *
     * @param g the input graph
     * @param srcID the starting node's ID
     * @return a map with shortest paths distances from src to every other node
     */
    public static Map<Integer, Double> getSingleSourceShortestPath(Graph g,
                                                                   int srcID) {
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
     * Wrapper for the Floyd-Warshall algorithm.
     * @see Search#floydWarshall(Graph)
     *
     * @param g the input graph
     * @return a map representing the all-pairs shortest path matrix
     */
    public static Map<Integer, Map<Integer, Double>> getAllPairsShortestPath(
            Graph g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        return convertFloyd(Search.floydWarshall(g));
    }

    /**
     * Wrapper for the Ford-Fulkerson algorithm.
     * @see Flow#getMaxFlow(Graph, Node, Node)
     *
     * @param g   The graph to find
     * @param srcID the starting node's ID
     * @param tgtID the ending node's ID
     * @return the maximum flow from src -> tgt on the graph g
     */
    public static int getMaxFlow(Graph g, int srcID, int tgtID) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        Node src = g.getNodeByID(srcID);
        Node tgt = g.getNodeByID(tgtID);
        if (src == null || tgt == null) {
            throw new IllegalArgumentException();
        }
        
        return Flow.getMaxFlow(g, src, tgt);
    }

    /**
     * Wrapper for Brandes' Betweenness Centrality algorithm.
     * @see Centrality#btwCentrality(Graph)
     *
     * @param g the Graph whose values should be calculated
     * @return a map containing the betweeness centrality values
     */
    public static Map<Integer, Double> getBetweennessCentrality(Graph g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        return convertMap(Centrality.btwCentrality(g));
    }

    /**
     * Wrapper for the PageRank algorithm.
     * @see Centrality#pageRank(Graph)
     *
     * @param g the graph to run PageRank on
     * @return a map from node ID to rank
     */
    public static Map<Integer, Double> getPageRank(Graph g) {
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
    
    private static Map<Integer, List<Integer>> convertIntMap(
            Map<Node, List<Integer>> nodeMap) {
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
