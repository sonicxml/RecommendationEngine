package tests;

import engine.DataReader;
import engine.Recommender;
import engine.Graph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
* Recommender Tester.
*/
public class RecommenderTest {

@Before
public void before() throws Exception {
}

@After
public void after() throws Exception {
}

/**
*
* Method: collabFilter(int userID, TreeMap<Double, List<Node>> scores)
*
*/
@Test
public void testCollabFilter() throws Exception {
    int userID = 1;
    Recommender r = new Recommender(DataReader.readMovieLensData());
    List<Integer> recommended = r.collabFilter(userID,
            r.getPearsonScores(userID), 10, 5);
    System.out.println(recommended);
    r.getMovieLensNames(userID, recommended);
}

@Test
public void testCollabFilterUser2() throws Exception {
    int userID = 2;
    Recommender r = new Recommender(DataReader.readMovieLensData());
    List<Integer> recommended = r.collabFilter(userID,
            r.getPearsonScores(userID), 10, 5);
    System.out.println(recommended);
    r.getMovieLensNames(userID, recommended);
}

@Test
public void testCollabFilterTestData() throws Exception {
    int userID = 2;
    String filename = "data/ml-100k/u1.base";
    Recommender r = new Recommender(DataReader.readMovieLensTestData(filename));
    List<Integer> recommended = r.collabFilter(userID,
            r.getPearsonScores(userID), 10, 5);
    System.out.println(recommended);
    r.getMovieLensNames(userID, recommended);
}

@Test
public void testCollabFilterAccuracy() throws Exception {
    double found = 0.0;
    double expected = 15 * 943;
    
    for (int i = 1; i < 6; i++) {
        String file1 = "data/ml-100k/u" + i +".base";
        String file2 = "data/ml-100k/u" + i + ".test";
        System.out.println(i);
        Graph g = DataReader.readMovieLensTestData(file2);
        Recommender r = new Recommender(DataReader.readMovieLensTestData(file1));
        for (int j = 1; j < 944; j++) {
            System.out.println("Finding recommendations for:" + j);
            List<Integer> recommended = r.collabFilter(j, 
                    r.getPearsonScores(j), 10, 3);
            for (int k = 0; k < 3; k++) {
                if (g.containsEdge(j, recommended.get(k))) {
                    found++;
                }
            }
        }
    }
    
    System.out.println("Percentage of recommendations found is: " + found / expected);
}

/**
*
* Method: getPearsonScores(int userID)
*
*/
@Test
public void testGetPearsonScores() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: getJaccardScores(int userID)
*
*/
@Test
public void testGetJaccardScores() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: getNode()
*
*/
@Test
public void testGetNode() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: getWeight()
*
*/
@Test
public void testGetWeight() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: setWeight(double weight)
*
*/
@Test
public void testSetWeight() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: compareTo(Entry other)
*
*/
@Test
public void testCompareTo() throws Exception {
//TODO: Test goes here...
}


/**
*
* Method: getTopMatches(TreeMap<Double, List<Node>> map, int limit)
*
*/
@Test
public void testGetTopMatches() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = Recommender.getClass().getMethod("getTopMatches", TreeMap<Double,.class, int.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
}

/**
*
* Method: getPearsonCoeff(Node n1, Node n2)
*
*/
@Test
public void testGetPearsonCoeff() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = Recommender.getClass().getMethod("getPearsonCoeff", Node.class, Node.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
}

/**
*
* Method: getJaccardCoeff(Node n1, Node n2)
*
*/
@Test
public void testGetJaccardCoeff() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = Recommender.getClass().getMethod("getJaccardCoeff", Node.class, Node.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
}

}
