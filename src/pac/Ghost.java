package pac;

public abstract class Ghost extends GameObj{
	private static final int SIZE = 25;
    private static final int INIT_VEL_X = 0;
    private static final int INIT_VEL_Y = 0;
    private static int initPx;
    private static int initPy;
    private boolean frightenedMode = false;
    private boolean isDead = false;
    
    public Ghost(int px, int py, int courtWidth, int courtHeight) {
        super(INIT_VEL_X , INIT_VEL_Y , px, py, SIZE, SIZE, courtWidth, courtHeight);
        initPx = px;
        initPy = py;
    }
    
    public void isDead() {
        this.isDead = true;
        this.setPx(0);
        this.setPy(0); 
    }
    
    public boolean getIsDead() {
        return this.isDead;
    }
    
    public boolean getFrightenedMode() {
        return this.frightenedMode;
    }
    
    public boolean isNearPacman(int px, int py) {
        if ((Math.abs(px - this.getPx()) <= 125) || (Math.abs(py - this.getPy()) <= 125)) {
            return true;
        }
        return false;
    }
    
    public String randomDirection() {
        int index = (int) (Math.random() * 4);
        String[] directions = {"LEFT", "RIGHT", "UP", "DOWN"};
        return directions[index];
    }
    
    public void toggleMode() {
        this.frightenedMode = !frightenedMode;
    }
    
    public void reset() {
        this.setPx(initPx);
        this.setPy(initPy);
    }
    
    
}


