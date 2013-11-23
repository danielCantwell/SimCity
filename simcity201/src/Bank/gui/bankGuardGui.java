package Bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import Bank.bankGuardRole;
import SimCity.gui.Gui;

public class bankGuardGui implements Gui{
	
	private bankGuardRole guard;
	private int xPos = 650, yPos = 350;
	private int xDest = 570,  yDest = 350;
	private int xSize = 30, ySize = 30;
	
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

	public bankGuardGui(bankGuardRole guard) {
		this.guard = guard;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, xSize, ySize);	
	}
	
	public void doEnterBank() {
		xDest = 440;
		yDest = 320;
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
