package tests;

import engine.DataReader;
import engine.Graph;
import engine.Recommender;
import org.junit.Test;

import java.util.List;

/**
 * Recommender Tester.
 */
public class RecommenderTest {
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
        int numRecommendations = 5;
        int numSimilarUsers = 75;
        double expected = 5 * numRecommendations * 943;

        for (int i = 1; i < 6; i++) {
            String file1 = "data/ml-100k/u" + i + ".base";
            String file2 = "data/ml-100k/u" + i + ".test";
            Graph g = DataReader.readMovieLensTestData(file2);
            Recommender r = new Recommender(DataReader.readMovieLensTestData(file1));
            for (int j = 1; j < 944; j++) {
                List<Integer> recommended = r.collabFilter(j,
                        r.getPearsonScores(j), numSimilarUsers, numRecommendations);
                for (int k = 0; k < numRecommendations; k++) {
                    if (recommended.size() > k &&
                            g.containsEdge(j, recommended.get(k))) {
                        found++;
                    }
                }
            }
        }

        System.out.println("Percentage of recommendations found is: "
                + found / expected);
    }
}