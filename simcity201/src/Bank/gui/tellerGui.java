package Bank.gui;

import Bank.tellerRole;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.gui.Gui;

public class tellerGui implements Gui {

	private tellerRole teller;
	private int xPos, yPos;
	private int xDest,  yDest;
	private int xSize = 20, ySize = 20;
	private final int yCounterPos = 1;
	private final int xCounterPos = 1;
	
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
	
	public tellerGui(tellerRole teller) {
		this.teller = teller;
	}
	
	public void doEnterBank(int tellerNum) {
		//teller goes to specified location on counter
	}
	//Teller Leaves the Bank at the end of work shift
	public void doLeaveBank() {
		xDest = 640;
		yDest = 320;
	}
	//Teller goes to his assigned work counter
	public void doGoToCounter(int tellerNum) {
		xDest = xCounterPos;
		yDest = yCounterPos;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
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
