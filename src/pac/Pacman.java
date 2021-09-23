package pac;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class Pacman extends GameObj{
	public static final int SIZE = 25;
    public static final int INIT_POS_X = 225;
    public static final int INIT_POS_Y = 225;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    
    private int row = 9;
    private int col = 9;
    private int score = 0;
    private int lives = 3;
    
    private String direction = "LEFT";
    private static BufferedImage img;
    public static final String IMG_FILE = "files/pacman.png";
    private boolean frightened = false;

    /**
    * Note that, because we don't need to do anything special when constructing a Pacman, we simply
    * use the superclass constructor called with the correct parameters.
    */
    public Pacman(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    
    public boolean isNextCellNavigable(String direction) {
        if (direction.equals("RIGHT")) {
            return (Wall.getMaze()[row][col + 1] != 'w');
        } else if (direction.equals("LEFT")) {
            return (Wall.getMaze()[row][col - 1] != 'w');
        } else if (direction.equals("UP")) {
            return (Wall.getMaze()[row - 1][col] != 'w');
        } else if (direction.equals("DOWN")) {
            return (Wall.getMaze()[row + 1][col] != 'w');
        }
        return false;
    }
     
    public void incrementY(String d) {
        if (d.equals("UP")) {
            row--;
        } else if (d.equals("DOWN")) {
            row++;
        }
    }
    
    public void incrementX(String d) {
        if (d.equals("LEFT")) {
            col--;
        } else if (d.equals("RIGHT")) {
            col++;
        }
    }
    
    public void setCol(int x) {
        col = x;
    }
    
    public void eat() {
        if (Wall.getMaze()[row][col] == 's') {
            score++;
            Wall.getMaze()[row][col] = 'e';
        } else if (Wall.getMaze()[row][col] == 'p') {
            Wall.getMaze()[row][col] = 'o';
            frightened = true;
        }
    }
    
    public void ghostInteraction(Ghost one, Ghost two, Ghost three) {
        if (frightened) {
            if (intersects(one)) {
                score += 200;
                one.isDead();
                two.reset();
                three.reset();
            } else if (intersects(two)) {
                score += 200;
                two.isDead();
                one.reset();
                three.reset();
            } else if (intersects(three)) {
                score += 200;
                three.isDead();
                one.reset();
                two.reset();
            } 
        } else {
            if (intersects(one) || intersects(two) || intersects(three)) {
                lives--;
                one.reset();
                two.reset();
                three.reset();
            }
        }
    }

    public int getScore() {
        return score;
    }
    
    public void addScore(int x) {
        this.score += x;
    }
    
    public void resetScore() {
        score = 0;
    }

    public int getLives() {
        return lives;
    }
    
    public void lostLife() {
        lives--;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


    public boolean isFrightened() {
        return frightened;
    }
    
    public void setFrightened(boolean x) {
        frightened = x;
    }
    
    
    //Helper getters for testing
    public int getRow() {
        return this.row;
    }
    
    public int getCol() {
        return this.col;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}

