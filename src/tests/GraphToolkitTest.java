package tests;

import engine.DataReader;
import engine.Graph;
import engine.GraphToolkit;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphToolkitTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testBfsAcyclic() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/bfs_acyclic.txt");
        List<Integer> nodes = GraphToolkit.bfs(g, 1, 4, false);
        List<Integer> answer = new LinkedList<>();
        answer.add(1);
        answer.add(2);
        answer.add(4);
        assertEquals("see if output is shortest path", nodes, answer);
    }

    @Test
    public void testBfsCyclic() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/bfs_cyclic.txt");
        List<Integer> nodes = GraphToolkit.bfs(g, 3, 1, false);
        List<Integer> answer = new LinkedList<>();
        answer.add(3);
        answer.add(5);
        answer.add(1);
        assertEquals("see if output is shortest path", nodes, answer);
    }

    @Test
    public void testBfsSingleNode() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/SingleNode.txt");
        List<Integer> path = GraphToolkit.bfs(g, 1, 1, false);
        List<Integer> answer = new LinkedList<>();
        answer.add(1);
        assertEquals("see if output is shortest path", path, answer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBfsNull() throws Exception {
        Graph g = null;
        GraphToolkit.bfs(g, 1, 2, false);
    }

    @Test
    public void testDfsForestAcyclic() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/dfs_acyclic.txt");
        Map<Integer, List<Integer>> timestamps = GraphToolkit.dfsForest(g, 1);
        assertEquals("see if dfs does not break", timestamps.size(), 6);
    }

    @Test
    public void testDfsForestCyclic() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/dfs_cyclic.txt");
        Map<Integer, List<Integer>> timestamps = GraphToolkit.dfsForest(g, 1);
        assertEquals("see if dfs does not break", timestamps.size(), 6);
    }

    @Test
    public void testDfsForestSingleNode() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/SingleNode.txt");
        Map<Integer, List<Integer>> timestamps = GraphToolkit.dfsForest(g, 1);
        Map<Integer, List<Integer>> answers = new HashMap<>();
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        answers.put(1, list);
        assertEquals(timestamps, answers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDfsForestNull() throws Exception {
        Graph g = null;
        GraphToolkit.dfsForest(g, 1);
    }

    @Test
    public void dfsTreeWithCC() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/simpleDfs.txt");
        Map<Integer, List<Integer>> timestamps = GraphToolkit.dfsTree(g, 1);
        Map<Integer, List<Integer>> answers = new HashMap<>();
        List<Integer> list1 = new LinkedList<>();
        List<Integer> list2 = new LinkedList<>();
        List<Integer> list3 = new LinkedList<>();
        list1.add(1);
        list1.add(6);
        list2.add(2);
        list2.add(5);
        list3.add(3);
        list3.add(4);
        answers.put(1, list1);
        answers.put(2, list2);
        answers.put(3, list3);
        assertEquals(timestamps, answers);
    }

    @Test
    public void dfsTreeSingleNode() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/SingleNode.txt");
        Map<Integer, List<Integer>> timestamps = GraphToolkit.dfsTree(g, 1);
        Map<Integer, List<Integer>> answers = new HashMap<>();
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        answers.put(1, list);
        assertEquals(timestamps, answers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dfsTreeNull() throws Exception {
        Graph g = null;
        GraphToolkit.dfsTree(g, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTopSortCyclic() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/dfs_cyclic.txt");
        GraphToolkit.topSort(g);
    }

    @Test
    public void testTopSortAyclic() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/dfs_acyclic.txt");
        List<Integer> sort = GraphToolkit.topSort(g);
        List<Integer> trueSort = new LinkedList<>();
        trueSort.add(1);
        trueSort.add(2);
        trueSort.add(4);
        trueSort.add(3);
        trueSort.add(5);
        trueSort.add(6);
        List<Integer> trueSort2 = new LinkedList<>();
        trueSort2.add(1);
        trueSort2.add(2);
        trueSort2.add(3);
        trueSort2.add(4);
        trueSort2.add(5);
        trueSort2.add(6);
        assertTrue("test for equality of sorts", sort.equals(trueSort)
                || sort.equals(trueSort2));
    }

    @Test
    public void testTopSortSingleNode() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/SingleNode.txt");
        List<Integer> sort = GraphToolkit.topSort(g);
        List<Integer> trueSort = new LinkedList<>();
        trueSort.add(1);
        assertEquals(sort, trueSort);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTopSortNull() throws Exception {
        Graph g = null;
        GraphToolkit.topSort(g);
    }

    @Test
    public void testGetSCC() throws Exception {
        Graph g = DataReader.readGraphData("data/"
                + "TestGraphs/kosaraju_self_loop.txt");
        Set<Set<Integer>> expected = new HashSet<>();
        Set<Integer> scc1 = new HashSet<>();
        scc1.add(1);
        scc1.add(2);
        scc1.add(5);
        Set<Integer> scc2 = new HashSet<>();
        scc2.add(3);
        scc2.add(4);
        Set<Integer> scc3 = new HashSet<>();
        scc3.add(6);
        scc3.add(7);
        Set<Integer> scc4 = new HashSet<>();
        scc4.add(8);
        expected.add(scc1);
        expected.add(scc2);
        expected.add(scc3);
        expected.add(scc4);
        Set<Set<Integer>> result = GraphToolkit.getSCC(g);
        for (Set<Integer> set : result) {
            assertTrue(expected.contains(set));
            expected.remove(set);
        }
        assertEquals("Sets size equal", 0, expected.size());
    }

    @Test
    public void testGetSCCSmall() throws Exception {
        Graph g = DataReader.readGraphData("data/"
                + "TestGraphs/kosaraju_small.txt");
        Set<Set<Integer>> expected = new HashSet<>();
        Set<Integer> scc1 = new HashSet<>();
        scc1.add(1);
        scc1.add(2);
        scc1.add(3);
        Set<Integer> scc2 = new HashSet<>();
        scc2.add(4);
        Set<Integer> scc3 = new HashSet<>();
        scc3.add(5);
        expected.add(scc1);
        expected.add(scc2);
        expected.add(scc3);
        Set<Set<Integer>> result = GraphToolkit.getSCC(g);
        for (Set<Integer> set : result) {
            assertTrue(expected.contains(set));
            expected.remove(set);
        }
        assertEquals("Sets size equal", 0, expected.size());
    }

    @Test
    public void testGetSCCMedium() throws Exception {
        Graph g = DataReader.readGraphData("data/"
                + "TestGraphs/kosaraju_medium.txt");
        Set<Set<Integer>> expected = new HashSet<>();
        Set<Integer> scc1 = new HashSet<>();
        scc1.add(1);
        scc1.add(2);
        scc1.add(5);
        scc1.add(6);
        Set<Integer> scc2 = new HashSet<>();
        scc2.add(3);
        scc2.add(7);
        Set<Integer> scc3 = new HashSet<>();
        scc3.add(4);
        Set<Integer> scc4 = new HashSet<>();
        scc4.add(8);
        expected.add(scc1);
        expected.add(scc2);
        expected.add(scc3);
        expected.add(scc4);
        Set<Set<Integer>> result = GraphToolkit.getSCC(g);
        for (Set<Integer> set : result) {
            assertTrue(expected.contains(set));
            expected.remove(set);
        }
        assertEquals("Sets size equal", 0, expected.size());
    }

    @Test
    public void testBtwCentrality() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/btw_centrality_small.txt");
        double[] ans = new double[]{0, 15.5, 2.5, 10, 0, 0, 2.5, 0.5};
        Map<Integer, Double> answer = GraphToolkit.getBetweennessCentrality(g);
        Iterator<Map.Entry<Integer, Double>> iter = answer.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Double> entry = iter.next();
            int id = entry.getKey();
            double temp = entry.getValue();
            //divide by two for undirected
            assertEquals(ans[id - 1], temp / 2, 0.001);
        }
    }

    @Test
    public void testMaxFlowSimple2Node() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/maxFlow_2Node.txt");
        assertEquals(1, GraphToolkit.getMaxFlow(g, 1, 2));
    }

    @Test
    public void testMaxFlow4Node() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/maxFlow_4node.txt");
        assertEquals(15, GraphToolkit.getMaxFlow(g, 1, 4));
    }

    @Test
    public void testMaxFlow6Node() throws Exception {
        Graph g = DataReader.readGraphData("data/TestGraphs/maxFlowTest.txt");
        assertEquals(23, GraphToolkit.getMaxFlow(g, 0, 5));
    }

    @Test
    public void testPageRank() throws Exception {
        Graph g = DataReader.readGraphData(
                "data/TestGraphs/pageRank_full.txt");
        double[] ans = new double[]{0.333, 0.333, 0.333};
        Map<Integer, Double> answer = GraphToolkit.getPageRank(g);
        Iterator<Map.Entry<Integer, Double>> iter = answer.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Double> entry = iter.next();
            int id = entry.getKey();
            double temp = entry.getValue();
            assertEquals(ans[id - 1], temp, 0.001);
        }
    }

    @Test
    public void testPageRank4NodeSmall() throws Exception {
        Graph g = DataReader.readGraphData(
                "data/TestGraphs/pageRank_4node.txt");
        Map<Integer, Double> ans = new HashMap<>();
        ans.put(1, .368);
        ans.put(2, .142);
        ans.put(3, .288);
        ans.put(4, .202);
        Map<Integer, Double> out = GraphToolkit.getPageRank(g);
        for (int i = 1; i <= 4; i++) {
            assertEquals(ans.get(i), out.get(i), 0.001);
        }
    }

    @Test
    public void testPageRank4NodeDisconnected() throws Exception {
        Graph g = DataReader.readGraphData(
                "data/TestGraphs/pageRank_5Node2Components.txt");
        Map<Integer, Double> ans = new HashMap<>();
        ans.put(1, .200);
        ans.put(2, .200);
        ans.put(3, .285);
        ans.put(4, .285);
        ans.put(5, .030);
        Map<Integer, Double> out = GraphToolkit.getPageRank(g);
        for (int i = 1; i <= 4; i++) {
            assertEquals(ans.get(i), out.get(i), 0.001);
        }
    }

    @Test
    public void testBellmanFord() {
        Graph g = DataReader.readGraphData(
                "data/TestGraphs/bellmanFord_large.txt");
        Map<Integer, Double> ans = new HashMap<>();
        ans.put(1, 0.0);
        ans.put(2, 1.0);
        ans.put(3, 4.0);
        ans.put(4, 0.0);
        ans.put(5, 8.0);
        ans.put(6, 0.0);
        ans.put(7, 5.0);
        ans.put(8, 2.0);
        Map<Integer, Double> result = GraphToolkit.getSingleSourceShortestPath(g, 1);
        for (int x : result.keySet()) {
            assertEquals(ans.get(x), result.get(x));
        }
    }

    @Test
    public void testFloydWarshall() {
        Graph g = DataReader.readGraphData(
                "data/TestGraphs/bellmanFord_large.txt");
        Map<Integer, Double> ans = new HashMap<>();
        ans.put(1, 0.0);
        ans.put(2, 1.0);
        ans.put(3, 4.0);
        ans.put(4, 0.0);
        ans.put(5, 8.0);
        ans.put(6, 0.0);
        ans.put(7, 5.0);
        ans.put(8, 2.0);
        Map<Integer, Double> result = GraphToolkit.getSingleSourceShortestPath(g, 1);
        for (int x : result.keySet()) {
            assertEquals(ans.get(x), result.get(x));
        }
    }
}