package Bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import Bank.bankGuardRole;
import SimCity.gui.Gui;

public class bankGuardGui implements Gui{
	
	private bankGuardRole guard;
	private int xPos, yPos;
	private int xDest,  yDest;
	private int xSize = 20, ySize = 20;
	
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
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillRect(xPos, yPos, xSize, ySize);	
	}
	
	public void doEnterBank() {
		xDest = 440;
		yDest = 320;
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
