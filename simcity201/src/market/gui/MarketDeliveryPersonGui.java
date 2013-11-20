package market.gui;

import java.awt.*;

import market.MarketDeliveryPersonRole;
import market.MarketManagerRole;
import SimCity.gui.Gui;

public class MarketDeliveryPersonGui implements Gui {

	private boolean pause = false;

	private MarketDeliveryPersonRole role = null;

	private int xPos = -20, yPos = -20;// default waiter position
	private int xDestination = -20, yDestination = -20;// default start position

	private enum State { none };

	private enum Action { none };

	private State state = State.none;
	private Action action = Action.none;

	static final int xPersonSize = 20;
	static final int yPersonSize = 20;

	public MarketDeliveryPersonGui(MarketDeliveryPersonRole role) {
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
