package exterior.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.trace.AlertLog;
import SimCity.trace.AlertTag;

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
	private boolean accidentProne;
	private boolean switchGui = false;
	private Person target = null;
	
	public CarGui(SimCityGui gui, int id, boolean createAccidents) {
		this.gui = gui;
		
		// Decide whether the car gets in an accident
		this.accidentProne = createAccidents;
		Random rand = new Random();
		int chooseAccidentProne = rand.nextInt(10);
		if (chooseAccidentProne < 2) {
			this.accidentProne = true;
		}
		
		Timer accidentTimer = new Timer(7500, new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   Random rand = new Random();
				   int chooseAccidentProne = rand.nextInt(10);
					
				   if (accidentProne == false && target == null && chooseAccidentProne <= 5) {
					   accidentProne = true;
				   }
	    }});
		accidentTimer.start();
		
		myID = id;
		int random = (int)(Math.random() * 8);
		int random2 = (int)(Math.random() * 8) + 8;
		DoTravel(random, random2);
	}

	public void updatePosition() {
		// Move left validly
		if (xPos < xDestination) {
			if ((int) Math.floor(xPos/64) + 1 >= 29 || (int) Math.floor(yPos/64) + 1 >= 29 ) {
				xPos+=4;
				rotation = 0;
			}
			else if (!(gui.animationPanel.MAP[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64)+1] != 'C' && xPos % 64 == 0 && 
					gui.animationPanel.MAP[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)] == 'C' && gui.animationPanel.horizontalRedLight)) {
				if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)] == 0) {
					xPos+=4;
					rotation = 0;
				}
				// Get in an accident
				else if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)] > 1 && accidentProne) {
					carAccident(gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)]).getPerson());
				}
			}
		}
		// Move right validly
		if (xPos > xDestination) {
			if ((int) Math.floor(xPos/64) - 1 <= 0 || (int) Math.floor(yPos/64) - 1 <= 0) {
				xPos-=4;
				rotation = 2;
			}
			else if (!(gui.animationPanel.MAP[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64)-1] != 'C' && xPos % 64 == 0 && 
					gui.animationPanel.MAP[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)] == 'C' && gui.animationPanel.horizontalRedLight)) {
				if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)] == 0) {
					xPos-=4;
					rotation = 2;
				}
				// Get in an accident
				else if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)] > 1 && accidentProne) {
					carAccident(gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)]).getPerson());
				}
			}
		}
		// Move down validly
		if (yPos < yDestination) {
			if ((int) Math.floor(yPos/64) + 1 >= 29 || (int) Math.floor(xPos/64) - 1 <= 0) {
				yPos+=4;
				rotation = 1;
			}
			else if (!(gui.animationPanel.MAP[(int) Math.floor(xPos/64) - 1][(int) Math.floor(yPos/64)] != 'C' && yPos % 64 == 0 && 
					gui.animationPanel.MAP[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) + 1] == 'C' && !gui.animationPanel.horizontalRedLight)) {
				if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) + 1] == 0) {
					yPos+=4;
					rotation = 1;
				}
				// Get in an accident
				else if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) + 1] > 1 && accidentProne) {
					carAccident(gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) + 1]).getPerson());
				}
			}
		}
		// Move up validly
		if (yPos > yDestination) {
			if ((int) Math.floor(yPos/64) - 1 <= 0 || (int) Math.floor(xPos/64) + 1 >= 29) {
				yPos-=4;
				rotation = 3;
			}
			else if (!(gui.animationPanel.MAP[(int) Math.floor(xPos/64) + 1][(int) Math.floor(yPos/64)] != 'C' && yPos % 64 == 0 && 
					gui.animationPanel.MAP[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64)] == 'C' && !gui.animationPanel.horizontalRedLight)) {
				if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) - 1] == 0) {
					yPos-=4;
					rotation = 3;
				}
				// Get in an accident
				else if (gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) - 1] > 1 && accidentProne) {
					carAccident(gui.animationPanel.idList.get(gui.animationPanel.vehicleGrid[(int) Math.floor(xPos/64)][(int) Math.floor(yPos/64) - 1]).getPerson());
				}
			}
		}

		// Update position on grid
		gui.animationPanel.clearVGrid(myID);
		gui.animationPanel.setVGrid((int) Math.floor(xPos/64), (int) Math.floor(yPos/64), myID);
		
		// Decide next path in route
		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.seekDest) {
				command = Command.noCommand;
				isPresent = false;
				gui.animationPanel.clearVGrid(myID);
				person.animation.release();
				if (switchGui && target != null) {
					person.loseCar(target);
					target = null;
				}
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
			g.setColor(Color.RED);
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

    // Initially start traveling to a given destination
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
    
    // Travel horizontally
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
    
    // Get into an accident
    public void carAccident(Person other) {
    	if (other != null) {
	    	if (other.gui instanceof PersonGui) {
	    		God.Get().playSound("pedsplat", false);
	    	} else {
	    		God.Get().playSound("carcrash", false);
	    	}
    	}
		AlertLog.getInstance().logWarning(AlertTag.God, "God (GUI)", "A car (" + this + ") got into an accident.");
		accidentProne = false;
		switchGui = true;
		target = other;
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
		return "Car";
	}
	
	public void setPerson(Person person) {
		this.person = person;
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
