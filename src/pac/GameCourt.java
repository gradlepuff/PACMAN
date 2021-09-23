package pac;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
public class GameCourt extends JPanel{
	private Pacman pacman; 
    private RandomGhost ghostOne;
    private PatrolGhost ghostTwo;
    private AvoidGhost ghostThree;
    private Wall wall;
    private Lives lifeOne, lifeTwo, lifeThree;
    
    private boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    private int counter = 0; //"Timer" counter during frightened mode
    
    //Score IO
    private static String scoreFile = "files/scores.txt";
    private BufferedReader br = null;
    private BufferedWriter bw = null;
    private ArrayList<Scorer> scores = new ArrayList<Scorer>();
    

    // Game constants
    public final static int BLOCK_SIZE = 25;
    public final static int NUM_OF_BLOCKS = 19;
    public static final int COURT_WIDTH = BLOCK_SIZE * NUM_OF_BLOCKS;
    public static final int COURT_HEIGHT = COURT_WIDTH + 50;
    public static final int SQUARE_VELOCITY = 5;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    
    public GameCourt(JLabel status) {
        setBackground(Color.BLACK);
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!
        
        // Enable keyboard focus on the court area.
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                playing = true;
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    pacman.setDirection("LEFT");
                    if (pacman.getPx() == 0) {
                        pacman.setCol(18);
                        pacman.setPx(GameCourt.COURT_WIDTH);
                    } else if (pacman.isNextCellNavigable("LEFT")) {
                        pacman.incrementX("LEFT");
                        pacman.setPx(pacman.getPx() - BLOCK_SIZE);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    pacman.setDirection("RIGHT");
                    if (pacman.getPx() + BLOCK_SIZE == COURT_WIDTH) {
                        pacman.setCol(0);
                        pacman.setPx(0);
                    } else if (pacman.isNextCellNavigable("RIGHT")) {
                        pacman.incrementX("RIGHT");
                        pacman.setPx(pacman.getPx() +  BLOCK_SIZE);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN 
                    && pacman.isNextCellNavigable("DOWN")) {
                    pacman.setDirection("DOWN");
                    pacman.incrementY("DOWN");
                    pacman.setPy(pacman.getPy() +  BLOCK_SIZE);
                } else if (e.getKeyCode() == KeyEvent.VK_UP && pacman.isNextCellNavigable("UP")) {
                    pacman.setDirection("UP");
                    pacman.incrementY("UP");
                    pacman.setPy(pacman.getPy() -  BLOCK_SIZE);
                }
                pacman.eat();
            }
        });
        this.status = status;
    }

    /**
     * (Re-)set/Restarts the game to its initial state.
     */
    public void reset() {
        wall = new Wall(COURT_WIDTH, COURT_HEIGHT, BLOCK_SIZE, NUM_OF_BLOCKS);
        wall.boardReset();
        pacman = new Pacman(COURT_WIDTH, COURT_HEIGHT);

        lifeOne = new Lives(COURT_WIDTH, COURT_HEIGHT, 0, 480);
        lifeTwo = new Lives(COURT_WIDTH, COURT_HEIGHT, 25, 480);
        lifeThree = new Lives(COURT_WIDTH, COURT_HEIGHT, 50, 480);
        
        ghostOne = new RandomGhost(COURT_WIDTH, COURT_HEIGHT); 
        ghostTwo = new PatrolGhost(COURT_WIDTH, COURT_HEIGHT); 
        ghostThree = new AvoidGhost(COURT_WIDTH, COURT_HEIGHT); 
        
        status.setText("Running...");
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        playing = false;
        repaint();
    }
    
    //Returns instructions
    public String[] instructions() {
        String[] lines = { "Welcome to Pac-Man!", " ",
            "The objective of this game is to accumulate as many points as possible",
            "by eating dots and blue ghosts. Each dot contributes 1 point to your score",
            "and blue ghosts contribute 200 points.", " ",
            "To start, press any arrow key. This will begin the game and control Pac-Man.", 
            "Pac-Man. In response, the ghosts will begin moving. When you press an arrow", 
            "key, Pac-Man will move one block in that direction. When the key is released,", 
            "Pac-Man will not move. You can choose to either hold the arrow key down",
            "or press and release the arrow key to move. Move and avoid the ghosts.",
            "Pac-Man can only move in the black areas and cannot traverse the blue walls."," ", 
            "When you hit a ghost, you will lose one of your three lives, indicated by", 
            "the Pac-Man icons on the bottom left under the playing area. However, if",
            "Pac-Man eats a Power Pellet (a large white dot), the game will enter", 
            "'frightened mode', where the ghosts are frightened and can be eaten by",
            "Pac-Man without losing a life for a limited amount of time. In frightened",
            "mode, the ghosts are blue with frowny faces. When Pac-Man hits a ghost,", 
            "the ghosts will return to their original spots to give you time to move Pac-Man.", " ",
            "The red ghost, Blinky, moves randomly. The pink ghost, Pinky, is a patrol", 
            "ghost and only moves when Pac-Man comes near it. The orange ghost, Clyde,",
            "moves in the opposite direction of Pac-Man when he's nearby, trying to avoid", 
            "Pac-Man. The game ends when either you lost all 3 lives or Pac-Man eats all", 
            "three ghosts. When the game is over, a dialog box will open for you to input",
            "your name and save your score. When dialog boxes are open, the game will", 
            "pause.", " ",
            "To restart the game, select the 'Restart' button. To view high scores, select", 
            "the 'Scores' button. Good luck!", 
            };
        playing = false;
        requestFocusInWindow();
        repaint();
        return lines;
    }

    //Takes in user input to save score
    public void addScore() {
        String name = JOptionPane.showInputDialog("Input your name:");
        Scorer temp = new Scorer(name, pacman.getScore());
        scores.add(temp);
        Collections.sort(scores, new ScoreSorter());
        try {
            if (scoreFile == null) {
                throw new IllegalArgumentException();
            }
            this.bw = new BufferedWriter(new FileWriter(scoreFile));
            for (Scorer x: scores) {
                bw.write(x.getName().replaceAll("-", "") + "-" + x.getScore());
                bw.newLine();
            }
            bw.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        } catch (IOException e) {
            System.out.print("Error adding score to score data");
            e.printStackTrace();
        }
    }
        
    //Displays sorted scores in descending order
    public String displayScores(String scoreFile) {
        playing = false;
        String toPrint = "";
        try {
            if (scoreFile == null) {
                throw new IllegalArgumentException();
            }
            this.br = new BufferedReader(new FileReader(scoreFile));
            while (br.ready()) {
                String temp = br.readLine();
                if (temp != null && temp.contains("-")) {
                    int separator = temp.indexOf("-");
                    int score = Integer.parseInt(temp.substring(separator + 1));
                    toPrint = toPrint + temp.substring(0,separator) + "-" + score + "\n";
                } 
            } 
        } catch (FileNotFoundException e) {
            toPrint = "Error accessing score data";
            throw new IllegalArgumentException();
        } catch (IOException e) {
            toPrint = "Error accessing score data";
            System.out.print("Error reading score data to score data");
            e.printStackTrace();
        }

        if (toPrint.isEmpty()) {
            toPrint = "No Scores Saved";
        }
        requestFocusInWindow();
        repaint();
        return toPrint;
    }
    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
            ghostOne.move();
            
            if (ghostTwo.isNearPacman(pacman.getPx(), pacman.getPy())) {
                ghostTwo.move();
            }
            ghostThree.move(pacman.getDirection(), ghostThree.isNearPacman(pacman.getPx(), 
                pacman.getPy()));
 
            
            if (!pacman.isFrightened()) {
                pacman.ghostInteraction(ghostOne, ghostTwo, ghostThree);
            } else if (pacman.isFrightened() && counter < 185) {
                if (counter == 0) {
                    ghostOne.toggleMode();
                    ghostTwo.toggleMode();
                    ghostThree.toggleMode();
                }
                counter++;
                pacman.ghostInteraction(ghostOne, ghostTwo, ghostThree);
            } else if (pacman.isFrightened() && counter >= 185) {
                pacman.setFrightened(false);
                counter = 0;
                ghostOne.toggleMode();
                ghostTwo.toggleMode();
                ghostThree.toggleMode();
                if (!ghostOne.getIsDead()) {
                    ghostOne.reset();
                } 
                if (!ghostTwo.getIsDead()) {
                    ghostTwo.reset();
                } 
                if (!ghostThree.getIsDead()) {
                    ghostThree.reset();
                }
            }        
            repaint();
            
            //Game End-State
            if (pacman.getLives() == 0) {
                status.setText("Game Over!");
                playing = false;
                addScore();
               
            } else if (ghostOne.getIsDead() && ghostTwo.getIsDead() && ghostThree.getIsDead()) {
                status.setText("Pac-Man ate all the ghosts!");
                playing = false;
                addScore();
            }

        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        wall.draw(g);
        pacman.draw(g);
        
        if  (!ghostOne.getIsDead()) {
            ghostOne.draw(g);
        }

        if (!ghostTwo.getIsDead()) {
            ghostTwo.draw(g);
        }
        
        if (!ghostThree.getIsDead()) {
            ghostThree.draw(g);
        }
        
        //Score Text
        g.setColor(Color.WHITE);
        Font bigFont = new Font("SansSerif", Font.BOLD, 15);
        g.setFont(bigFont);
        g.drawString("Score: " + pacman.getScore(), 204,510);
        
        //Lives Display
        if (pacman.getLives() == 3) {
            lifeOne.draw(g);
            lifeTwo.draw(g);
            lifeThree.draw(g);
        } else if (pacman.getLives() == 2) {
            lifeOne.draw(g);
            lifeTwo.draw(g);
        } else if (pacman.getLives() == 1) {
            lifeOne.draw(g);
        } 

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}

