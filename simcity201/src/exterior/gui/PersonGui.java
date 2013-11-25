package exterior.gui;

import java.awt.*;
import java.util.List;
<<<<<<< HEAD

import exterior.astar.AStarNode;
import exterior.astar.*;
=======
import java.util.Random;
import java.util.concurrent.Semaphore;
>>>>>>> exterior

import exterior.astar.*;

public class PersonGui implements Gui {
	private boolean isPresent = false;
	private int xPos, yPos;
	private int xDestination, yDestination, iDestination;
    private final int SPRITE_SIZE = 64;
    private final int CITY_SIZE = 4;
	private enum Command {none, traveling};
	private Command command = Command.none;
	private AStarTraversal aStar;
	private Position currentPosition; 
	
	public PersonGui(SimCityGui gui, AStarTraversal aStar) {
		this.aStar = aStar;
		Random rand = new Random();
		int start = rand.nextInt(15);
		int end = rand.nextInt(15);
		DoTravel(start, end);
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.traveling) {
				command = Command.none;
		    	guiMoveFromCurrentPositionTo(new Position(getBuildingX(iDestination), getBuildingY(iDestination)));
			} 
		}
		if (xPos == getBuildingX(iDestination)*SPRITE_SIZE && yPos == getBuildingY(iDestination)*SPRITE_SIZE) {
			isPresent = false;
            currentPosition.release(aStar.getGrid());
			command = Command.none;
			//agent.msgAnimationFinishedGoToSeat();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, SPRITE_SIZE, SPRITE_SIZE);
	}
	
    public boolean isPresent() {
    	return isPresent;
    }

    public void DoTravel(int position, int destination) {
    	isPresent = true;
    	xPos = getBuildingX(position)*SPRITE_SIZE;
    	yPos = getBuildingY(position)*SPRITE_SIZE;
    	xDestination = getBuildingX(position)*SPRITE_SIZE;
    	yDestination = getBuildingY(position)*SPRITE_SIZE;
    	iDestination = destination;
    	currentPosition = new Position(getBuildingX(position), getBuildingY(position));
    	guiMoveFromCurrentPositionTo(new Position(getBuildingX(destination), getBuildingY(destination)));
    }
    
    public int getBuildingX(int position) {
    	return ((position % CITY_SIZE) * 7) + 4;
    }
    
    public int getBuildingY(int position) {
    	return ((position / CITY_SIZE) * 7) + 6;
    }

    void guiMoveFromCurrentPositionTo(Position to){
    	if (command != Command.traveling) {
    		
    	/*
		Semaphore[][] grid = aStar.getGrid();
        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 30; x++) {
            	System.out.print(grid[x][y].availablePermits() + " ");
            }
            	System.out.println("");
            }
        System.out.println("====");
        */
         
        AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);
        List<Position> path = aStarNode.getPath();
        boolean firstStep = true;
        boolean gotPermit = true;

        for (Position tmpPath : path) {
            if (firstStep) {
                firstStep = false;
                continue;
            }
            
            //Try and get lock for the next step.
            int attempts = 1;
            gotPermit = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());
            
            //Did not get lock. Lets make n attempts.
            while (!gotPermit && attempts < 3) {
                try { Thread.sleep(1000); }
                catch (Exception e){}

                gotPermit = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());
                attempts ++;     
            }

            //Did not get lock after trying n attempts. So recalculating path.            
            if (!gotPermit) {
                guiMoveFromCurrentPositionTo(to);
                break;
            }

            //Got the required lock. Lets move.
            currentPosition.release(aStar.getGrid());
            currentPosition = new Position(tmpPath.getX(), tmpPath.getY());
            //System.out.println("new pos: " + currentPosition.getX() + " " + currentPosition.getY());
            xDestination = currentPosition.getX() * SPRITE_SIZE;
            yDestination = currentPosition.getY() * SPRITE_SIZE;
            command = Command.traveling;
            break;
        }
    }
    }
}
