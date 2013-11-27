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
	
	public CarGui(SimCityGui gui) {
		//agent = c;
		xPos = -20;
		yPos = -20;
		xDestination = -20;
		yDestination = -20;
		this.gui = gui;
		
		int random = (int)(Math.random() * 8);
		int random2 = (int)(Math.random() * 8) + 8;
		DoTravel(random, random2);
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos+=4;
			rotation = 0;
		}
		else if (xPos > xDestination) {
			xPos-=4;
			rotation = 2;
		}
		if (yPos < yDestination) {
			yPos+=4;
			rotation = 1;
		}
		else if (yPos > yDestination) {
			yPos-=4;
			rotation = 3;
		}

		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.seekDest) {
				command = Command.noCommand;
				isPresent = false;
				person.animation.release();
			} else if (command == Command.seekX) {
				command = Command.seekY;
				xDestination = xPos;
				yDestination = getBuildingY(iDestination);
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
    	}
    	else {
    		command = Command.seekX;
    		xDestination = getNearestCornerX(iDestination);
    		yDestination = yPos;
    	}
    }
    
    public int getBuildingX(int position) {
    	int target = ((position % 4) * 7 + 4)*64;
    	if (yPos < target) { target += 64; }
    	return target;
    }
    
    public int getBuildingY(int position) {
    	int target = ((position / 4) * 7 + 7)*64;
    	if (xPos < target) { target += 64; }
    	return target;
    }
    
    public int getNearestCornerX(int position) {
    	if (xPos > getBuildingX(position)) {
    		if (yPos < getBuildingY(position)) {
    			return ((position % 4) * 7 + 4 + 3)*64;
    		} else {
    			return ((position % 4) * 7 + 4 + 4)*64;
    		}
    	} else {
    		if (yPos < getBuildingY(position)) {
    			return ((position % 4) * 7 + 4 - 4)*64;
    		} else {
    			return ((position % 4) * 7 + 4 - 3)*64;
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
