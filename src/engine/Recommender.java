package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

/**
 * This class provides various scoring methods, along with
 * a collaborative filtering recommendation algorithm.
 *
 */
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
    private Set<Node> users = new HashSet<>();
    private Set<Node> items = new HashSet<>();
    private int size = 0;

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

    /**
     * This method implements the collaborative filtering algorithm for
     * finding recommendations.  It accepts a TreeMap of scores that can
     * be generated using either the Pearson or Jaccard scores.
     * 
     * @param userID         the user for which to provide recommendations
     * @param scores         the scores to use for collaborative filtering
     * @param numUsers       the number of similar users to find
     * @param numRecommends  the number of recommendations (if any) to return
     * @return               the list of recommended nodes
     */
    public List<Integer> collabFilter(int userID, TreeMap<Double, List<Node>> scores,
            int numUsers, int numRecommends) {
        if (scores == null) {
            throw new IllegalArgumentException();
        }
        
        Node user = g.getNodeByID(userID);

        Set<Node> top = getTopMatches(scores, numUsers);
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
                        recommends.put(target, new Entry(target, weight));
                    }
                }
            }
        }

        LinkedList<Entry> sorted = new LinkedList<>(recommends.values());
        Collections.sort(sorted);

        // Get the Nodes corresponding to the top recommendations
        List<Integer> out = new LinkedList<>();
        int counter = 0;
        while (!sorted.isEmpty() && counter < numRecommends) {
            Node next = sorted.pollLast().getNode();
            // We don't want to include
            if (!user.getNeighbors().contains(next)) {
                // Highest recommendations will be at the end
                out.add(next.getID());
                counter++;
            }
        }

        return out;
    }

    /**
     * Provides a method for getting all of the Pearson Correlation
     * Coefficient scores for a given Node.
     * 
     * @param userID  the Node whose scores you wish to find
     * @return        a TreeMap of scores to a list of Nodes with that score
     */
    public TreeMap<Double, List<Node>> getPearsonScores(int userID) {
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
        
        return scores;
    }
    
    /**
     * Provides a method for getting all of the Jaccard Correlation
     * Coefficient scores for a given Node.
     * 
     * @param userID  the Node whose scores you wish to find
     * @return        a TreeMap of scores to a list of Nodes with that score
     */
    public TreeMap<Double, List<Node>> getJaccardScores(int userID) {
        Node user = g.getNodeByID(userID);
        TreeMap<Double, List<Node>> scores = new TreeMap<>();
        for (Node node : users) {
            if (!node.equals(user)) {
                double score = getJaccardCoeff(user, node);
                if (scores.containsKey(score)) {
                    scores.get(score).add(node);
                } else {
                    List<Node> nodes = new LinkedList<>();
                    nodes.add(node);
                    scores.put(score, nodes);
                }
            }
        }
        
        return scores;
    }
    
    /**
     * Provides a method for returning a given number of top matches
     * from a given map of scores.
     * 
     * @param map    the map of scores
     * @param limit  the maximum number of top scores to return
     * @return       the list of nodes corresponding to the top limit scores
     */
    private Set<Node> getTopMatches(TreeMap<Double, List<Node>> map, int limit) {
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
    
    /**
     * Method for calculating the Pearson Correlation Coefficient score
     * for two given Nodes.
     * 
     * @param n1  the first Node
     * @param n2  the second Node
     * @return    the score for the two nodes
     */
    private double getPearsonCoeff(Node n1, Node n2) {
        Set<Node> firstNeighbors = n1.getNeighbors();
        Set<Node> sndNeighbors = n2. getNeighbors();
        Set<Edge> firstEdges = n1.getEdges();
        Set<Edge> sndEdges = n2.getEdges();
        Map<Node, Double> firstWeights = new HashMap<>();
        Map<Node, Double> sndWeights = new HashMap<>();
        
        firstNeighbors.retainAll(sndNeighbors);
        
        if (firstNeighbors.size() == 0) return 0.0;
        
        for (Edge e : firstEdges) {
            firstWeights.put(e.getTgt(), e.getWeight());
        }
        for (Edge e : sndEdges) {
            sndWeights.put(e.getTgt(), e.getWeight());
        }
        
        int size = firstNeighbors.size();
        double firstSum = 0.0;
        double sndSum = 0.0;
        double firstSqSum = 0.0;
        double sndSqSum = 0.0;
        double prodSum = 0.0;
        
        for (Node n : firstNeighbors) {
            double first = firstWeights.get(n);
            double snd = sndWeights.get(n);
            
            firstSum = firstSum + first;
            sndSum = sndSum + snd;
            firstSqSum = firstSqSum + first * first;
            sndSqSum = sndSqSum + snd * snd;
            prodSum = prodSum + first * snd;
        }
        
        double numerator = prodSum - (firstSum * sndSum / size);
        double denom = Math.sqrt((firstSqSum - firstSum * firstSum / size) * 
                (sndSqSum - sndSum * sndSum / size));
        
        if (denom == 0.0) return 0.0;
        
        return numerator / denom;
    }

    /**
     * Calculate Jaccard coefficient for n1 and n2.
     * If A = neighbors(n1) and B = neighbors(n2)
     * coeff = (A ∩ B) / (A ∪ B)
     *
     * @param n1 the first node
     * @param n2 the second node
     * @return the Jaccard similarity coefficient between n1 and n2
     */
    private double getJaccardCoeff(Node n1, Node n2) {
        Set<Node> n1Neighbors = n1.getNeighbors();
        Set<Node> n2Neighbors = n2.getNeighbors();
        Set<Node> temp1 = new HashSet<>(n1Neighbors);
        Set<Node> temp2 = new HashSet<>(n2Neighbors);

        // n1Neighbors now contains all nodes they have in common
        n1Neighbors.retainAll(temp2);

        // n2Neighbors now contains all nodes they don't have in common
        n2Neighbors.removeAll(temp1);

        double coeff = (double) (n1Neighbors.size() - n2Neighbors.size()) /
                (double) (n1Neighbors.size() + n2Neighbors.size());
        return coeff;
    }

    public String getMovieLensNames(int user, List<Integer> items)
            throws DataFormatException {
        String result = "";
        String users = "data/ml-100k/u.user";
        String movies = "data/ml-100k/u.item";
        Pattern p = Pattern.compile("(^" + Integer.toString(user) + ".*)");
        Matcher m = p.matcher("");
        Set<Integer> setOfMovies = new HashSet<>(items);
        try {
            Scanner s = new Scanner(new File(users));
            while (s.hasNextLine()) {
                String line = s.nextLine();
                m.reset(line); //reset the input
                if (m.find()) {
                    System.out.println("For user " + m.group(1)
                            + " our best recommendations are:");
                    result += "For user " + m.group(1) + " our "
                            + "recommendations are:\n";
                    break;
                }
            }

            Scanner s2 = new Scanner(new File(movies));
            while (s2.hasNextLine()) {
                String line = s2.nextLine();
                String[] tokens = line.split("\\|");
                if (tokens.length < 2) {
                    throw new DataFormatException();
                }
                int id = Integer.parseInt(tokens[0]);
                if (setOfMovies.contains(id + 943)) {
                    System.out.println("Movie: " + tokens[0] + "|"
                            + tokens[1] + "|" + tokens[2]);
                    result += "" + tokens[0] + "|" + tokens[1] +
                            "|" + tokens[2] + "\n";
                    setOfMovies.remove(id);
                }
                if (setOfMovies.isEmpty()) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
