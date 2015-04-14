import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;



public class GraphToolkit {
    private GraphToolkit() {
        // This is not supposed to be instantiated
        throw new IllegalStateException();
    }

    /**
     * A BFS Implementation to find the shortest path from src to tgt.
     * Since this is BFS, the shortest path is in terms of length, not weights.
     * @param g the Graph
     * @param src the node to start BFS from
     * @return the shortest path from src to tgt.
     */
    public static List<Node> bfs(Graph g, Node src, Node tgt) {
        if (g == null || src == null) {
            throw new IllegalArgumentException();
        }

        if (src == tgt) {
            LinkedList<Node> output = new LinkedList<>();
            output.add(tgt);
            return output;
        }

        // The nodes we've explored
        HashSet<Node> explored = new HashSet<>();
        explored.add(src);

        // Keeps track of the parents of each node
        Map<Node, Node> parents = new HashMap<>();
        parents.put(src, null);

        // Keep track of nodes to be visited
        Queue<Node> q = new LinkedList<>();
        q.add(src);

        while (!q.isEmpty()) {
            Node user = q.poll();
            Set<Edge> following = user.getEdges();
            for (Edge e : following) {

                Node n = e.getTgt();

                if (n == tgt) {
                    parents.put(n, user);
                    // We found the target node
                    Node i = n;
                    LinkedList<Node> output = new LinkedList<>();
                    output.addFirst(i);
                    while (parents.get(i) != null) {
                        output.addFirst(parents.get(i));
                        i = parents.get(i);
                    }
                    return output;
                }

                if (!explored.contains(n)) {
                    explored.add(n);
                    q.add(n);
                    parents.put(n, user);
                }
            }
        }

        // If there is no path
        return new LinkedList<>();
    }

    public static void dfs(Graph g, Node src) {
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

    public static Map<Integer, Double> pageRank(Graph g) {
        // Implement the mathematical method of PageRank
        double DF = 0.85; // Damping Factor

    	// Node ID to int
    	Map<Integer, Integer> idMap = new HashMap<>();
        // Reverse of the above map
        Map<Integer, Integer> revIDMap = new HashMap<>();

        Matrix adjMatrix = new Basic2DMatrix();
        Set<Node> nodes = g.getAllNodes();
    	for (Node node : nodes) {
    		if (!idMap.containsKey(node.getID())) {
    			idMap.put(node.getID(), idMap.size());
                revIDMap.put(idMap.size(), node.getID());
    		}
    	}

    	for (Node node : nodes) {
            double cellEntry = (double) (1 / node.getOutDegree());
    		Set<Edge> edges = node.getEdges();
    		for (Edge e : edges) {
    			adjMatrix.set(idMap.get(e.getSrc().getID()), 
                                idMap.get(e.getTgt().getID()),
                                cellEntry);
    		}
    	}

        int size = adjMatrix.rows() * adjMatrix.columns();
        for (int i = 0; i < adjMatrix.rows(); i++) {
            for (int j = 0; j < adjMatrix.columns(); j++) {
                Double val = adjMatrix.get(i, j);
                Double dampedVal = (DF * val) + ((1 - DF) / size);
                adjMatrix.set(i, j, dampedVal);
            }
        }

        EigenDecompositor ed = new EigenDecompositor(adjMatrix);
        Matrix[] vd = ed.decompose();
        Matrix v = vd[0];
        Matrix d = vd[1];
        double max = Double.MIN_VALUE;
        int maxIdx = 0;
        for (int i = 0; i < d.columns(); i++) {
            double colMax = d.maxInColumn(i);
            if (colMax > max) {
                max = colMax;
                maxIdx = i;
            }
        }

        Vector principalEV = v.getColumn(maxIdx);
        Map<Integer, Double> ranks = new HashMap<>();
        for (int i = 0; i < principalEV.length(); i++) {
            ranks.put(revIDMap.get(i), principalEV.get(i));
        }

        return ranks;
    }

    public static void shortestPath(Graph g, Node src) {
        // TODO: Implement
        // Also return something
        // Implement Dijkstra's algorithm for SSSP
    }
}
