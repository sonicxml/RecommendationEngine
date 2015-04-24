package engine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;


public class DataReader {
    private static final String MOVIE_LENS_FILE = "";

    public static Graph readMovieLensData() {
        Graph g = new Graph();
        HashMap<Integer, Node> nodes = new HashMap<>();
        try {
            Scanner sc = new Scanner(new File(MOVIE_LENS_FILE));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] temp = line.split("\t");
                if (temp.length == 4) {
                    int src = Integer.parseInt(temp[0]);
                    int tgt = Integer.parseInt(temp[1]);
                    int weight = Integer.parseInt(temp[2]);
                    g.addEdge(src, tgt, weight);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return g;
    }
}
