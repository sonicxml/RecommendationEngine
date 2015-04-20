import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Recommender {
	private Graph g;
	private Set<Node> users;
	private Set<Node> items;
	private int size;
	
	/**
	 * Creates a Recommender object from a Graph.
	 * INVARIANT: The graph must be directed and bipartite.
	 * 
	 * @param g  the Graph from which to construct the Recommender
	 */
	public Recommender(Graph g) {
		this.g = g;
		Set<Node> nodes = g.getAllNodes();
		this.size = nodes.size();
		for (Node node : nodes) {
			if (node.getNeighbors().size() > 0) {
				users.add(node);
			} else {
				items.add(node);
			}
		}
	}
	
	public void collabFilter(int userID) {
		Node user = g.getNodeByID(userID);
		TreeMap<Double, List<Node>> scores = new TreeMap<>();
		for (Node node : users) {
			if (!node.equals(user)) {
				double score = getPearsonCoeff(user, node);
				if (scores.containsKey(score)) {
					scores.get(score).add(node);
				} else {
					List<Node> nodes = new LinkedList<>();
					nodes.add(node);
					scores.put(score, nodes);
				}
			}
		}
		
		Set<Node> top = getTopMatches(scores, 10);
		Set<Node> neighbors = user.getNeighbors();
		Set<Edge> recommends = new TreeSet<>();
		
		for (Node node : top) {
			Set<Edge> edges = node.getEdges();
			for (Edge e : edges) {
				Node target = e.getTgt();
				if (!neighbors.contains(target)) {
					//do some dark magic
				}
			}
		}
		
		
	}
	
	private Set<Node> getTopMatches(TreeMap<Double, List<Node>> map,
			int limit) {
		if (limit > size) {
			throw new IllegalArgumentException();
		}
		
		Set<Node> topMatches = new HashSet<Node>();
		while (topMatches.size() < limit) {
			Map.Entry<Double, List<Node>> entry = map.pollLastEntry();
			topMatches.addAll(entry.getValue());
		}
		return topMatches;
	}
	
	private double getPearsonCoeff(Node n1, Node n2) {
		return 0.0;
	}
}
