package jesseRest.gui;


import java.awt.*;

import jesseRest.CustomerAgent;
import jesseRest.WaiterAgent;

public class WaiterGui implements Gui {

    private WaiterAgent agent = null;
    private String icon = "";
    private int xCook = 800-180, yCook = 290; //Cooking area position
    private int speed = 1;
    private int xDestination, yDestination; //default start position
    private int tableNumber = 0;
    private int position;
    private int xFront, yFront;
    private int xPos = 0, yPos = 0;
    private int xCustomer, yCustomer;
    
    public static final int TABLESIZE = 50;
    public static final int xTable = TABLESIZE;
    public static final int yTable = TABLESIZE;
    private final int SPRITE_SIZE = 20;
    
    public WaiterGui(WaiterAgent agent) {
        this.agent = agent;
    }
    
    public void setIcon(String text) {
    	icon = text;
    }
    
    public void setPosition(int pos) {
    	position = pos;
    	xFront = position*40 + 20;
    	yFront = 20;
    	xPos = xFront;
    	yPos = -20;
    	xDestination = xFront;
    	yDestination = yFront;
    }
    
    public String getIcon() {
    	return icon;
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

        if (tableNumber != 0 && xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable*2 + 2*TABLESIZE*(tableNumber - 1) + SPRITE_SIZE) & (yDestination == yTable*2 - SPRITE_SIZE)) {
           agent.msgAtTable();
        }
        
        if (tableNumber != 0 && xPos == xDestination && yPos == yDestination 
        		&& (xDestination == xFront) && (yDestination == yFront)) {
        	tableNumber = 0;
        	agent.msgAtFrontDesk();
        }
        
        if (xPos == xCook && yPos == yCook) {
        	agent.msgAtKitchen();
        }
        
        if (xPos == -SPRITE_SIZE && yPos == yCook) {
        	agent.msgAtCashier();
        }
        
        if (xPos == xCustomer && yPos == yCustomer) {
        	agent.msgAtCustomer();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, SPRITE_SIZE, SPRITE_SIZE);
        g.setColor(Color.BLACK);
        g.drawString(icon, xPos, yPos+SPRITE_SIZE*2);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(CustomerAgent customer, int tableToVisit) {
    	speed = 1;
    	agent.leavingFrontDesk();
    	tableNumber = tableToVisit;
		xDestination = xTable*2 + 2*TABLESIZE*(tableNumber - 1) + SPRITE_SIZE;
		yDestination = yTable*2 - SPRITE_SIZE;
    }
    
    public void DoGoToCustomer(int position) {
    	speed = 1;
    	agent.leavingFrontDesk();
    	xCustomer = SPRITE_SIZE*2;
    	yCustomer = SPRITE_SIZE*2 + position*40;
    	xDestination = xCustomer;
    	yDestination = yCustomer;
    }
    
    public void DoGoToKitchen() {
    	speed = 2;
    	agent.leavingFrontDesk();
    	xDestination = xCook;
    	yDestination = yCook;
    }

    public void DoGoToFront() {
    	speed = 2;
        xDestination = xFront;
        yDestination = yFront;
    }
    
    public void DoGoToCashier() {
    	speed = 2;
        xDestination = -SPRITE_SIZE;
        yDestination = yCook;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
	public void setOnBreak(boolean selected) {
		if (selected) {
			if (!agent.isOnBreak) {
				System.out.println("Waiter wants to go on break.");
				agent.msgWantToGoOnBreak();
			}
		} else {
			if (agent.isOnBreak) {
				System.out.println("Waiter break has ended.");
				agent.isOnBreak = false;
				agent.restartAgent();
			}
		}
	}
}
