package restaurant.gui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;

import restaurant.CookAgent;

public class CookGui implements Gui {

    private CookAgent agent = null;
    
    RestaurantGui gui;

    private int xPos = 550, yPos = 70;
    private int xDestination = 550, yDestination = 70;//default start position
    
    private int hostWidth = 20;
    private int hostHeight = 20;
    
    private boolean receivedAction;
    private static final int movementOffset = 20;

    private String displayText = "";
    private List<String> foods = new ArrayList<String>();
    private List<String> grilling = new ArrayList<String>();
    
    public CookGui(CookAgent agent, RestaurantGui r) {
        this.agent = agent;
        receivedAction = false;
        gui = r;
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
        if (displayText.trim().length() > 0){
        	if (xPos > 0 && xPos < 600 && yPos>0 && yPos<450){
        		//g.drawString(displayText, xPos, yPos);
        	}
        }
    }
    
    public void DoGoToGrills(String choice){
    	//setText("Cooking");
    	grilling.add(choice);
    }
    public void DoGoToGrills(){
    	xDestination = 520;
    	yDestination = 125;
    	receivedAction = true;
    }
    
    public void DoGoToPlates(String choice){
    	//setText("Plating");
    	foods.add(choice);
    }
    
    public void DoGoToPlates(){
    	//setText("Plating");
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
   
}
