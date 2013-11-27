package restaurant.gui;

import restaurant.interfaces.Waiter;

import java.awt.*;

public class WaiterGui implements Gui {

	private final int SPEED = 5;
	
    private Waiter agent = null;

    private int xHome, yHome;
    
    private int xPos = -20, yPos = -20;//initial waiter position
    private int xDestination = -20, yDestination = -20;//initial start position
    
    private String icon;
    
    private RestaurantGui gui;
    
	private enum Command {noCommand, GoToTable, GoToHost, GoToCashier, GoToCook};
	private Command command=Command.noCommand;
	
	boolean doesWantBreak = false;

    public WaiterGui(Waiter agent, RestaurantGui gui) {
        this.agent = agent;
        this.gui = gui;
        icon = new String();
    }

    public void updatePosition() {
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
			}
			else if (command==Command.GoToHost)
			{
				agent.msgAnimationFinishedGoToHost();
			}
			else if (command==Command.GoToCashier)
			{
				agent.msgAnimationFinishedGoToCashier();
			}
			else if (command==Command.GoToCook)
			{
				agent.msgAnimationFinishedGoToCook();
			}
			command=Command.noCommand;
        }
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
        xDestination = tablePosition.x + 20;
        yDestination = tablePosition.y - 20;
        command = Command.GoToTable;
    }
    
    public void GoToIdle()
    {
        xDestination = xHome;
        yDestination = yHome;
        command = Command.noCommand;
    }

    public void GoToHost() {
        xDestination = 50;
        yDestination = 50;
        command = Command.GoToHost;
    }

    public void GoToCashier() {
        xDestination = -20;
        yDestination = 200;
        command = Command.GoToCashier;
    }

    public void GoToCook() {
        xDestination = 300;
        yDestination = 400;
        command = Command.GoToCook;
    }

	public void GoToBreak()
	{
        xDestination = -20;
        yDestination = 50;
        command = Command.noCommand;
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
		gui.setWaiterBreak(agent, state);
	}
}
