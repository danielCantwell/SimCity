package Bank.gui;

import Bank.interfaces.Teller;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.gui.Gui;

public class tellerGui implements Gui {

	private Teller teller;
	private int xPos = 30, yPos = 30;
	private int xDest = 330,  yDest = 30;
	private int xSize = 30, ySize = 30;
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
	
	public tellerGui(Teller teller) {
		this.teller = teller;
	}
	
	public void doEnterBank(int tn) {
		xDest = 100*(tn-1);
		yDest = 80;
	}
	//Teller Leaves the Bank at the end of work shift
	public void doLeaveBank() {
		xDest = 640;
		yDest = 320;
	}
	//Teller goes to his assigned work counter
	public void doGoToCounter(int tellerNum) {
		System.out.println("gui called");
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
