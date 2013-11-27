package EricRestaurant.gui;


import java.awt.*;

import EricRestaurant.EricCook;
import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Waiter;

public class CookGui implements Gui{
	private EricCook agent = null;
    public int xPos = 420, yPos = 30;//default waiter position
    private int xDestination = 420, yDestination = 30;//default start position

	  public CookGui(EricCook agent) {
	        this.agent = agent;
	    }
	  
	  public void draw(Graphics2D g) {
	        g.setColor(Color.RED);
	        g.fillRect(xPos, yPos, 20, 20);
	    }
	  
	  public void updatePosition() {
	        if (xPos < xDestination)
	            xPos+=5;
	        else if (xPos > xDestination)
	            xPos-=5;

	        if (yPos < yDestination)
	            yPos+=5;
	        else if (yPos > yDestination)
	            yPos-=5;
	    }
	  
	  public void getIng() {
		  xDestination = 510;
		  yDestination = 30;
	  }
	  
	  public void goGrill() {
		  xDestination = 420;
		  yDestination = 20;
	  }
	  
	  public void goCounter() {
		  xDestination = 420;
		  yDestination = 50;
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
}
