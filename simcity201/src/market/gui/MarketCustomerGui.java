package market.gui;

import java.awt.*;

import market.MarketCustomerRole;
import SimCity.gui.Gui;

public class MarketCustomerGui implements Gui {

		private boolean pause = false;
	
		private MarketCustomerRole role = null;
	
		private int xPos = 200, yPos = 300;// default waiter position
		private int xDestination = xPos, yDestination = yPos;// default start position
	
		private enum State { none };
	
		private enum Action { none };
	
		private State state = State.none;
		private Action action = Action.none;
	
		static final int xPersonSize = 20;
		static final int yPersonSize = 20;
	
		public MarketCustomerGui(MarketCustomerRole role) {
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

    public void pause()
    {
        // TODO Auto-generated method stub
        
    }

    public void restart()
    {
        // TODO Auto-generated method stub
        
    }
}
