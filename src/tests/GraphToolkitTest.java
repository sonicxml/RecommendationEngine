package tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*; 
import engine.*; 

public class GraphToolkitTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testBfs() throws Exception {
    	Graph g = DataReader.readSampleGraphData("RecommendationEngine/Data/"
    	        + "TestGraphs/bfs_acyclic.txt"); 
    	Node src = g.getNodeByID(1); 
    	Node tgt = g.getNodeByID(4); 
    	List<Node> nodes = GraphToolkit.bfs(g, src, tgt, false); 
    	List<Integer> path = new LinkedList<Integer>(); 
    	for (Node n : nodes) {
    		path.add(n.getID()); 
    	}
    	List<Integer> answer = new LinkedList<Integer>(); 
    	answer.add(1); 
    	answer.add(2); 
    	answer.add(4); 
    	assertEquals("see if output is shortest path", path, answer); 
    	
    }

    @Test
    public void testDfs() throws Exception {
    	Graph g = DataReader.readSampleGraphData("TestGraphs/dfs_acyclic.txt"); 
    	Node src = g.getNodeByID(1); 
    	Set<Node> nodes = g.getAllNodes(); 
    	Map<Node, List<Integer>> timestamps = GraphToolkit.dfs(g, src); 
    	for (Node n : nodes) {
    		int id = n.getID(); 
    		int startTime = timestamps.get(n).get(0); 
    		//int finishTime = timestamps.get(n).get(1); 
    		System.out.println(id + ": " + startTime + ", "); 
    	}
    	assertEquals("see if dfs does not break", timestamps.size(), 6); 
    }

    @Test
    public void testGetSCC() throws Exception {

    }

    @Test
    public void testDijkstra() throws Exception {

    }

    @Test
    public void testMaxFlow() throws Exception {

    }

    @Test
    public void testBtwCentrality() throws Exception {
        Graph g = DataReader.readSampleGraphData("RecommendationEngine/Data/"
                + "TestGraphs/btw_centrality_small.txt");
        double[] ans = new double[] {0, 15.5, 2.5, 10, 0, 0, 2.5, 0};
        Map<Node, Double> answer = GraphToolkit.btwCentrality(g);
        Iterator<Map.Entry<Node, Double>> iter = answer.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Node, Double> entry = iter.next();
            int id = entry.getKey().getID();
            double temp = entry.getValue();
            //assertEquals(ans[id - 1], temp, 0.001);
            System.out.println("ID: " + id);
            System.out.println("Centrality: " + temp);
        }
    }

    @Test
    public void testCommDetection() throws Exception {

    }

    @Test
    public void testPageRank() throws Exception {

    }
}