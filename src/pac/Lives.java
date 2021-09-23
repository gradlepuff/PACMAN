package pac;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Lives extends GameObj{
	public static final String IMG_FILE = "files/pacman.png";
    public static final int SIZE = 20;

    private static BufferedImage img;

    public Lives(int courtWidth, int courtHeight, int x, int y) {
        super(0, 0, x, y, SIZE, SIZE, courtWidth, courtHeight);

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    } 

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}


