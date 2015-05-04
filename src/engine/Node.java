package engine;

import java.util.HashSet;
import java.util.Set;

/**
 * This class provides an implementation of a generic Node object
 * with associated methods.
 *
 */
 class Node {
    private int id;
    private Set<Edge> edges = new HashSet<>();

   public Node(int id) {
        this.id = id;
    }

    void addEdge(Edge e) {
        if (e == null) {
            throw new IllegalArgumentException("null edge");
        }

        edges.add(e);
    }

    Set<Edge> getEdges() {
        return new HashSet<>(edges);
    }

    public int getID() {
        return id;
    }

    public int getOutDegree() {
        return edges.size();
    }

    public Set<Node> getNeighbors() {
        Set<Node> neighbors = new HashSet<>();
        for (Edge e : edges) {
            neighbors.add(e.getTgt());
        }

        return neighbors;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                '}';
    }
}
