package market.gui;

import java.awt.*;

import market.MarketClerkRole;
import market.MarketManagerRole;
import SimCity.gui.Gui;

public class MarketClerkGui implements Gui {

	private boolean pause = false;

	private MarketClerkRole role = null;

	private int xPos = 200, yPos = 300;// default waiter position
	private int xDestination = xPos, yDestination = yPos;// default start position

	private enum State { none };

	private enum Action { none };

	private State state = State.none;
	private Action action = Action.none;

	static final int xPersonSize = 20;
	static final int yPersonSize = 20;

	public MarketClerkGui(MarketClerkRole role) {
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
		g.setColor(Color.BLUE);
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
