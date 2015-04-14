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
    
    public Set<Node> getAllNodes() {
        return new HashSet<>(nodes.values());
    }

    public Node getNodeByID(int id) {
        return nodes.get(id);
    }

    public int getSize() {
        return nodes.size();
    }
    
    public Graph copyOf(boolean reverse)
    {
    	Graph ans = new Graph();
    	Set<Node> nodes = this.getAllNodes();
    	
    	for (Node node : nodes) {
    		int id = node.getID();
    		Node temp = new Node(id);
    		ans.addNode(id, temp);
    	}
    	
    	for (Node node : nodes) {
    		Set<Edge> edges = node.getEdges();
    		for (Edge e : edges) {
    			int src = e.getSrc().getID();
    			int tgt = e.getTgt().getID();
    			if (reverse) {
    				Edge eNew = new Edge(ans.getNodeByID(tgt), 
    						ans.getNodeByID(src), e.getWeight());
    				ans.getNodeByID(tgt).addEdge(eNew);
    			} else {
    				Edge eNew = new Edge(ans.getNodeByID(src), 
    						ans.getNodeByID(tgt), e.getWeight());
    				ans.getNodeByID(src).addEdge(eNew);
    			}
    		}
    	}
    	
    	return ans;
    }
}