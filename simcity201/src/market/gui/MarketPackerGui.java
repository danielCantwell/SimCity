package market.gui;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import market.MarketPackerRole;
import SimCity.gui.Gui;

public class MarketPackerGui implements Gui {

	private boolean pause = false;

	private MarketPackerRole role = null;

    private int xPos = 200, yPos = 200;// default counter location.
    private int xDestination = xPos, yDestination = yPos;// default start position
    
    private Map<Integer, Point> locations = Collections.synchronizedMap(new HashMap<Integer, Point>());
    private Point counterLoc = new Point(xPos, yPos);
    private String itemName;

	private enum State { none };

	private enum Action { Idle, Fetching, Returning };

	private State state = State.none;
	private Action action = Action.Idle;

	static final int xPersonSize = 20;
	static final int yPersonSize = 20;

	public MarketPackerGui(MarketPackerRole role) {
		this.role = role;
	}

	public void updatePosition() {
		if (!pause) {

			if (xPos < xDestination)
				xPos+=2;
			else if (xPos > xDestination)
				xPos-=2;

			if (yPos < yDestination)
				yPos+=2;
			else if (yPos > yDestination)
				yPos-=2;

			if (xPos == xDestination && yPos == yDestination)
			{
				   if (action == Action.Fetching)
				   {
				       role.msgGuiArrivedAtItem();
				   }
				   else if (action == Action.Returning)
				   {
				       role.msgGuiArrivedAtCounter();
				   }
				   action = Action.Idle;
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, xPersonSize, yPersonSize);
		if (action == Action.Returning)
		{
		    g.setColor(Color.WHITE);
		    g.drawString(itemName, xPos-10, yPos+30);
		}
	}

    public void DoGoToItem(int location, String name)
    {
        xDestination = locations.get(location).x;
        yDestination = locations.get(location).y;
        itemName = name;
        action = Action.Fetching;
    }

    public void DoGoToCounter()
    {
        xDestination = counterLoc.x;
        yDestination = counterLoc.y;
        action = Action.Returning;
    }

	public boolean isPresent() {
		return true;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
    
    public void setLocations(Map<Integer, Point> locations)
    {
        this.locations = locations;
    }

    public void pause()
    {
        // TODO Auto-generated method stub
        
    }

    public void restart()
    {
        // TODO Auto-generated method stub
        
    }
}
