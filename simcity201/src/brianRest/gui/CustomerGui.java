package brianRest.gui;

import restaurant.interfaces.Customer;

import java.awt.*;

import brianRest.BrianCustomerRole;
import brianRest.BrianTable;
import brianRest.interfaces.BrianCustomer;
import brianRest.interfaces.BrianHost;
import SimCity.gui.Gui;

public class CustomerGui implements Gui{

	private BrianCustomer agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	private BrianHostRole host;
	
	int customerNumber = -1;
	
	//GUI Customer Stats
	private final int sizeX = 20;
	private final int sizeY = 20;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command = Command.noCommand;
	
	private boolean receivedCoordinates;
	private boolean dead;
	
	String displayText = "";
	
	//Cache the host so we have access to table locations.
	//BrianHost host; //We only cache the host so that we can ask for the table location.

	public CustomerGui(BrianCustomer brianCustomerRole, BrianHost host){ //HostAgent m) {
		agent = brianCustomerRole;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		this.host = host;
		receivedCoordinates = false;
		dead = false;
	}

	public void updatePosition() {
		if (receivedCoordinates){
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
	
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
	
			if (xPos == xDestination && yPos == yDestination) {
				if (command==Command.GoToSeat) {
					if (agent instanceof BrianCustomerRole)
					((BrianCustomerRole) agent).msgAnimationFinishedGoToSeat();
				}
				else if (command==Command.LeaveRestaurant) {
					if (agent instanceof BrianCustomerRole)
					((BrianCustomerRole) agent).msgAnimationFinishedLeaveRestaurant();
					isHungry = false;
					isPresent = true;
				}
				receivedCoordinates = false;
				displayText = "";
				if (dead) displayText = "Dead Customer Pile";
				command=Command.noCommand;
			}
		}
	}
	
	public void DoLeavingTable(){
		xDestination = -20; //home base
		yDestination = -20; //home base
		receivedCoordinates = true;
	}
	
	public void DoGoToWaitingArea(){
		xDestination = 20+30*(customerNumber%2);
		yDestination = 20+30*(customerNumber/2);
		receivedCoordinates = true;
	}
	
	public void DoGoToDeadLocation(){
		xDestination = 300;
		yDestination = 400;
		dead = true;
		receivedCoordinates = true;
	}
	
	public void DoGoToSeat(int tableNumber){
		for(BrianTable t : host.tables){
			if (t.getTableNumber() == tableNumber){
				xDestination = t.getPosX();
				yDestination = t.getPosY();
			}
		}
		command = Command.GoToSeat;
		receivedCoordinates = true;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		if (dead){
			g.setColor(Color.RED);
		}
		g.fillRect(xPos, yPos, sizeX, sizeY);
		
		if (displayText.trim().length() >0)
		g.drawString(displayText, xPos, yPos);
	}
	
	public void setText(String text){
		displayText = text;
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.msgIsHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		
		command = Command.LeaveRestaurant;
		receivedCoordinates = true;
	}
	
	//utilities
	public void setCustNumber(int num){
		customerNumber = num;
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
