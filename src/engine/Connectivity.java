package engine;

import java.util.Set;
import java.util.HashSet;
import java.util.Stack;

/**
 * This class provides methods for determining the connectivity
 * of a Graph, including Kosaraju's algorithm.
 *
 */
class Connectivity {
    private Connectivity() {
        // This class should not be instantiated
        throw new IllegalStateException();
    }

    /**
     * Method for returning the strongly connected components of
     * a given Graph, using Kosaraju's algorithm.
     * 
     * @param g  the Graph on which to run Kosaraju's
     * @return   the Set of connected components
     */
    static Set<Set<Integer>> getSCC(Graph g) {
        Stack<Node> s = dfs(g);
        Set<Set<Integer>> components = new HashSet<>();
        Graph reverse = g.copyOf(true);
        while (!s.isEmpty()) {
            Node src = s.pop();
            System.out.println(s);
            Set<Node> connected = Search.dfsTree(reverse, src).keySet();
            System.out.println("Path:" + connected);
            s.removeAll(connected);
            System.out.println(s);
            System.out.println("");
            Set<Integer> component = new HashSet<>();
            for (Node node : connected) {
                component.add(node.getID());
            }
            components.add(component);
        }
        return components;
    }
    
    /**
     * Method for returning all of the Nodes in a given graph in
     * a Stack form representing the reverse of the order in
     * which the Nodes finish, using dfs.
     * 
     * @param g  the Graph on which to run dfs
     * @return   the Stack of Nodes
     */
    private static Stack<Node> dfs(Graph g) {
        Stack<Node> s = new Stack<>();
        Set<Node> visited = new HashSet<>();
        
        for (Node node : g.getAllNodes()) {
            dfsVisit(node, s, visited);
        }
        
        return s;
    }
    
    /**
     * Method for recursively visiting a given Node and all of its children.
     * 
     * @param src      the source Node
     * @param s        the stack of Nodes that have finished
     * @param visited  the set of visited Nodes
     */
    private static void dfsVisit(Node src, Stack<Node> s, Set<Node> visited) {
        if (!visited.contains(src)) {
            visited.add(src);
            for (Node node : src.getNeighbors()) {
                if (!visited.contains(node)) {
                    dfsVisit(node, s, visited);
                }
            }
            s.push(src);
        }
    }
}
