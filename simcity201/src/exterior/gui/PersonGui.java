package exterior.gui;

import java.awt.*;
import java.util.List;

import exterior.astar.*;

import java.util.Random;

import javax.swing.ImageIcon;

import SimCity.Base.Person;

public class PersonGui implements Gui {
	private boolean isPresent = false;
	private int xPos, yPos, rotation;
	private int xDestination, yDestination, iDestination;
    private final int SPRITE_SIZE = 64;
    private final int CITY_SIZE = 4;
	private enum Command {none, traveling};
	private Command command = Command.none;
	private AStarTraversal aStar;
	private Position currentPosition; 
	private Person person;
	private int myID;
	private SimCityGui gui;
	
	public PersonGui(SimCityGui gui, int myID, AStarTraversal aStar) {
		this.aStar = aStar;
		this.myID = myID;
		this.gui = gui;
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			if (gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)]) instanceof CarGui || 
					(gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)]) instanceof PersonGui &&  
					gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)]).getRotation() == rotation)) {
				rotation = 0;
			} else {
				xPos+=2;
				rotation = 0;
			}
		}
		else if (xPos > xDestination) {
			if (gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)]) instanceof CarGui || 
					(gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)]) instanceof PersonGui &&  
					gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)]).getRotation() == rotation)) {
				rotation = 2;
			} else {
				xPos-=2;
				rotation = 2;
			}
		}
		if (yPos < yDestination) {
			if (gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) + 1]) instanceof CarGui || 
					(gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) + 1]) instanceof PersonGui &&  
					gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) + 1]).getRotation() == rotation)) {
				rotation = 1;
			} else {
				yPos+=2;
				rotation = 1;
			}
		}
		else if (yPos > yDestination) {
			if (gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) - 1]) instanceof CarGui || 
					(gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) - 1]) instanceof PersonGui &&  
					gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) - 1]).getRotation() == rotation)) {
				rotation = 3;
			} else {
				yPos-=2;
				rotation = 3;
			}
		}
		gui.animationPanel.clearVGrid(myID);
		gui.animationPanel.setVGrid((int) Math.floor(xPos/64), (int) Math.floor(yPos/64), myID);
		
		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.traveling) {
				command = Command.none;
		    	guiMoveFromCurrentPositionTo(new Position(getBuildingX(iDestination), getBuildingY(iDestination)));
			}
		}
		if (currentPosition != null && xPos == getBuildingX(iDestination)*SPRITE_SIZE && yPos == getBuildingY(iDestination)*SPRITE_SIZE) {
			isPresent = false;
            currentPosition.release(aStar.getGrid());
			command = Command.none;
			gui.animationPanel.clearVGrid(myID);
			person.animation.release();
		}
	}

	public void draw(Graphics2D g) {
		if (SHOW_RECT) {
			g.setColor(Color.GREEN);
			g.fillRect(xPos + 16, yPos + 16, SPRITE_SIZE/2, SPRITE_SIZE/2);
		}
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

    void guiMoveFromCurrentPositionTo(Position to) {
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
	            currentPosition = new Position(tmpPath.getX(), tmpPath.getY());
	            //System.out.println("new pos: " + currentPosition.getX() + " " + currentPosition.getY());
	            xDestination = currentPosition.getX() * SPRITE_SIZE;
	            yDestination = currentPosition.getY() * SPRITE_SIZE;
	            command = Command.traveling;
	            break;
	        }
	    }
    }

	@Override
	public int getX() {
		return xPos;
	}

	@Override
	public int getY() {
		return yPos;
	}

	@Override
	public int getRotation() {
		return rotation;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String getType() {
		return "Person";
	}

	@Override
	public int getID() {
		return myID;
	}

	@Override
	public Person getPerson() {
		return person;
	}
}
