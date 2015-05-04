package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This class provides implementations of the various algorithms
 * dealing with flow in the Graph Toolkit, including Edmonds-Karp.
 *
 */
class Flow {
    private Flow() {
        // This class should not be instantiated
        throw new IllegalStateException();
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
    static int maxFlow(Graph g, Node src, Node tgt) {
        Graph newG = biDirectGraph(g);
        src = newG.getNodeByID(src.getID());
        tgt = newG.getNodeByID(tgt.getID());
        List<Node> path = Search.bfs(newG, src, tgt, true);

        while (!path.isEmpty()) {
            List<Edge> edgePath = getEdgesFromNodes(newG, path);
            Collections.reverse(path);
            List<Edge> revEdgePath = getEdgesFromNodes(newG, path);
            Collections.reverse(revEdgePath);
            List<Integer> residuals = new ArrayList<>();
            for (Edge e : edgePath) {
                residuals.add((int) e.getWeight() - e.getFlow());
            }

            int flow = Collections.min(residuals);

            for (int i = 0; i < path.size() - 1; i++) {
                Edge e = edgePath.get(i);
                Edge revE = revEdgePath.get(i);
                e.setFlow(e.getFlow() + flow);
                revE.setFlow(revE.getFlow() - flow);
            }

            path = Search.bfs(newG, src, tgt, true);
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
    private static List<Edge> getEdgesFromNodes(Graph g, List<Node> nodes) {
        List<Edge> edgePath = new LinkedList<>();

        for (int i = 0; i < nodes.size() - 1; i++) {
            Node src = g.getNodeByID(nodes.get(i).getID());
            Node tgt = g.getNodeByID(nodes.get(i + 1).getID());

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
}
