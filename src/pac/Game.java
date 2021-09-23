package pac;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Game implements Runnable{
	public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Pac-Man");
        frame.setLocation(300, 300);
        

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        
        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Top "Nav" Bar
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        //Button that restarts game
        final JButton reset = new JButton("Restart");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);
        
        //Button that displays instructions
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String [] lines = court.instructions();
                JList<String> list = new JList<String>(lines);
                JScrollPane scrollpane = new JScrollPane(list);
                scrollpane.setPreferredSize(new Dimension(475, 500));
                JOptionPane.showMessageDialog(
                    null, scrollpane, "Instructions", JOptionPane.PLAIN_MESSAGE);
            } 
        });
        control_panel.add(instructions);
        
        //Button that displays sorted scores
        final JButton scores = new JButton("Scores");
        scores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String toPrint = court.displayScores("files/scores.txt");
                JOptionPane.showMessageDialog(null, toPrint);
            }
        });
        control_panel.add(scores);
        
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
 
    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}

