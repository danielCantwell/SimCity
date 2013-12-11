package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import market.MarketCustomerRole;
import SimCity.gui.Gui;

public class MarketCustomerGui implements Gui {
    
    static final int xPersonSize = 20;
    static final int yPersonSize = 20;

	private boolean pause = false;

	private MarketCustomerRole role = null;

	private int xPos = -xPersonSize, yPos = 300;// default customer position
	private int xDestination = xPos, yDestination = yPos;// default start position

	private int xDoor = xPos, yDoor = yPos;
	
	private enum State { none };

    private enum Action { Idle, ToLine, ToCounter, ToDoor };

	private State state = State.none;
	private Action action = Action.Idle;

	public MarketCustomerGui(MarketCustomerRole role) {
		this.role = role;
	}

	public void updatePosition() {
		if (!pause) {

			if (xPos < xDestination)
				xPos+=5;
			else if (xPos > xDestination)
				xPos-=5;

			if (yPos < yDestination)
				yPos+=5;
			else if (yPos > yDestination)
				yPos-=5;

			if (xPos == xDestination && yPos == yDestination)
			{
                if (action == Action.ToLine)
                {
                    role.msgGuiArrivedAtLine();
                }
                else if (action == Action.ToCounter)
                {
                    role.msgGuiArrivedAtClerk();
                }
                else if (action == Action.ToDoor)
                {
                    role.msgGuiArrivedAtDoor();
                }
                action = Action.Idle;
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, xPersonSize, yPersonSize);
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

    public void DoGoToLine(Point location)
    {
        xDestination = location.x;
        yDestination = location.y;
        action = Action.ToLine;
    }

    public void DoGoToCounter(Point location)
    {
        xDestination = location.x;
        yDestination = location.y;
        action = Action.ToCounter;
    }

    public void DoGoToDoor()
    {
        xDestination = xDoor;
        yDestination = yDoor;
        action = Action.ToDoor;
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
