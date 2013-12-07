package EricRestaurant.gui;

import java.awt.*;

import EricRestaurant.EricHost;
import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Waiter;

public class HostGui implements Gui {

    private Waiter agent = null;

    public int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public static final int xTable = 200;
    public static final int yTable = 250;
    public int tn;
    private String displayText = "";

    public HostGui(Waiter agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos+=5;
        else if (xPos > xDestination)
            xPos-=5;

        if (yPos < yDestination)
            yPos+=5;
        else if (yPos > yDestination)
            yPos-=5;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + 20 + (tn*100)) & (yDestination == yTable - 20)) {
           agent.msgAtTable();
           //agent.atTheCust();
        }
    	if(xPos == 300 && yPos == 10) {
    		agent.atTheCook();
    	}
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        if (displayText.trim().length() >0)
			g.drawString(displayText, (xPos-10), (yPos-3));
    }
    
    public void setText(String text){
    	displayText = text;
    }
    
    public void setWaiter() {
    	agent.addWaiter();
    }

    public boolean isPresent() {
        return true;
    }

    public void waiting (int wNum) {
    	xDestination = 20;
    	yDestination = (20*wNum + 5);
    }
    
    public void DoBringToTable(Customer customer, int t) {
    	tn = t-1;
        xDestination = xTable + 20 + (tn*100);
        yDestination = yTable - 20;

    }
    
    public void getfromCook() {
    	xDestination = 410;
    	yDestination = 90;  
    }
    
    public void foodToCust(int t) {
    	t = t -1;
    	 xDestination = xTable + 20 + (t*100);
         yDestination = yTable - 20;
    }
    
    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
