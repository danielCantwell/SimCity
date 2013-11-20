import java.awt.*;
import java.util.List;
import astar.*;

public class PersonGui implements Gui {
	SimCityGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination, iDestination;
    private final int SPRITE_SIZE = 32;
	private enum Command {none, traveling};
	private Command command = Command.none;
	private boolean isPresent = false;
	private AStarTraversal aStar;
	private Position currentPosition; 
	
	public PersonGui(SimCityGui gui, AStarTraversal aStar) {
		xPos = 0;
		yPos = 0;
		xDestination = 0;
		yDestination = 0;
		currentPosition = new Position(0,0);
		this.gui = gui;
		this.aStar = aStar;
		
		DoTravel(2, 7);
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
				//isPresent = false;
				//agent.msgAnimationFinishedGoToSeat();
			} 
		}
		if (xPos == getBuildingX(iDestination) && yPos == getBuildingY(iDestination)) {
			isPresent = false;
			command = Command.none;
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
    	xPos = getBuildingX(position)*32;
    	yPos = getBuildingY(position)*32;
    	currentPosition = new Position(getBuildingX(position), getBuildingY(position));
    	guiMoveFromCurrentPositionTo(new Position(getBuildingX(destination), getBuildingY(destination)));
    	iDestination = destination;
    }
    
    public int getBuildingX(int position) {
    	return ((position % 4) * 7 + 4);
    }
    
    public int getBuildingY(int position) {
    	return ((position / 4) * 7 + 6);
    }

    void guiMoveFromCurrentPositionTo(Position to){
    	if (command != Command.traveling) {
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
            currentPosition = new Position(tmpPath.getX(), tmpPath.getY ());
            xDestination = currentPosition.getX() * 32;
            yDestination = currentPosition.getY() * 32;
            
            command = Command.traveling;
            break;
        }
    }
    }
}
