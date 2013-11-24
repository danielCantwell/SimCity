package market.gui;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import market.MarketManagerRole;
import market.MarketPackerRole;
import SimCity.gui.Gui;

public class MarketPackerGui implements Gui {

	private boolean pause = false;

	private MarketPackerRole role = null;

    private int xPos = 200, yPos = 200;// default counter location.
    private int xDestination = xPos, yDestination = yPos;// default start position
    
    private Map<Integer, Point> locations = Collections.synchronizedMap(new HashMap<Integer, Point>());
    private Point counterLoc = new Point(xPos, yPos);

	private enum State { none };

	private enum Action { none };

	private State state = State.none;
	private Action action = Action.none;

	static final int xPersonSize = 20;
	static final int yPersonSize = 20;

	public MarketPackerGui(MarketPackerRole role) {
		this.role = role;
	}

	public void updatePosition() {
		if (!pause) {

			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;

			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;

			if (xPos == xDestination && yPos == yDestination)
			{
				   
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, xPersonSize, yPersonSize);
	}

    public void DoGoToItem(int location)
    {
        xDestination = locations.get(location).x;
        yDestination = locations.get(location).y;
    }

    public void DoGoToCounter()
    {
        xDestination = counterLoc.x;
        yDestination = counterLoc.y;
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
