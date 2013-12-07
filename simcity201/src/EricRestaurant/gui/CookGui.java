package EricRestaurant.gui;


import java.awt.*;

import EricRestaurant.EricCook;
import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Waiter;

public class CookGui implements Gui{
	private EricCook agent = null;
    public int xPos = -20, yPos = -20;
    private int xDestination = 420, yDestination = 30;
    String displayText = "";
    
	  public CookGui(EricCook agent) {
	        this.agent = agent;
	    }
	  
	  public void draw(Graphics2D g) {
	        g.setColor(Color.RED);
	        g.fillRect(xPos, yPos, 20, 20);
	        if (displayText.trim().length() >0)
				g.drawString(displayText, (xPos-10), (yPos-3));
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
	  
	  public void setText(String string) {
		displayText = string;  
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
