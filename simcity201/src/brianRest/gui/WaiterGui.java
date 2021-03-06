package brianRest.gui;


import restaurant.interfaces.Customer;

import java.awt.*;

import SimCity.Base.God;
import SimCity.gui.Gui;
import brianRest.BrianAbstractWaiter;
import brianRest.BrianHostRole;
import brianRest.BrianPCWaiterRole;
import brianRest.BrianTable;
import brianRest.BrianWaiterRole;
import brianRest.interfaces.BrianCustomer;
import brianRest.interfaces.BrianWaiter;

public class WaiterGui implements Gui {

    private BrianWaiter agent = null;
    
    int backUpTime = 0;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    private int xTable = 200;
    private int yTable = 250;
    
    private  int hostWidth = 20;
    private  int hostHeight = 20;
    
    private boolean receivedAction;
    private boolean doingIdle;
    private static final int movementOffset = 20;
    
    private boolean wantBreak;
    	public boolean isOnBreak(){
    		return wantBreak;
    	}
    	public void wantBreak(boolean b){
    		wantBreak = b;
    		agent.msgWantABreak();
    	}
    
    private String displayText = "";

    public WaiterGui(BrianWaiter brianPCWaiterRole) {
        this.agent = brianPCWaiterRole;
        receivedAction = false;
        doingIdle = false;
        wantBreak = false;
    }

    public void updatePosition() {
    	if (receivedAction){
    		if (God.Get().hour == backUpTime+2){
    			agent.atLocation();
    			receivedAction = false;
    			return;
    		}
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
        		if(!doingIdle)
        			agent.atLocation();
        		displayText = "";
        		receivedAction = false;
        		return;
	        }
    	}
    }

    public void draw(Graphics2D g) {
    	if (agent instanceof BrianWaiterRole)
        g.setColor(Color.MAGENTA);
    	else if (agent instanceof BrianPCWaiterRole){
    		g.setColor(Color.ORANGE);
    	}
        g.fillRect(xPos, yPos, hostWidth, hostHeight);
        if (displayText.trim().length() > 0){
        	if (xPos > 0 && xPos < 600 && yPos>0 && yPos<450){
        		g.drawString(displayText, xPos, yPos);
        	}
        }
    }

    public boolean isPresent() {
        return true;
    }
    
    public void setText(String text){
    	displayText = text;
    }
    
    
    private void receivedAction(){
    	backUpTime = God.Get().hour;
    }
    
    public void DoTakeABreak(){
    	xDestination = -20; //Top Left of the screen
    	yDestination = -20; //Top Left of the screen
    	receivedAction = true;
    	doingIdle = false;
    }

    public void DoBringToTable(BrianCustomer customer, int tableNumber) {
    	receivedAction();
    	for (BrianTable myTable : ((BrianHostRole)(agent.getHost())).tables){
    		if (myTable.getTableNumber() == tableNumber){
    			xTable = myTable.getPosX();
    			yTable = myTable.getPosY();
    			
    			xDestination = xTable + movementOffset;
    			yDestination = yTable - movementOffset;
    		}
    	}
    	receivedAction = true;
    }
    
    public void DoIdle(){
    	
    	xDestination = 200 + 25 * (agent.getWaiterNumber() % 10); //Idle destination
    	yDestination = 50 + 25 * (agent.getWaiterNumber() / 10); //Idle destination
    	receivedAction = true;
    	doingIdle = true;
    }
    
    public void DoGoToDeathPile(){
    	xDestination = 300;
    	yDestination = 400;
    	receivedAction = true;
    	doingIdle = false;
    }
    
    public void DoGetCustomer(){
    	receivedAction();
    	xDestination = 20; //Host destination
    	yDestination = 20; // Host Destination
    	receivedAction = true;
    	doingIdle = false;
    }
    
    public void DoOffBreak(){
    	wantBreak = false;
    }
    
    public void DoGiveOrderToCook(){
    	receivedAction();
    	xDestination = 510; //Destination of cook
    	yDestination = 70; //Destination of cook
    	receivedAction = true;
    	doingIdle = false;
    }
    
    public void DoGoToCashier(){
    	receivedAction();
    	xDestination = 100; //Host destination
    	yDestination = 30; // Host Destination
    	receivedAction = true;
    	doingIdle = false;
    }
    
    public void DoWalkToCustomer(BrianTable table, String text){
    	receivedAction();
    	xDestination = table.getPosX() + movementOffset;
    	yDestination = table.getPosY() - movementOffset;
    	displayText = text;
    	receivedAction = true;
    	doingIdle = false;
    }
    
    public void exitBuilding(){
    	xDestination = -20; //Host destination
    	yDestination = -20; // Host Destination
    	receivedAction = true;
    	doingIdle = false;
    }

    public void DoLeaveCustomer() {
    	receivedAction();
        xDestination = -movementOffset;
        yDestination = -movementOffset;
        receivedAction = true;
        doingIdle = false;
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
