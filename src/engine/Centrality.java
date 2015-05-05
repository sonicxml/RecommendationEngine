package engine;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.functor.VectorAccumulator;
import org.la4j.vector.functor.VectorFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * This class provides implementations of the various centrality measures
 * of the Graph Toolkit, including betweeness cetnrality and PageRank.
 *
 */
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

    /**
     * Find the Eigenvector Centrality of a graph
     * using the scaled PageRank formula.
     *
     * <p>
     *     We default to a damping factor of 0.85, the value originally used by
     *     Google.
     *
     * <p>
     *     We implemented this using linear algebra, as opposed to the iterative
     *     or power methods. This is because we know that the convergence of
     *     those methods is the same as finding the principal eigenvector
     *     (in this case, the eigenvector corresponding to eigenvalue 1) of the
     *     dampened, normalized adjacency matrix.
     *
     * <p>
     *     We make this assumption because we also know that any
     *     column-stochastic matrix (a matrix where all entries are non-negative
     *     and where every column sums to 1) has an eigenvalue of 1. Our
     *     original adjacency matrix is column-stochastic and it remains
     *     column-stochastic after dampening as long as the damping factor is in
     *     [0, 1]. By the Perron-Frobenius theorem, we can also say that the
     *     eigenvalue of 1, which is the largest eigenvalue of a
     *     column-stochastic matrix, is a unique eigenvalue (i.e. it has
     *     algebraic multiplicity 1).
     *
     * <p>
     *     We chose the scaled PageRank because it allowed us to assert that
     *     the eigenvalue 1 has geometric multiplicty of 1. The principal
     *     eigenvector of the non-dampened but normalized adjacency matrix could
     *     have geometric multiplicty > 1 in two cases:
     *
     * <p><ol>
     *     <li>The rankings are not all unique. This could occur, for example,
     *     if the graph has multiple connected components.
     *     <li>There exists a sink (a node with out degree 0) in the graph. This
     *     would result in the adjacency matrix being column-substochastic
     *     (since not all columns would sum to 1), meaning that the matrix need
     *     not have an eigenvalue of 1 (although all eigenvalues would be <= 1).
     * <ol><p>
     *
     *
     * @param g the graph to run PageRank on
     * @return a map from node ID to rank
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
     * matrix. Because the dampened adjacency matrix is column-stochastic,
     * we know that the largest eigenvalue equals 1.
     *
     * @param adjMatrix an adjacency matrix for a graph
     * @return the principal eigenvector of adjMatrix
     */
    private static Vector getPrincipalEV(Matrix adjMatrix) {
        EigenDecompositor ed = new EigenDecompositor(adjMatrix);
        Matrix[] vd = ed.decompose();
        Matrix v = vd[0];
        Matrix d = vd[1];
        int maxIdx = 0;
        for (int i = 0; i < v.columns(); i++) {
            double colMax = d.foldColumn(i, new VectorAccumulator(){
                double accumulator = 0.0;
                public void update(int i, double value) {
                    accumulator += value;
                }
                public double accumulate() {
                    return accumulator;
                }
            });
            if (Math.abs(colMax - 1.0) < 1e-4) {
                maxIdx = i;
            }
        }

        return getNormalizedVector(v.getColumn(maxIdx));
    }

    /**
     * Method for getting the normalized Vector of a given Vector,
     * such that the sum of all elements is 1.
     *
     * @param v  the Vector to normalize
     * @return   the normalized Vector
     */
    private static Vector getNormalizedVector(Vector v) {
        final double sum = v.sum();
        v.update(new VectorFunction() {
            public double evaluate(int i, double value) {
                return value / sum;
            }
        });
        return v;
    }

    /**
     * Adjusts the values in the given matrix according to the formula for the
     * dampened PageRank algorithm.
     *
     * <p>
     *     This formula is:
     *
     * <p>
     *     M = DF * A + (1 - DF) * S
     *
     * <p>
     *     Where A is the (column-stochastic) normalized adjacency matrix and S
     *     is a matrix where each element = 1/n. As long as 0 <= DF <= 1,
     *     M is also column-stochastic.
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
     * Method for returning the adjacency matrix for a given set of Nodes.
     *
     * <p>
     *     This adjacency matrix, A, is normalized such that every column
     *     sums to 1. This is done by dividing each entry of a column by the
     *     column's corresponding node's out degree. This gives us a
     *     column-stochastic matrix, a property we rely heavily on in the rest
     *     of the PageRank computation.
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
                adjMatrix.set(idMap.get(e.getTgt().getID()),
                        idMap.get(e.getSrc().getID()),
                        cellEntry);
            }
        }

        return adjMatrix;
    }
}
