import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

class Search {
    private Search() {
        // This class should not be instantiated
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
    static List<Node> bfs(Graph g, Node src, Node tgt, boolean flow) {
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

    /* An implementation of the DFS algorithm that returns the
    finish times of each vertex in the graph. */
    static Map<Node, List<Integer>> dfs(Graph g, Node src) {
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

    /* Function returns a topological sort on given graph */
    static List<Node> topSort(Graph g) {
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

    static Map<Node, Integer> singleSourceShortestPath(Graph g, Node src) {
        // TODO: Implement
        // Also return something
        // Implement Dijkstra's algorithm for SSSP
        return null;
    }

    /**
     * Check if n has in degree
     *
     * @param n     the node to check
     * @param graph the graph containing the node
     * @return true if in degree == 0, false otherwise
     */
    private static boolean checkInDegree(Node n, Set<Node> graph) {
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
    private static Node getUnvisited(Set<Node> graph, Set<Node> visited) {
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
    private static Map<Node, List<Integer>> stamp(Map<Node,
            List<Integer>> map, Node n, int i) {
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

    /*function returns a boolean checking if given graph contains a cycle.
    Returns true if there exists a cycle. */
    private static boolean detectCycle(Graph g) {
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
}
