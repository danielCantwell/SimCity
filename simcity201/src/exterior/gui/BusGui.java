package exterior.gui;

import java.awt.*;
import java.util.ArrayList;

import SimCity.Base.Person;

public class BusGui implements Gui {
	SimCityGui gui;

	private int xPos, yPos, rotation;
	private int xDestination, yDestination, iDestination;
    private final int SPRITE_SIZE = 64;
	private enum Command {noCommand, seekX, seekY, seekDest};
	private Command command = Command.noCommand;
	private boolean isPresent = false;
	private int myID;
	private int location = 0;
	private int[] busRoute = {0, 1, 2, 3, 7, 6, 5, 4, 8, 9, 10, 11, 15, 14, 13, 12};
	private ArrayList<Person> riders = new ArrayList<Person>();
	
	public BusGui(SimCityGui gui, int id) {
		this.gui = gui;
		myID = id;
		if (id % 2 == 0) {
			DoTravel(location, ++location);
		} else {
			location = 8;
			DoTravel(location, ++location);
		}
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
				// People going in bus
				while (gui.animationPanel.standees.get(location).size() > 0) {
					System.out.println("Someone entered the bus: " + gui.animationPanel.standees.get(location).get(0));
					riders.add(gui.animationPanel.standees.get(location).get(0));
					gui.animationPanel.standees.get(location).remove(0);
				}
				// People getting off bus
				ArrayList<Person> ridersToRemove = new ArrayList<Person>();
				for (int i = 0; i < riders.size(); i++) {
					System.out.println(riders.get(i).destination.getID());
					if (riders.get(i).destination.getID() == location) {
						ridersToRemove.add(riders.get(i));
					}
				}
				for (Person p : ridersToRemove) {
					System.out.println("Someone got off the bus: " + p);
					riders.remove(riders.indexOf(p));
					p.animation.release();
				}
				
				DoTravel(busRoute[location], busRoute[(location + 1) % 16]);
				location = (location + 1) % 16;
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
		if (SHOW_RECT) {
			g.setColor(Color.BLUE);
			if (rotation == 0 || rotation == 2) {
				g.fillRect(xPos, yPos + 12, SPRITE_SIZE, SPRITE_SIZE - 24);
			} else {
				g.fillRect(xPos + 12, yPos, SPRITE_SIZE - 24, SPRITE_SIZE);
			}
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
		return rotation;
	}

	@Override
	public String getType() {
		return "Bus";
	}
	
	public void setPerson(Person person) {
		//this.person = person;
	}

	@Override
	public int getID() {
		return myID;
	}
}
