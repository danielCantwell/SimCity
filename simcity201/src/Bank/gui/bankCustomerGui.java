package Bank.gui;

import Bank.bankCustomerRole;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.gui.Gui;

public class bankCustomerGui implements Gui {

	private bankCustomerRole customer;
	private int xPos, yPos;
	private int xDest,  yDest;
	private int xSize = 20, ySize = 20;
	private final int yCounterPos = 150;
	private final int xCounterPos = 320;
	
	public void updatePosition() {
		if (xPos < xDest)
			xPos++;
		else if (xPos > xDest)
			xPos--;

		if (yPos < yDest)
			yPos++;
		else if (yPos > yDest)
			yPos--;
	}
	//Customer enters the Bank and goes to the guard
	public void doEnterBank() {
		xDest = 440;
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
		return false;
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
