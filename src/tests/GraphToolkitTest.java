package tests;

import java.util.*; 
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;
import engine.*; 

public class GraphToolkitTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testBfs() throws Exception {
    	Graph g = DataReader.readSampleGraphData("TestGraphs/bfs_acyclic.txt"); 
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

    }

    @Test
    public void testCommDetection() throws Exception {

    }

    @Test
    public void testPageRank() throws Exception {

    }
}