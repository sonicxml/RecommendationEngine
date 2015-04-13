import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    // Map from ID to Node
    private Map<Integer, Node> nodes = new HashMap<>();

    public Graph() {
        // Howdy mate
    	// Ryan, ESP is real.
    }

    public void addNode(int id, Node n) {
        nodes.put(id, n);
    }

    public Node getNode(int id) {
        return nodes.get(id);
    }

    public Set<Node> getAllNodes() {
        return new HashSet<>(nodes.values());
    }

    public Node getNodeByID(int id) {
        return nodes.get(id);
    }
}