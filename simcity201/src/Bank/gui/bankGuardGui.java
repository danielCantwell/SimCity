package Bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import Bank.interfaces.Guard;
import SimCity.gui.Gui;

public class bankGuardGui implements Gui{
	
	private Guard guard;
	private int xPos = 650, yPos = 350;
	private int xDest = 570,  yDest = 350;
	private int xSize = 30, ySize = 30;
	String displayText = "";
	
	@Override
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
			guard.doneMotion();
		}
	}

	public bankGuardGui(Guard guard) {
		this.guard = guard;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(xPos, yPos, xSize, ySize);	
		if (displayText.trim().length() >0)
			g.drawString(displayText, (xPos-5), (yPos-3));
	}
	
	public void setText(String text){
		displayText = text;
	}
	
	public void doEnterBank() {
		xDest = 440;
		yDest = 280;
	}
	
	public void doLeaveBank() {
		xDest = 650;
		yDest = 350;
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
