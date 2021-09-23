package pac;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class RandomGhost extends Ghost{
	private int row = 7;
    private int col = 1;
    public static final int INIT_POS_X = 25;
    public static final int INIT_POS_Y = 175;
    private static final String IMG_FILE_FRIGHT = "files/frightened.png";
    private static final String IMG_FILE_NORMAL = "files/random.png";
    private static BufferedImage imgNormal;
    private static BufferedImage imgFright;

    private String direction = "RIGHT";
    
    public RandomGhost(int courtWidth, int courtHeight) {
        super(INIT_POS_X, INIT_POS_Y, courtWidth, courtHeight);
        try {
            if (imgNormal == null) {
                imgNormal = ImageIO.read(new File(IMG_FILE_NORMAL));
            } 
            
            if (imgFright == null) {
                imgFright = ImageIO.read(new File(IMG_FILE_FRIGHT));
            } 
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
     
    public boolean isNextCellNavigable(String direction) {
        if (direction.equals("RIGHT") && col < 18) {
            return (Wall.getMaze()[row][col + 1] != 'w');
        } else if (direction.equals("LEFT") && col > 1) {
            return (Wall.getMaze()[row][col - 1] != 'w');
        } else if (direction.equals("UP") && row > 1) {
            return (Wall.getMaze()[row - 1][col] != 'w');
        } else if (direction.equals("DOWN") && row < 18) {
            return (Wall.getMaze()[row + 1][col] != 'w');
        }
        return false;
    }
  
    @Override
    public void move() {
        if (isNextCellNavigable(direction) && !getIsDead()) {
            if (direction.equals("LEFT")) {
                col--;
                this.setPx(this.getPx() - 25);
            } else if (direction.equals("RIGHT")) {
                col++;
                this.setPx(this.getPx() + 25);
            } else if (direction.equals("UP")) {
                row--;
                this.setPy(this.getPy() - 25);
            } else if (direction.equals("DOWN")) {
                row++;
                this.setPy(this.getPy() + 25);
            } 
        }
        this.direction = randomDirection();
    }
  
    @Override
    public void reset() {
        if (!getIsDead()) {
            this.setPx(INIT_POS_X);
            this.setPy(INIT_POS_Y);
            row = 7;
            col = 1;
        }
    }
    
    @Override
    public void draw(Graphics g) {
        if (!getFrightenedMode()) {
            g.drawImage(imgNormal, this.getPx(), this.getPy(), 
                this.getWidth(), this.getHeight(), null);
        } else if (!getIsDead()) {
            g.drawImage(imgFright, this.getPx(), this.getPy(), 
                this.getWidth(), this.getHeight(), null);
        }
    }
    
}




