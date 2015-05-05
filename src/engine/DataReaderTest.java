package engine;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataReaderTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testReadMovieLensData() throws Exception {
        Graph g = DataReader.readMovieLensData();

        assertEquals("Size of graph", 2625, g.getSize());
    }

    @Test
    public void testReadSampleGraphData() throws Exception {
        String filename = "data/TestGraphs/testGraph1.txt";

        Graph g = DataReader.readGraphData(filename);
        Graph g2 = new Graph();
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        n1.addEdge(new Edge(n1, n2, 1));
        n1.addEdge(new Edge(n2, n4, 1));
        n2.addEdge(new Edge(n2, n3, 1));
        n3.addEdge(new Edge(n3, n1, 1));
        n4.addEdge(new Edge(n4, n5, 1));
        g2.addNode(n1.getID(), n1);
        g2.addNode(n2.getID(), n2);
        g2.addNode(n3.getID(), n3);
        g2.addNode(n4.getID(), n4);
        g2.addNode(n5.getID(), n5);

        assertEquals("Equal size", g.getSize(), g2.getSize());
        Set<Node> nodes1 = g.getAllNodes();
        for (Node node : nodes1) {
            int ID = node.getID();
            Node node2 = g2.getNodeByID(ID);
            assertFalse("Node2 null", node2 == null);
            assertTrue("Same degree", node.getOutDegree() ==
                    node2.getOutDegree());
        }
    }
}