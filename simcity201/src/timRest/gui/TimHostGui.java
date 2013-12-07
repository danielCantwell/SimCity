package timRest.gui;


import timRest.TimHostRole;

import java.awt.*;

import SimCity.gui.Gui;

public class TimHostGui implements Gui {

    private TimHostRole agent = null;

    private int xPos = -20, yPos = -20;//default waiter position

    public TimHostGui(TimHostRole agent) {
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

    @Override
    public void pause()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void restart()
    {
        // TODO Auto-generated method stub
        
    }
}
