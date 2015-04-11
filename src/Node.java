import java.util.HashSet;
import java.util.Set;

public class Node {
    private int id;
    private Set<Edge> adjacent = new HashSet<>();

    public Node(int id) {
        this.id = id;
    }

    public void addEdge(Edge e) {
        if (e == null) {
            throw new IllegalArgumentException("null edge");
        }

        adjacent.add(e);
    }

    public Set<Edge> getAdjacent() {
        return new HashSet<>(adjacent);
    }
}
