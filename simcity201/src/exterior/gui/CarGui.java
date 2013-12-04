package exterior.gui;

import java.awt.*;

import SimCity.Base.Person;

public class CarGui implements Gui {
	SimCityGui gui;

	private int xPos, yPos, rotation;
	private int xDestination, yDestination, iDestination;
    private final int SPRITE_SIZE = 64;
	private enum Command {noCommand, seekX, seekY, seekDest};
	private Command command = Command.noCommand;
	private boolean isPresent = false;
	private Person person;
	private int myID; 
	
	public CarGui(SimCityGui gui, int id) {
		this.gui = gui;
		myID = id;
		int random = (int)(Math.random() * 8);
		int random2 = (int)(Math.random() * 8) + 8;
		DoTravel(random, random2);
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			if ((int) Math.floor(xPos/64) + 1 >= 29 || (int) Math.floor(yPos/64) + 1 >= 29 ) {
				xPos+=4;
				rotation = 0;
			}
			else if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)] == 0
					&& !(gui.animationPanel.MAP[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64)+1] != 'C' && xPos % 64 == 0 && gui.animationPanel.MAP[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)] == 'C' && gui.animationPanel.horizontalRedLight)) {
				xPos+=4;
				rotation = 0;
			}
		}
		if (xPos > xDestination) {
			if ((int) Math.floor(xPos/64) - 1 <= 0 || (int) Math.floor(yPos/64) - 1 <= 0) {
				xPos-=4;
				rotation = 2;
			}
			else if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)] == 0
					&& !(gui.animationPanel.MAP[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64)-1] != 'C' && xPos % 64 == 0 && gui.animationPanel.MAP[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)] == 'C' && gui.animationPanel.horizontalRedLight)) {
				xPos-=4;
				rotation = 2;
			}
		}
		if (yPos < yDestination) {
			if ((int) Math.floor(yPos/64) + 1 >= 29 || (int) Math.floor(xPos/64) - 1 <= 0) {
				yPos+=4;
				rotation = 1;
			}
			else if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) + 1] == 0
					&& !(gui.animationPanel.MAP[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)] != 'C' && yPos % 64 == 0 && gui.animationPanel.MAP[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) + 1] == 'C' && !gui.animationPanel.horizontalRedLight)) {
				yPos+=4;
				rotation = 1;
			}
		}
		if (yPos > yDestination) {
			if ((int) Math.floor(yPos/64) - 1 <= 0 || (int) Math.floor(xPos/64) + 1 >= 29) {
				yPos-=4;
				rotation = 3;
			}
			else if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) - 1] == 0
					&& !(gui.animationPanel.MAP[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)] != 'C' && yPos % 64 == 0 && gui.animationPanel.MAP[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64)] == 'C' && !gui.animationPanel.horizontalRedLight)) {
				yPos-=4;
				rotation = 3;
			}
		}

		gui.animationPanel.clearVGrid(myID);
		gui.animationPanel.setVGrid((int) Math.floor(xPos/64), (int) Math.floor(yPos/64), myID);
		
		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.seekDest) {
				command = Command.noCommand;
				isPresent = false;
				gui.animationPanel.clearVGrid(myID);
				person.animation.release();
			} else if (command == Command.seekX) {
				command = Command.seekY;
				xDestination = xPos;
				yDestination = getBuildingY(iDestination);
				
	    		if (xPos < getBuildingX(iDestination)) {
	    			yDestination+=64;
	    		}
			} else if (command == Command.seekY) {
				command = Command.seekDest;
				xDestination = getBuildingX(iDestination);
				yDestination = yPos;
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		if (rotation == 0 || rotation == 2) {
			g.fillRect(xPos, yPos + 12, SPRITE_SIZE, SPRITE_SIZE - 24);
		} else {
			g.fillRect(xPos + 12, yPos, SPRITE_SIZE - 24, SPRITE_SIZE);
		}
	}
	
    public boolean isPresent() {
    	return isPresent;
    }

    public void DoTravel(int position, int destination) {
    	isPresent = true;
    	iDestination = destination;
    	
    	xPos = getBuildingX(position);
    	yPos = getBuildingY(position);
    	
    	if (yPos == getBuildingY(iDestination)) {
    		command = Command.seekDest;
    		xDestination = getBuildingX(iDestination);
    		yDestination = yPos;
    		
    		if (xPos < xDestination) {
    			yPos+=64;
    			yDestination+=64;
    		}
    	}
    	else {
    		command = Command.seekX;
    		xDestination = getNearestCornerX(iDestination);
    		yDestination = yPos;
    		
    		if (xPos < xDestination) {
    			yPos+=64;
    			yDestination+=64;
    		}
    	}
    }
    
    public int getBuildingX(int position) {
    	return ((position % 4) * 7 + 4)*64;
    }
    
    public int getBuildingY(int position) {
    	return ((position / 4) * 7 + 7)*64;
    }
    
    public int getNearestCornerX(int position) {
    	// Going left
    	if (xPos > getBuildingX(position)) {
    		if (yPos > getBuildingY(iDestination)) {
        		return ((position % 4) * 7 + 4 + 4)*64;
    		} else {
        		return ((position % 4) * 7 + 4 + 3)*64;
    		}
    	} else if (xPos < getBuildingX(position)){
    		// Going right
    		if (yPos > getBuildingY(iDestination)) {
        		return ((position % 4) * 7 + 4 - 3)*64;
    		} else {
        		return ((position % 4) * 7 + 4 - 4)*64;
    		}
    	} else {
    		// Equal X
    		if (yPos > getBuildingY(iDestination)) {
        		return ((position % 4) * 7 + 4 - 3)*64;
    		} else {
        		return ((position % 4) * 7 + 4 - 4)*64;
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
		return 0;
	}

	@Override
	public String getType() {
		return "Car";
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
}
