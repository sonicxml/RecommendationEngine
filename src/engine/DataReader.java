package engine;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class provides methods for reading the data from various
 * sources into the Graph class.
 *
 */
public class DataReader {
    private static final String MOVIE_LENS_FILE = 
            "data/ml-100k/u.data";

    public static Graph readMovieLensData() {
        Graph g = new Graph();
        try {
            Scanner sc = new Scanner(new File(MOVIE_LENS_FILE));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] temp = line.split("\t");
                if (temp.length == 4) {
                    int src = Integer.parseInt(temp[0]);
                    //Since there are 943 users
                    int tgt = Integer.parseInt(temp[1]) + 943;
                    int weight = Integer.parseInt(temp[2]);
                    g.addEdge(src, tgt, weight);
                }
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return g;
    }
    
    public static Graph readSampleGraphData(String filename) {
        Graph g = new Graph();         
        try {
            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] temp = line.split(", ");
                if (temp.length == 3) {
                    int src = Integer.parseInt(temp[0]);
                    int tgt = Integer.parseInt(temp[1]);
                    int weight = Integer.parseInt(temp[2]);
                    g.addEdge(src, tgt, weight);
                }
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return g;
    }
}
