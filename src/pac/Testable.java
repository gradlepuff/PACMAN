package pac;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JLabel;

import org.junit.jupiter.api.Test;
public class Testable {
	
	  public void pacmanEatsSmallDot() {
	        JLabel status = new JLabel("Running...");
	        GameCourt court = new GameCourt(status);
	        Pacman pacman = new Pacman(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        court.reset();
	        
	        //Moves Pac-Man one block to the left
	        pacman.incrementX("LEFT");
	        assertEquals('s',Wall.getMaze()[pacman.getRow()][pacman.getCol()]);
	        
	        //Pac-Man eats dot on that block
	        pacman.eat();
	        
	        assertTrue(pacman.isNextCellNavigable("LEFT"));
	        assertFalse(pacman.isNextCellNavigable("UP"));
	        assertFalse(pacman.isNextCellNavigable("DOWN"));
	        assertFalse(pacman.isFrightened());
	        assertEquals(1, pacman.getScore());
	        assertEquals('e',Wall.getMaze()[pacman.getRow()][pacman.getCol()]);
	        
	        //Checks if dot isn't there anymore after Pac-Man leaves square and returns 
	        pacman.incrementX("LEFT");
	        pacman.incrementX("RIGHT");
	        assertEquals('e',Wall.getMaze()[pacman.getRow()][pacman.getCol()]);
	    }
	   
	    
	  public void pacmanEatsPowerPelletAndTriggersFrightened() {
	        JLabel status = new JLabel("Running...");
	        GameCourt court = new GameCourt(status);
	        Pacman pacman = new Pacman(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        court.reset();
	        
	        //Moves Pac-Man to closest power pellet
	        pacman.incrementX("LEFT");
	        pacman.incrementX("LEFT");
	        pacman.incrementX("LEFT");
	        pacman.incrementX("LEFT");
	        pacman.incrementX("LEFT");
	        pacman.incrementY("DOWN");
	        pacman.incrementY("DOWN");
	        
	        //Pac-Man eats PP on that block
	        assertEquals('p',Wall.getMaze()[pacman.getRow()][pacman.getCol()]);
	        pacman.eat();
	        
	        assertTrue(pacman.isNextCellNavigable("LEFT"));
	        assertTrue(pacman.isNextCellNavigable("UP"));
	        assertTrue(pacman.isNextCellNavigable("DOWN"));
	        assertFalse(pacman.isNextCellNavigable("RIGHT"));
	        assertEquals(0, pacman.getScore());
	        assertTrue(pacman.isFrightened());
	        assertEquals('o',Wall.getMaze()[pacman.getRow()][pacman.getCol()]);
	    }
	    
	    
	  public void testScoreDisplay() {
	        JLabel status = new JLabel("Running...");
	        GameCourt court = new GameCourt(status);
	        assertThrows(IllegalArgumentException.class, () -> {
	            String toPrint = court.displayScores(null);  
	            assertEquals("Error reading score data to score data",
	                toPrint);
	        });

	        assertThrows(IllegalArgumentException.class, () -> {
	            String toPrint = court.displayScores("doesn't exist");  
	            assertEquals("Error reading score data to score data",
	                toPrint);
	        }); 
	    }
	    
	    
	  public void testPacmanEatsGhost() {
	        JLabel status = new JLabel("Running...");
	        GameCourt court = new GameCourt(status);
	        Pacman pacman = new Pacman(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        PatrolGhost ghostOne = new PatrolGhost(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        AvoidGhost ghostTwo = new AvoidGhost(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        RandomGhost ghostThree = new RandomGhost(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        court.reset();
	        
	        //Moves Pac-Man to closest power pellet
	        pacman.incrementX("LEFT");
	        pacman.incrementX("LEFT");
	        pacman.incrementX("LEFT");
	        pacman.incrementX("LEFT");
	        pacman.incrementX("LEFT");
	        pacman.incrementY("DOWN");
	        pacman.incrementY("DOWN");
	        
	        //Pac-Man eats PP on that block
	        assertEquals('p',Wall.getMaze()[pacman.getRow()][pacman.getCol()]);
	        pacman.eat();
	        
	        pacman.setPx(225);
	        pacman.setPy(225);
	        ghostOne.setPx(225);
	        ghostOne.setPy(225);
	        
	        //Pac-Man eats first ghost
	        pacman.ghostInteraction(ghostOne, ghostTwo, ghostThree);
	        assertTrue(pacman.isFrightened());
	        assertEquals(200, pacman.getScore());
	        assertEquals(3, pacman.getLives());
	        assertTrue(ghostOne.getIsDead());
	        
	        //Checks that alive ghosts reset positions
	        assertEquals(0, ghostOne.getPx());
	        assertEquals(0, ghostOne.getPy());
	        assertEquals(425, ghostTwo.getPx());
	        assertEquals(275, ghostTwo.getPy());
	        assertEquals(25, ghostThree.getPx());
	        assertEquals(175, ghostThree.getPy());
	        
	        //Pac-Man eats second ghost
	        ghostTwo.setPx(225);
	        ghostTwo.setPy(225);
	        pacman.ghostInteraction(ghostOne, ghostTwo, ghostThree);
	        assertEquals(400, pacman.getScore());
	        assertEquals(3, pacman.getLives());
	        assertTrue(ghostTwo.getIsDead());
	        
	        //Checks that alive ghosts reset positions
	        assertEquals(0, ghostOne.getPx());
	        assertEquals(0, ghostOne.getPy());
	        assertEquals(0, ghostTwo.getPx());
	        assertEquals(0, ghostTwo.getPy());
	        assertEquals(25, ghostThree.getPx());
	        assertEquals(175, ghostThree.getPy());
	        
	        //Pac-Man eats last ghost
	        ghostThree.setPx(225);
	        ghostThree.setPy(225);
	        pacman.ghostInteraction(ghostOne, ghostTwo, ghostThree);
	        assertEquals(600, pacman.getScore());
	        assertEquals(3, pacman.getLives());
	        assertTrue(ghostThree.getIsDead());
	        
	        //Checks that ghosts are dead and aren't roaming the board
	        assertEquals(0, ghostOne.getPx());
	        assertEquals(0, ghostOne.getPy());
	        assertEquals(0, ghostTwo.getPx());
	        assertEquals(0, ghostTwo.getPy());
	        assertEquals(0, ghostThree.getPx());
	        assertEquals(0, ghostThree.getPy());
	    }
	    
	    
	    
	  public void testPacmanIntersectsGhost() {
	        JLabel status = new JLabel("Running...");
	        GameCourt court = new GameCourt(status);
	        Pacman pacman = new Pacman(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        PatrolGhost ghostOne = new PatrolGhost(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        AvoidGhost ghostTwo = new AvoidGhost(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        RandomGhost ghostThree = new RandomGhost(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        court.reset();
	        
	        pacman.setPx(225);
	        pacman.setPy(225);
	        ghostOne.setPx(225);
	        ghostOne.setPy(225);
	        
	        pacman.ghostInteraction(ghostOne, ghostTwo, ghostThree);
	        assertFalse(pacman.isFrightened());
	        assertEquals(0, pacman.getScore());
	        assertEquals(2, pacman.getLives());
	        assertFalse(ghostOne.getIsDead());
	        
	        //Checks that alive ghosts reset positions
	        assertEquals(25, ghostOne.getPx());
	        assertEquals(275, ghostOne.getPy());
	        assertEquals(425, ghostTwo.getPx());
	        assertEquals(275, ghostTwo.getPy());
	        assertEquals(25, ghostThree.getPx());
	        assertEquals(175, ghostThree.getPy());
	        
	        ghostTwo.setPx(225);
	        ghostTwo.setPy(225);
	        
	        pacman.ghostInteraction(ghostOne, ghostTwo, ghostThree);
	        assertFalse(pacman.isFrightened());
	        assertEquals(0, pacman.getScore());
	        assertEquals(1, pacman.getLives());
	        assertFalse(ghostTwo.getIsDead());
	        
	        ghostThree.setPx(225);
	        ghostThree.setPy(225);
	        
	        pacman.ghostInteraction(ghostOne, ghostTwo, ghostThree);
	        assertFalse(pacman.isFrightened());
	        assertEquals(0, pacman.getScore());
	        assertEquals(0, pacman.getLives());
	        assertFalse(ghostThree.getIsDead());
	    }
	    
	    
	    
	  public void testPatrolGhostMove() {
	        PatrolGhost ghost = new PatrolGhost(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        ghost.move();
	        ghost.move();
	        ghost.move();
	        
	        assertTrue(ghost.isNextCellNavigable("LEFT"));
	        assertTrue(ghost.isNextCellNavigable("UP"));
	        assertTrue(ghost.isNextCellNavigable("DOWN"));
	        assertFalse(ghost.isNextCellNavigable("RIGHT"));
	        assertEquals(100,ghost.getPx());
	        assertEquals(275,ghost.getPy());
	        
	        //Makes sure it doesn't move if it's dead
	        ghost.isDead();
	        ghost.move();
	        assertEquals(0,ghost.getPx());
	        assertEquals(0,ghost.getPy());
	        ghost.move();
	        assertEquals(0,ghost.getPx());
	        assertEquals(0,ghost.getPy());
	    }
	    
	    
	  public void testAvoidGhostMove() {
	        AvoidGhost ghost = new AvoidGhost(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        
	        //Check if ghost moves opposite of Pac-Man
	        ghost.move("RIGHT", true);
	        ghost.move("RIGHT", true);
	        ghost.move("RIGHT", true);
	        
	        //Check if ghost doesn't move if next cell not navigable
	        assertFalse(ghost.isNextCellNavigable("LEFT"));
	        assertTrue(ghost.isNextCellNavigable("UP"));
	        assertTrue(ghost.isNextCellNavigable("DOWN"));
	        assertTrue(ghost.isNextCellNavigable("RIGHT"));
	        assertEquals(350,ghost.getPx());
	        assertEquals(275,ghost.getPy());
	        
	        //Check if ghost doesn't move if it a wall is in the way
	        ghost.move("RIGHT", true);
	        assertEquals(350,ghost.getPx());
	        assertEquals(275,ghost.getPy());
	        
	        //Makes sure it doesn't move if it's dead
	        ghost.isDead();
	        ghost.move();
	        assertEquals(0,ghost.getPx());
	        assertEquals(0,ghost.getPy());
	        ghost.move();
	        assertEquals(0,ghost.getPx());
	        assertEquals(0,ghost.getPy());
	    }
	    
	   
	  public void testRandomGhostMove() {
	        RandomGhost ghost = new RandomGhost(GameCourt.COURT_WIDTH,GameCourt.COURT_HEIGHT);
	        ghost.move();
	        
	        //Because randomized, testing movement to the right once
	        assertTrue(ghost.isNextCellNavigable("LEFT"));
	        assertFalse(ghost.isNextCellNavigable("UP"));
	        assertFalse(ghost.isNextCellNavigable("DOWN"));
	        assertTrue(ghost.isNextCellNavigable("RIGHT"));
	        assertEquals(50,ghost.getPx());
	        assertEquals(175,ghost.getPy());
	        
	        //Makes sure it doesn't move if it's dead
	        ghost.isDead();
	        ghost.move();
	        assertEquals(0,ghost.getPx());
	        assertEquals(0,ghost.getPy());
	        ghost.move();
	        assertEquals(0,ghost.getPx());
	        assertEquals(0,ghost.getPy());
	    }

	}


