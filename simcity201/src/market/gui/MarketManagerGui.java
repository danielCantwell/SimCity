package market.gui;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import market.MarketManagerRole;
import SimCity.gui.Gui;

public class MarketManagerGui implements Gui {

	private boolean pause = false;

	private MarketManagerRole role = null;

    private int xPos = 500, yPos = 250;// default waiter position
    private int xDestination = xPos, yDestination = yPos;// default start position

	private Map<String, Inventory> inventory = Collections.synchronizedMap(new HashMap<String, Inventory>());
	private Map<Integer, Point> locations = Collections.synchronizedMap(new HashMap<Integer, Point>());
	
	private enum State { none };

	private enum Action { none };

	private State state = State.none;
	private Action action = Action.none;

	static final int xPersonSize = 20;
	static final int yPersonSize = 20;

	public MarketManagerGui(MarketManagerRole role) {
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
    
    public void updateInventory(String name, int amount, int location)
    {
        if (inventory.containsKey(name))
        {
            Inventory i = inventory.get(name);
            i.setData(amount, location);
        }
        else
        {
            inventory.put(name, new Inventory(amount, location));
        }
    }

	public void draw(Graphics2D g) {
		g.setColor(Color.ORANGE);
		g.fillRect(xPos, yPos, xPersonSize, yPersonSize);
		
		g.setColor(Color.GRAY);
        for (Inventory i : inventory.values())
        {
            if (i.amount > 0)
            {
                g.fillRect(i.xLocation, i.yLocation, 10, 10);
            }
        }
        g.setColor(Color.BLACK);
        for (Inventory i : inventory.values())
        {
            if (i.amount > 0)
            {
                g.drawRect(i.xLocation, i.yLocation, 10, 10);
            }
        }
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
    
    public class Inventory
    {
        int amount;
        int xLocation;
        int yLocation;
        
        public Inventory(int amount, int location)
        {
            setData(amount, location);
        }
        
        public void setData(int amount, int location)
        {
            this.amount = amount;
            this.xLocation = locations.get(location).x;
            this.yLocation = locations.get(location).y;
        }
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
