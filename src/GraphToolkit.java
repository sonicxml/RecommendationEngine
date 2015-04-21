import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.util.*;


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

                if ((flow && e.getWeight() - e.getFlow() > 0) || !flow) {
                    if (!explored.contains(n)) {
                        explored.add(n);
                        q.add(n);
                        parents.put(n, user);
                    }
                }
            }
        }

        // If there is no path
        return new LinkedList<>();
    }

    //returns true if Node n has no In Degree
    public static boolean checkInDegree(Node n, Set<Node> graph) {
        for (Node k : graph) {
            if (k.getNeighbors().contains(n)) {
                return false;
            }
        }
        return true;
    }

    /*	Given a set of nodes in a graph and a set of visited nodes, function returns
    an unvisited node of zero In degree. If there are no nodes of zero
    in degree in remaining connected component, returns arbitrary unvisited node */
    public static Node getUnvisited(Set<Node> graph,
                                    Set<Node> visited) {
        for (Node n : graph) {
            if (!visited.contains(n) && checkInDegree(n, graph)) {
                return n;
            }
        }
        for (Node n : graph) {
            if (!visited.contains(n)) {
                return n;
            }
        }
        return null;
    }

    /*Given a map, a node, and an int, function adds int to the set of
    integers mapped by the given node. Helper function for dfs and
    cycle detection. */
    public static Map<Node, List<Integer>>
    stamp(Map<Node, List<Integer>> map, Node n, int i) {
        if (map.containsKey(n)) {
            List<Integer> set = map.get(n);
            if (set.size() > 2) {
                return map;
            } else {
                set.add(i);
                map.put(n, set);
                return map;
            }
        } else {
            List<Integer> set = new LinkedList<Integer>();
            map.put(n, set);
            return map;
        }
    }

    /* An implementation of the DFS algorithm that returns the
    finish times of each vertex in the graph. */
    public static Map<Node, List<Integer>> dfs(Graph g, Node src) {
        if (g == null || src == null) {
            throw new IllegalArgumentException();
        }
        Set<Node> nodes = g.getAllNodes();
        //A mapping of a Node to its corresponding finish time
        Map<Node, List<Integer>> timeStamps =
                new HashMap<Node, List<Integer>>();

        //Set keeping track gray nodes
        Set<Node> visited = new HashSet<Node>();

        //stack to perform depth first search
        Stack<Node> stack = new Stack<Node>();
        stack.push(src);

        //counter to record finish times
        int counter = 1;

        while (!stack.empty()) {
            Node a = stack.peek();
            //If node has not been visited yet
            if (!visited.contains(a)) {
                visited.add(a);
                stamp(timeStamps, a, counter);
                Set<Node> neighbors = a.getNeighbors();
                for (Node n : neighbors) {
                    if (!visited.contains(n))
                        stack.add(n);
                }
            }

			/*If node has already been visited then stamp finish time and
            pop from stack. If stack is empty then search the graph for
			unvisited nodes. */
            else {
                stamp(timeStamps, a, counter);
                stack.pop();
                if (stack.isEmpty() &&
                        getUnvisited(nodes, visited) != null) {
                    stack.push(getUnvisited(nodes, visited));
                }
            }

            counter++;
        }

        return timeStamps;
    }

    /*function returns a boolean checking if given graph contains a cycle.
    Returns true if there exists a cycle. */
    public static boolean detectCycle(Graph g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        Set<Node> nodes = g.getAllNodes();
        Node src = g.getNodeByID(0);

        //Set keeping track gray nodes
        Set<Node> visited = new HashSet<Node>();

        //stack to perform depth first search
        Stack<Node> stack = new Stack<Node>();
        stack.add(src);

        while (!stack.empty()) {
            Node a = stack.peek();

            if (!visited.contains(a)) {
                visited.add(a);
                Set<Node> neighbors = a.getNeighbors();
                for (Node n : neighbors) {
                    if (!visited.contains(n))
                        stack.add(n);
                }
            }
            //if node is in the stack more than once,
            //then a cycle exists in given graph
            else {
                int count = 0;
                for (Node k : stack) {
                    if (k.equals(a)) {
                        count++;
                    }
                }
                if (count > 2) {
                    return true;
                }
                stack.pop();
                if (stack.isEmpty() && getUnvisited(nodes, visited) != null) {
                    stack.push(getUnvisited(nodes, visited));
                }
            }
        }

        return false;
    }


    /* Function returns a topological sort on given graph */
    public static List<Node> topSort(Graph g) {
        if (!detectCycle(g)) {
            TreeMap<Node, Integer> map = new TreeMap<Node, Integer>();
            Set<Node> nodes = g.getAllNodes();
            Set<Node> visited = new HashSet<Node>();
            Node src = getUnvisited(nodes, visited);
            Map<Node, List<Integer>> timeStamps = dfs(g, src);
            for (Node n : nodes) {
                int finishTime = timeStamps.get(n).get(1);
                map.put(n, finishTime);
            }
            Set<Node> keys = map.keySet();
            Stack<Node> sort = new Stack<Node>();
            for (Node n : keys) {
                sort.push(n);
            }
            return sort;
        } else {
            throw new IllegalArgumentException();
        }


    }

    public static void getSCC(Graph g) {
        // TODO: Implement
        // Also return something
        // Implement Kosaraju's Algorithm for finding SCCs
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
        Graph newG = biDirectGraph(g);
        List<Node> path = bfs(newG, src, tgt, true);

        while (!path.isEmpty()) {
            List<Edge> edgePath = getEdgesFromNodes(path);
            Collections.reverse(path);
            List<Edge> revEdgePath = getEdgesFromNodes(path);
            Collections.reverse(revEdgePath);
            List<Integer> residuals = new ArrayList<>();
            for (Edge e : edgePath) {
                residuals.add((int) e.getWeight() - e.getFlow());
            }

            int flow = Collections.min(residuals);

            for (int i = 0; i < path.size(); i++) {
                Edge e = edgePath.get(i);
                Edge revE = revEdgePath.get(i);
                e.setFlow(e.getFlow() + flow);
                revE.setFlow(revE.getFlow() - flow);
            }

            path = bfs(newG, src, tgt, true);
        }

        int maxFlow = 0;
        for (Edge e : src.getEdges()) {
            maxFlow += e.getFlow();
        }

        return maxFlow;
    }

    /**
     * Takes a list of nodes that forms a path, and creates a corresponding list
     * of edges for that same path.
     *
     * @param nodes the path of nodes
     * @return the path of edges
     */
    private static List<Edge> getEdgesFromNodes(List<Node> nodes) {
        List<Edge> edgePath = new LinkedList<>();

        for (int i = 0; i < nodes.size() - 1; i++) {
            Node src = nodes.get(i);
            Node tgt = nodes.get(i + 1);

            Set<Edge> edges = src.getEdges();

            for (Edge e : edges) {
                // Let's just check for equality by comparing IDs
                if (tgt.getID() == e.getTgt().getID()) {
                    edgePath.add(e);
                    break;
                }
            }
        }

        return edgePath;
    }

    /**
     * Bidirects a graph (for every directed edge, adds the backwards edge).
     *
     * @param g the graph
     * @return a bidirected version of g
     */
    private static Graph biDirectGraph(Graph g) {
        Graph newG = g.copyOf(true, 0);
        mergeGraphs(newG, g);
        return newG;
    }

    /**
     * Merges two graphs, g1 and g2, into g1
     *
     * @param g1 the first graph
     * @param g2 the second graph
     */
    private static void mergeGraphs(Graph g1, Graph g2) {
        // Add all nodes and edges from g2 into g1
        for (Node n : g2.getAllNodes()) {
            Set<Edge> edges = n.getEdges();
            for (Edge e : edges) {
                int srcID = e.getSrc().getID();
                int tgtID = e.getTgt().getID();

                g1.addEdge(srcID, tgtID, e.getWeight());
            }
        }
    }

    /**
     * Calculates the betweeness centrality for each node using Brandes'
     * algorithm.
     *
     * @param g the Graph whose values should be calculated
     * @return a map containing the betweeness centrality values
     */
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    public static Map<Node, Double> btwCentrality(Graph g) {
        // TODO: Implement
        // Also return something
        // Implement Brandes' algorithm for betweenness centrality
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
                s.add(v);
                Set<Node> neighbors = v.getNeighbors();
                for (Node neighbor : neighbors) {
                    int tempIndex = nodeMap.get(neighbor);
                    if (distances[tempIndex] < 0) {
                        q.add(neighbor);
                        distances[tempIndex] = distances[nodeMap.get(v)] + 1;
                    } else if (distances[tempIndex] == distances[nodeMap.get(v)] + 1) {
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

    public static void commDetection(Graph g) {
        // TODO: Implement
        // Also return something
        // Implement the Girvan-Newman algorithm for community detection
    }

    public static Map<Integer, Double> pageRank(Graph g) {
        /**
         * Find the Eigenvector Centrality of a graph
         * using the dampened PageRank formula
         */

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
                revIDMap.put(idMap.size(), node.getID());
            }
        }

        // Get the adjancency matrix
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

    public static void shortestPath(Graph g, Node src) {
        // TODO: Implement
        // Also return something
        // Implement Dijkstra's algorithm for SSSP
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
        int size = adjMatrix.rows() * adjMatrix.columns();
        for (int i = 0; i < adjMatrix.rows(); i++) {
            for (int j = 0; j < adjMatrix.columns(); j++) {
                Double val = adjMatrix.get(i, j);
                Double dampedVal = (DF * val) + ((1 - DF) / size);
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
        Matrix adjMatrix = new Basic2DMatrix();
        for (Node node : nodes) {
            double cellEntry = (double) (1 / node.getOutDegree());
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
