package Bank.gui;

import Bank.interfaces.Customer;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.gui.Gui;

public class bankCustomerGui implements Gui {

	private Customer customer;
	private int xPos = 670, yPos = 350;
	private int xDest = 660,  yDest = 350;
	private int xSize = 30, ySize = 30;
	private final int yCounterPos = 110;
	private final int xCounterPos = 330;
	
	public void updatePosition() {
		if (xPos < xDest)
			xPos++;
		else if (xPos > xDest)
			xPos--;

		if (yPos < yDest)
			yPos++;
		else if (yPos > yDest)
			yPos--;
		if(xPos == xDest && yPos == yDest) {
			customer.doneMotion();
		}
	}
	
	public bankCustomerGui (Customer client) {
		this.customer = client;
	}
	//Customer enters the Bank and goes to the guard
	public void doEnterBank() {
		xDest = 470;
		yDest = 320;
	}
	//Customer waits in line
	public void doWaitLine(int custNum) {
		//customer waits in his position in line
	}
	//Customer goes to the teller
	public void doGoToTeller() {
		xDest = xCounterPos;
		yDest = yCounterPos;
	}
	//Customer leaves the Bank
	public void doLeaveBank() {
		xDest = 700;
		yDest = 320;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(xPos, yPos, xSize, ySize);	
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
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
