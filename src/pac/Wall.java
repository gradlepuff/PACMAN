package pac;
import java.awt.*;
public class Wall extends GameObj{
	private static int blockDimension;
    private static int numOfBlocks;
    private static final int SIZE = blockDimension * numOfBlocks;
    private static char[][] maze = 
        {
            {'w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w'},
            {'w','s','s','s','s','s','s','p','s','w','s','s','s','s','s','s','s','s','w'},
            {'w','s','w','w','s','w','w','w','s','w','s','w','w','w','s','w','w','s','w'},
            {'w','s','s','s','s','s','s','s','s','s','s','s','s','s','s','s','s','s','w'},
            {'w','s','w','w','s','w','s','w','w','w','w','w','s','w','s','w','w','s','w'},
            {'w','s','s','s','s','w','s','s','s','w','s','s','s','w','s','s','s','s','w'},
            {'w','w','w','w','s','w','w','w','s','w','s','w','w','w','s','w','w','w','w'},
            {'w','s','s','s','s','w','s','s','s','s','s','s','s','w','s','s','s','p','w'},
            {'w','w','w','w','s','w','s','w','w','s','w','w','s','w','s','w','w','w','w'},
            {'s','s','s','s','s','s','s','s','s','i','s','s','s','s','s','s','s','s','s'},
            {'w','w','w','w','s','w','s','w','w','s','w','w','s','w','s','w','w','w','w'},
            {'w','s','s','s','p','w','s','s','s','s','s','s','s','w','s','s','s','s','w'},
            {'w','w','w','w','s','w','w','w','s','w','s','w','w','w','s','w','w','w','w'},
            {'w','s','s','s','s','w','s','s','s','w','s','s','s','w','s','s','s','s','w'},
            {'w','s','w','w','s','w','s','w','w','w','w','w','s','w','s','w','w','s','w'},
            {'w','s','s','s','s','s','s','s','s','s','s','s','s','s','s','s','s','s','w'},
            {'w','s','w','w','w','w','w','w','s','w','s','w','w','w','w','w','w','s','w'},
            {'w','s','s','s','s','s','s','s','p','w','s','s','s','s','s','s','s','s','w'},
            {'w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w'},
        };

    public Wall(int courtWidth, int courtHeight, int blockSize, int numBlocks) {
        super(0, 0, 0, 0, SIZE, SIZE, courtWidth, courtHeight);
        blockDimension = blockSize;
        numOfBlocks = numBlocks;
    }

    @Override
    public void draw(Graphics g) {
        for (int r = 0; r < maze.length; r ++) {
            for (int c = 0; c < maze[0].length; c++) {
                if (maze[c][r] == 'w') {
                    g.setColor(Color.BLUE);
                    g.fillRect(r * blockDimension,c * blockDimension,blockDimension,blockDimension);
                } else if (maze[c][r] == 's') {
                    g.setColor(Color.WHITE);
                    g.fillOval(r * blockDimension + 12, c * blockDimension + 12,5,5);
                } else if (maze[c][r] == 'p') {
                    g.setColor(Color.WHITE);
                    g.fillOval(r * blockDimension + 8, c * blockDimension + 8,11,11);
                }
            }
        }
    }
     
    public void boardReset() {
        for (int r = 0; r < maze.length; r ++) {
            for (int c = 0; c < maze[0].length; c++) {
                if (maze[c][r] == 'e') {
                    maze[c][r] = 's';
                } else if (maze[c][r] == 'o') {
                    maze[c][r] = 'p';
                }
            }
        }
    }
    
    public static char[][] getMaze() {
        return maze;
    }
}







