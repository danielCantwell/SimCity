import java.awt.*;

public class CarGui implements Gui {
	SimCityGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination, iDestination;
    private final int SPRITE_SIZE = 20;
	private enum Command {noCommand, seekX, seekY, seekDest};
	private Command command = Command.noCommand;
	private boolean isPresent = false;
	
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
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.seekDest) {
				command = Command.noCommand;
				isPresent = false;
				//agent.msgAnimationFinishedGoToSeat();
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
		g.fillRect(xPos, yPos, SPRITE_SIZE, SPRITE_SIZE);
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
    	int target = ((position % 4) * 7 + 4)*32 + 6;
    	if (yPos < target) { target += 32; }
    	return target;
    }
    
    public int getBuildingY(int position) {
    	int target = ((position / 4) * 7 + 7)*32 + 6;
    	if (xPos < target) { target += 32; }
    	return target;
    }
    
    public int getNearestCornerX(int position) {
    	if (xPos > getBuildingX(position)) {
    		if (yPos < getBuildingY(position)) {
    			return ((position % 4) * 7 + 4 + 3)*32 + 6;
    		} else {
    			return ((position % 4) * 7 + 4 + 4)*32 + 6;
    		}
    	} else {
    		if (yPos < getBuildingY(position)) {
    			return ((position % 4) * 7 + 4 - 4)*32 + 6;
    		} else {
    			return ((position % 4) * 7 + 4 - 3)*32 + 6;
    		}
    	}
    }
}
