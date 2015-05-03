package engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.TreeMap;

/**
 * Class for allowing the user to interact with the Recommendation
 * Engine.  Provides parameters for choosing the data set, choosing
 * the score metric, choosing the number of similar users, and 
 * choosing the number of recommendations to calculate.
 *
 */
public class GUI implements Runnable {
    /**
     * Displays the GUI.
     */
    public void run() {
        final int LEFT = SwingConstants.LEFT;
        
        final JFrame frame = new JFrame("Recommendation Engine");
        frame.setLocation(150, 75);
        frame.setResizable(false);
        frame.setLayout(new GridLayout(2, 1));
        
        final JPanel elements = new JPanel();
        elements.setLayout(new GridLayout(3, 1));
        
        final JPanel datasetPane = new JPanel();
        datasetPane.setLayout(new FlowLayout());
        final JLabel dataLabel = new JLabel("Select a data set:", LEFT);
        String[] options = new String[] { "MovieLens" };
        final JComboBox<String> dataset = new JComboBox<>(options);
        dataset.setSelectedIndex(0);
        datasetPane.add(dataLabel);
        datasetPane.add(dataset);
        elements.add(datasetPane);
        
        final JPanel scorePane = new JPanel();
        scorePane.setLayout(new FlowLayout());
        final JLabel scoreLabel = new JLabel("Select a score metric:", LEFT);
        String[] simScores = new String[] { "Jaccard", "Pearson" };
        final JComboBox<String> metrics = new JComboBox<>(simScores);
        metrics.setSelectedIndex(0);
        scorePane.add(scoreLabel);
        scorePane.add(metrics);
        elements.add(scorePane);
        
        final JPanel input = new JPanel();
        input.setLayout(new FlowLayout());
        final JLabel userLabel = new JLabel("User:", LEFT);
        final JSpinner user = new JSpinner(new SpinnerNumberModel(1, 1, 943, 1));
        final JLabel simLabel = new JLabel("Number of similar users:", LEFT);
        final JSpinner sim = new JSpinner(new SpinnerNumberModel(10, 1, 30, 1));
        final JLabel recLabel = new JLabel("Number of recommendations:", LEFT);
        final JSpinner rec = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));
        final JButton go = new JButton("Run!");
        input.add(userLabel);
        input.add(user);
        input.add(simLabel);
        input.add(sim);
        input.add(recLabel);
        input.add(rec);
        input.add(go);
        elements.add(input);
        frame.add(elements);
        
        final JTextArea results = new JTextArea("", 10, 45);
        final JScrollBar vert = new JScrollBar(JScrollBar.VERTICAL);
        final JScrollPane scrollPane = new JScrollPane(results);
        scrollPane.setVerticalScrollBar(vert);
        frame.add(scrollPane);
        
        dataset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> source = (JComboBox<String>)e.getSource();
                String newText = (String)source.getSelectedItem();
                dataset.setSelectedItem(newText);
                updateJSpinners(dataset, user);
            }
        });
        
        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Recommender r = null;
                String data = (String)dataset.getSelectedItem();
                switch (data) {
                    case "MovieLens" :
                        Graph g = DataReader.readMovieLensData();
                        r = new Recommender(g);
                        break;
                    default :
                        break;
                }
                
                
                int userID = (int)user.getValue();
                int numSim = (int)sim.getValue();
                int numRec = (int)rec.getValue();
                
                TreeMap<Double, List<Node>> scores;
                String metric = (String)metrics.getSelectedItem(); 
                switch (metric) {
                    case "Jaccard" :
                        scores = r.getJaccardScores(userID);
                        break;
                    case "Pearson" :
                        scores = r.getPearsonScores(userID);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                
                try {
                    results.setText(r.getMovieLensNames(userID, r.collabFilter(userID,
                            scores, numSim, numRec)));
                    results.setEditable(false);
                } 
                catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        });
        
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    private void updateJSpinners(JComboBox<String> dataset, JSpinner user) {
        String data = (String)dataset.getSelectedItem();
        switch (data) {
            case "MovieLens" :
                user.setModel(new SpinnerNumberModel(1, 1, 943, 1));
                break;
            default:
                break;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GUI());
    }
}
