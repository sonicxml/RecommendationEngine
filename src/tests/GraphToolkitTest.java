package tests;

import engine.DataReader;
import engine.Graph;
import engine.GraphToolkit;
import engine.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GraphToolkitTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testBfs() throws Exception {
    	Graph g = DataReader.readSampleGraphData("data/"
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
    	Graph g = DataReader.readSampleGraphData(
                "data/TestGraphs/dfs_acyclic.txt");
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
        Graph g = DataReader.readSampleGraphData("RecommendationEngine/data/"
                + "TestGraphs/btw_centrality_small.txt");
        Set<Set<Integer>> answer = new HashSet<>();
        Set<Integer> scc1;
    }

    @Test
    public void testDijkstra() throws Exception {

    }

    @Test
    public void testMaxFlow() throws Exception {

    }

    @Test
    public void testBtwCentrality() throws Exception {
        Graph g = DataReader.readSampleGraphData("data/"
                + "TestGraphs/btw_centrality_small.txt");
        double[] ans = new double[] {0, 15.5, 2.5, 10, 0, 0, 2.5, 0.5};
        Map<Node, Double> answer = GraphToolkit.btwCentrality(g);
        Iterator<Map.Entry<Node, Double>> iter = answer.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Node, Double> entry = iter.next();
            int id = entry.getKey().getID();
            double temp = entry.getValue();
            //divide by two for undirected
            assertEquals(ans[id - 1], temp / 2, 0.001);
        }
    }

    @Test
    public void testCommDetection() throws Exception {

    }

    @Test
    public void testPageRank() throws Exception {
        Graph g = DataReader.readSampleGraphData(
                "data/TestGraphs/pageRank_full.txt");
        //double[] ans = new double[] {1.4901, 0.7833, 1.5766, 0.1500};
        double[] ans = new double[] {1, 1, 1};
        Map<Integer, Double> answer = GraphToolkit.pageRank(g);
        Iterator<Map.Entry<Integer, Double>> iter = answer.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Double> entry = iter.next();
            int id = entry.getKey();
            double temp = entry.getValue();
            System.out.println(temp);
            System.out.println(ans[id - 1]);
            //assertEquals(ans[id - 1], temp, 0.02);
        }
    }

    @Test
    public void testPageRank4NodeSmall() throws Exception {
        Graph g = DataReader.readSampleGraphData(
                "data/TestGraphs/pageRank_4node.txt");
        Map<Integer, Double> ans = new HashMap<>();
        ans.put(1, .387);
        ans.put(2, .129);
        ans.put(3, .290);
        ans.put(4, .194);
        Map<Integer, Double> out = GraphToolkit.pageRank(g);
        System.out.println(out);
        for (int i = 1; i <= 4; i++) {
            assertEquals(ans.get(i), out.get(i), 0.0001);
        }
    }
}