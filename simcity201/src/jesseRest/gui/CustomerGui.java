package jesseRest.gui;

import java.awt.*;

import jesseRest.JesseCustomer;
import jesseRest.JesseHost;

public class CustomerGui implements Gui{

	private JesseCustomer agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
    private String icon = "";
    
	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private int yBottom = 420; // Bottom of the restaurant
	private int position;
	int speed = 5;
	private enum Command {noCommand, GoToSeat, GoToCashier, LeaveRestaurant};
	private Command command=Command.noCommand;
	
    private final int X_INIT = 20;
    private int Y_INIT = 60;
    private final int SPRITE_SIZE = 20;
    private final static int TABLESIZE = 50;
	public static final int TABLEX = TABLESIZE;
	public static final int TABLEY = TABLESIZE;
    
	public CustomerGui(JesseCustomer c) {
		agent = c;
		xPos = X_INIT;
		yPos = Y_INIT;
		xDestination = X_INIT;
		yDestination = Y_INIT;
		this.gui = gui;
	}
	
    public void setIcon(String text) {
    	icon = text;
    }
    
    public void setPosition(int pos) {
    	position = pos;
    	xPos = 0;
    	yPos = Y_INIT + position*40;
    	xDestination = X_INIT;
    	yDestination = yPos;
    	Y_INIT = yPos;
    }

	public void updatePosition() {
		 if (xPos < xDestination) 
	            xPos+=speed;
	        else if (xPos > xDestination)
	            xPos-=speed;
	        if (yPos < yDestination)
	            yPos+=speed;
	        else if (yPos > yDestination)
	            yPos-=speed;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) {
				agent.msgAnimationFinishedGoToSeat();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				// Goes into a state able to be hungry again
				isHungry = false;

			} else if (command==Command.GoToCashier) {
				agent.msgAnimationFinishedGoToCashier();
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, SPRITE_SIZE, SPRITE_SIZE);
        g.setColor(Color.BLACK);
        g.drawString(icon, xPos+SPRITE_SIZE, yPos+SPRITE_SIZE);
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {
		xDestination = TABLEX*2 + 2*TABLESIZE*(seatnumber-1);
		yDestination = TABLEY*2;
		command = Command.GoToSeat;
	}
	
    public void DoGoToCashier() {
        xDestination = -SPRITE_SIZE;
        yDestination = yBottom;
        command = Command.GoToCashier;
    }

	public void DoExitRestaurant() {
		xDestination = X_INIT;
		yDestination = Y_INIT;
		command = Command.LeaveRestaurant;
	}
}
