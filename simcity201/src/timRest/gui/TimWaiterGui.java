package timRest.gui;

import timRest.interfaces.TimWaiter;

import java.awt.*;
import java.util.concurrent.Semaphore;

import SimCity.gui.Gui;

public class TimWaiterGui implements Gui {

	private final int SPEED = 5;
	
    private TimWaiter agent = null;

    private int xHome, yHome;
    
    private int xPos = -20, yPos = -20;//initial waiter position
    private int xDestination = -20, yDestination = -20;//initial start position
    
    private String icon;
    
    Semaphore positionSemaphore = new Semaphore(1, true);
    
	private enum Command {noCommand, GoToTable, GoToHost, GoToCashier, GoToCook};
	private Command command=Command.noCommand;
	
	boolean doesWantBreak = false;

    public TimWaiterGui(TimWaiter agent) {
        this.agent = agent;
        icon = new String();
    }

    public void updatePosition() {
        try {
            positionSemaphore.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		if (xPos < xDestination)
			xPos+= SPEED;
		else if (xPos > xDestination)
			xPos-= SPEED;

		if (yPos < yDestination)
			yPos+= SPEED;
		else if (yPos > yDestination)
			yPos-= SPEED;

        if (xPos == xDestination && yPos == yDestination)
        {
        	if (command==Command.GoToTable)
			{
				agent.msgAnimationFinishedGoToTable();
	            command=Command.noCommand;
			}
			else if (command==Command.GoToHost)
			{
				agent.msgAnimationFinishedGoToHost();
	            command=Command.noCommand;
			}
			else if (command==Command.GoToCashier)
			{
				agent.msgAnimationFinishedGoToCashier();
	            command=Command.noCommand;
			}
			else if (command==Command.GoToCook)
			{
				agent.msgAnimationFinishedGoToCook();
	            command=Command.noCommand;
			}
        }
        positionSemaphore.release();
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(icon, xPos + 4, yPos + 15);
    }

    public boolean isPresent() {
        return true;
    }

    public void GoToTable(Point tablePosition) {
        try {
            positionSemaphore.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        xDestination = tablePosition.x + 20;
        yDestination = tablePosition.y - 20;
        command = Command.GoToTable;
        positionSemaphore.release();
    }
    
    public void GoToIdle()
    {
        try {
            positionSemaphore.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        xDestination = xHome;
        yDestination = yHome;
        command = Command.noCommand;
        positionSemaphore.release();
    }

    public void GoToHost() {
        try {
            positionSemaphore.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        xDestination = 50;
        yDestination = 50;
        command = Command.GoToHost;
        positionSemaphore.release();
    }

    public void GoToCashier() {
        try {
            positionSemaphore.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        xDestination = -20;
        yDestination = 200;
        command = Command.GoToCashier;
        positionSemaphore.release();
    }

    public void GoToCook() {
        try {
            positionSemaphore.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        xDestination = 300;
        yDestination = 400;
        command = Command.GoToCook;
        positionSemaphore.release();
    }

	public void GoToBreak()
	{
        try {
            positionSemaphore.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        xDestination = -20;
        yDestination = 50;
        command = Command.noCommand;
        positionSemaphore.release();
	}

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setHome(int x, int y)
    {
    	xHome = x;
    	yHome = y;
    }
    
    public void setIcon(String icon)
    {
    	this.icon = icon;
    }

	public boolean doesWantBreak()
	{
		return doesWantBreak;
	}
	
	public void setBreak(boolean state)
	{
		doesWantBreak = state;
		//gui.setWaiterBreak(agent, state);
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
