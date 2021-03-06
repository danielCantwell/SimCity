package brianRest.gui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;

import SimCity.gui.Gui;
import brianRest.BrianCookRole;

public class CookGui implements Gui {

    private BrianCookRole agent = null;
    
    private int xPos = 550, yPos = 70;
    private int xDestination = 550, yDestination = 70;//default start position
    
    private int hostWidth = 20;
    private int hostHeight = 20;
    
    private boolean receivedAction;
    private static final int movementOffset = 20;

    private String displayText = "";
    private List<String> foods = new ArrayList<String>();
    private List<String> grilling = new ArrayList<String>();
    
    public CookGui(BrianCookRole agent) {
        this.agent = agent;
        receivedAction = false;
    }

    public void updatePosition() {
    	if (receivedAction){
	        if (xPos < xDestination)
	            xPos++;
	        else if (xPos > xDestination)
	            xPos--;
	        if (yPos < yDestination)
	            yPos++;
	        else if (yPos > yDestination)
	            yPos--;
	        else
	        if (xPos == xDestination && yPos == yDestination){
	        	agent.atLocation();
        		displayText = "";
        		receivedAction = false;
        		return;
	        }
    	}
    }
    
    public void DoRemoveFromGrill(String choice){
    	for (String s : grilling){
    		if(choice.equals(s)){
    			grilling.remove(choice);
    			return;
    		}	
    	}
    }
    
    public void DoRemovePlate(String choice){
    	for (String s : foods){
    		if(choice.equals(s)){
    			foods.remove(choice);
    			return;
    		}	
    	}
    }

    public void draw(Graphics2D g) {
    	
    	g.setColor(Color.black);
    	
    	g.drawString("Plated", 450, 20);
    	
    	for (int i = 0; i< foods.size(); i++){
    		g.drawString(foods.get(i), 450, 35+i*12);
    	}
    	
    	g.drawString("Grills", 450, 100);
    	
    	for (int i=0; i< grilling.size(); i++){
    		g.drawString(grilling.get(i), 450, 112+i*12);
    	}
    	
        g.setColor(Color.DARK_GRAY);
        g.fillRect(xPos, yPos, hostWidth, hostHeight);
        
    }
    
    public void DoGoToGrills(String choice){
    	grilling.add(choice);
    }
    public void DoGoToGrills(){
    	xDestination = 520;
    	yDestination = 125;
    	receivedAction = true;
    }
    
    public void DoGoToStand(){
    	xDestination = 520;
    	yDestination = 225;
    	receivedAction = true;
    }
    
    public void DoLeaveRestaurant(){
    	xDestination = -20;
    	yDestination = -20;
    	receivedAction = true;
    }
    
    public void DoGoToPlates(String choice){
    	foods.add(choice);
    }
    
    public void DoGoToPlates(){
    	xDestination = 520;
    	yDestination = 50;
    	receivedAction = true;
    }
    
    

    public boolean isPresent() {
        return true;
    }
    
    public void setText(String text){
    	displayText = text;
    }
    

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}
   
}
