package engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Recommender {
    private class Entry implements Comparable<Entry> {
        private Node node;
        private double weight;

        public Entry(Node node, double weight) {
            this.node = node;
            this.weight = weight;
        }

        public Node getNode() {
            return node;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        @Override
        public int compareTo(Entry other) {
            if (this.weight < other.getWeight()) {
                return -1;
            } else if (this.weight > other.getWeight()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private Graph g;
    private Set<Node> users;
    private Set<Node> items;
    private int size;

    /**
     * Creates a Recommender object from a Graph.
     * INVARIANT: The graph must be directed and bipartite.
     *
     * @param og the Graph from which to construct the Recommender
     */
    public Recommender(Graph og) {
        this.g = og;
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

    public List<Node> collabFilter(int userID) {
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
        Map<Node, Entry> recommends = new HashMap<>();

        // Get the cumulative sum of edge weights from each of the top most
        // similar users to items not adjacent to the given user.
        for (Node node : top) {
            Set<Edge> edges = node.getEdges();
            for (Edge e : edges) {
                Node target = e.getTgt();
                double weight = e.getWeight();
                if (!neighbors.contains(target)) {
                    if (recommends.containsKey(target)) {
                        double currWeight = recommends.get(target).getWeight();
                        recommends.get(target).setWeight(currWeight + weight);
                    } else {
                        recommends.put(node, new Entry(node, weight));
                    }
                }
            }
        }

        LinkedList<Entry> sorted = new LinkedList<>(recommends.values());
        Collections.sort(sorted);

        // Get the Nodes corresponding to the top 5 recommendations
        List<Node> out = new LinkedList<>();
        int counter = 0;
        while (!sorted.isEmpty() && counter < 5) {
            // Highest recommendations will be at the end
            out.add(sorted.pollLast().getNode());
            counter++;
        }

        return out;
    }

    private Set<Node> getTopMatches(TreeMap<Double, List<Node>> map,
                                    int limit) {
        if (limit > size) {
            throw new IllegalArgumentException();
        }

        Set<Node> topMatches = new HashSet<>();
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
