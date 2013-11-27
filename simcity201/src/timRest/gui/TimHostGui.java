package restaurant.gui;


import restaurant.HostAgent;

import java.awt.*;

public class HostGui implements Gui {

    private HostAgent agent = null;

    private int xPos = -20, yPos = -20;//default waiter position

    public HostGui(HostAgent agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        /*if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination)
        {
        	if (agent.getState() == HostAgent.HostState.RETURNING)
        	{
        		agent.msgAtHome();
        	}
        	else if (agent.getState() == HostAgent.HostState.SEATING) 
        	{
        		agent.msgAtTable();
        	}
        }*/
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }
}
