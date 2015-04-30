package engine;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

class Centrality {
    private Centrality() {
        // This class should not be instantiated
        throw new IllegalStateException();
    }

    /**
     * Calculates the betweeness centrality for each node using Brandes'
     * algorithm.
     *
     * @param g the Graph whose values should be calculated
     * @return a map containing the betweeness centrality values
     */
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    static Map<Node, Double> btwCentrality(Graph g) {
        Set<Node> nodes = g.getAllNodes();
        Map<Node, Integer> nodeMap = new HashMap<>();
        int size = nodes.size();
        Map<Node, Double> centrality = new HashMap<>();

        for (Node node : nodes) {
            nodeMap.put(node, nodeMap.size());
            centrality.put(node, 0.0);
        }

        for (Node node : nodes) {
            int index = nodeMap.get(node);
            Stack<Node> s = new Stack<>();
            Queue<Node> q = new LinkedList<>();
            q.add(node);
            ArrayList<Node>[] lists = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                lists[i] = new ArrayList<Node>();
            }
            int[] sigma = new int[size];
            sigma[index] = 1;
            int[] distances = new int[size];
            for (int i = 0; i < size; i++) {
                if (i != index) {
                    distances[i] = -1;
                }
            }
            double[] deltas = new double[size];

            while (!q.isEmpty()) {
                Node v = q.remove();
                s.push(v);
                Set<Node> neighbors = v.getNeighbors();
                for (Node neighbor : neighbors) {
                    int tempIndex = nodeMap.get(neighbor);
                    if (distances[tempIndex] < 0) {
                        q.add(neighbor);
                        distances[tempIndex] = distances[nodeMap.get(v)] + 1;
                    } 
                    if (distances[tempIndex] == distances[nodeMap.get(v)] + 1) {
                        sigma[tempIndex] = sigma[tempIndex] + sigma[nodeMap.get(v)];
                        lists[tempIndex].add(v);
                    }
                }
            }

            while (!s.isEmpty()) {
                Node w = s.pop();
                int wIndex = nodeMap.get(w);
                ArrayList<Node> list = lists[nodeMap.get(w)];
                for (Node v : list) {
                    int temp = nodeMap.get(v);
                    deltas[temp] = deltas[temp] + ((double) sigma[temp] /
                            (double) sigma[wIndex]) * (1 + deltas[wIndex]);

                }
                if (wIndex != index) {
                    centrality.put(w, centrality.get(w) + deltas[wIndex]);
                }
            }
        }
        return centrality;
    }

    static void commDetection(Graph g) {
        // TODO: Implement
        // Also return something
        // Implement the Girvan-Newman algorithm for community detection
    }

    /**
     * Find the Eigenvector Centrality of a graph
     * using the dampened PageRank formula
     *
     * @param g the graph to run PageRank on
     * @return a map from node id to rank
     */
    static Map<Integer, Double> pageRank(Graph g) {
        double DF = 0.85; // Damping Factor

        // Node ID to int
        Map<Integer, Integer> idMap = new HashMap<>();
        // Reverse of the above map
        Map<Integer, Integer> revIDMap = new HashMap<>();

        // Generate ID maps
        Set<Node> nodes = g.getAllNodes();
        for (Node node : nodes) {
            if (!idMap.containsKey(node.getID())) {
                idMap.put(node.getID(), idMap.size());
                revIDMap.put(idMap.size() - 1, node.getID());
            }
        }

        // Get the adjacency matrix
        Matrix adjMatrix = getAdjMat(idMap, nodes);

        // Dampen the matrix according to Scaled PageRank
        dampenMatrix(DF, adjMatrix);

        // Get the Principal Eigenvector
        // (Eigenvector corresponding to largest eigenvalue)
        Vector principalEV = getPrincipalEV(adjMatrix);

        // Map Node IDs to their Eigenvector Centrality
        Map<Integer, Double> ranks = new HashMap<>();
        for (int i = 0; i < principalEV.length(); i++) {
            ranks.put(revIDMap.get(i), principalEV.get(i));
        }

        return ranks;
    }

    /**
     * Uses the EigenDecompositor class from package la4j to find the principal
     * eigenvector, which corresponds to the largest eigenvector of the supplied
     * matrix.
     *
     * @param adjMatrix an adjacency matrix for a graph
     * @return the principal eigenvector of adjMatrix
     */
    private static Vector getPrincipalEV(Matrix adjMatrix) {
        EigenDecompositor ed = new EigenDecompositor(adjMatrix.transpose());
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

        return v.getColumn(maxIdx);
    }
    
    /**
     * Adjusts the values in the given matrix according to the formula for the
     * dampened PageRank algorithm
     *
     * @param DF        the damping factor (default 0.85)
     * @param adjMatrix the matrix whose contents should be modified
     */
    private static void dampenMatrix(double DF, Matrix adjMatrix) {
        int size = adjMatrix.rows();
        for (int i = 0; i < adjMatrix.rows(); i++) {
            for (int j = 0; j < adjMatrix.columns(); j++) {
                double val = adjMatrix.get(i, j);
                double dampedVal = (DF * val) + ((1 - DF) / size);
                adjMatrix.set(i, j, dampedVal);
            }
        }
    }

    /**
     * Method for returning the adjacency matrix for a given set of Nodes
     *
     * @param idMap the mapping from integers to Node IDs
     * @param nodes the set of Nodes from which the matrix should be made
     * @return the adjacency matrix in Matrix form from package la4j
     */
    private static Matrix getAdjMat(Map<Integer, Integer> idMap, Set<Node> nodes) {
        Matrix adjMatrix = new Basic2DMatrix(nodes.size(), nodes.size());
        for (Node node : nodes) {
            double cellEntry = 1 / (double) node.getOutDegree();
            Set<Edge> edges = node.getEdges();
            for (Edge e : edges) {
                adjMatrix.set(idMap.get(e.getSrc().getID()),
                        idMap.get(e.getTgt().getID()),
                        cellEntry);
            }
        }

        return adjMatrix;
    }
}
