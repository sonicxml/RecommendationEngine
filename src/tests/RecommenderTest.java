package tests;

import engine.DataReader;
import engine.Recommender;
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
    Recommender r = new Recommender(DataReader.readMovieLensTestData());
    List<Integer> recommended = r.collabFilter(userID,
            r.getPearsonScores(userID), 10, 5);
    System.out.println(recommended);
    r.getMovieLensNames(userID, recommended);
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
