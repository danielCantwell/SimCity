/**
 * 
 */
package housing.gui;

import housing.roles.TenantRole;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.gui.Gui;

/**
 * @author Daniel
 *
 */
public class TenantGui implements Gui {
	
	private TenantRole tenant = null;
	private boolean isPresent = true;
	
	private int xSize,	ySize;
	
	private int xPos,	yPos;
	private int xDest,	yDest;
	
	private final int xBed  	= 40;
	private final int yBed		= 40;
	
	private final int xDoor 	= 600;
	private final int yDoor 	= 315;
	
	private final int xFridge	= 30;
	private final int yFridge	= 600;
	
	private final int xStove	= 340;
	private final int yStove	= 550;
	
	private final int xTable	= 220;
	private final int yTable	= 320;
	
	private final int xMail		= 580;
	private final int yMail		= 160;
	
	private enum Dest { None, Bed, Table, Fridge, Stove, Mail, Door };
	private Dest destination = Dest.None;
	
	public TenantGui(TenantRole tenant) {
		this.tenant = tenant;
		xPos = xDoor;
		yPos = yDoor;
		xDest = xPos;
		yDest = yPos;
		isPresent= true;
		xSize = 20;
		ySize = 20;
	}

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
		
		if (xPos == xDest && yPos == yDest) {
			if (destination == Dest.Bed) {
				tenant.msgAtBed();
			}
			else if (destination == Dest.Table) {
				tenant.msgAtTable();
			}
			else if (destination == Dest.Bed) {
				tenant.msgAtBed();
			}
			else if (destination == Dest.Door) {
				tenant.msgAtDoor();
				isPresent = false;
			}
			else if (destination == Dest.Fridge) {
				tenant.msgAtFridge();
			}
			else if (destination == Dest.Mail) {
				tenant.msgAtMail();
			}
			else if (destination == Dest.Stove) {
				tenant.msgAtStove();
			}
			destination = Dest.None;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillOval(xPos, yPos, xSize, ySize);
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	public void setPresent( boolean value){
		isPresent = value;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

	// Go to the mailbox when tenant needs to pay (or not pay) rent
	public void DoGoToMailbox() {
		destination = Dest.Mail;
		xDest = xMail;
		yDest = yMail;
		System.out.println("Going To Mailbox");
	}

	// Go to the door and exit
	public void DoLeaveHouse() {
		destination = Dest.Door;
		xDest = xDoor;
		yDest = yDoor;
		System.out.println("Leaving House");
		
	}

	// Go to the fridge
	public void DoGoToFridge() {
		destination = Dest.Fridge;
		xDest = xFridge;
		yDest = yFridge;
		System.out.println("Going To Fridge");
	}
	
	// Go to the stove
	public void DoGoToStove() {
		destination = Dest.Stove;
		xDest = xStove;
		yDest = yStove;
		System.out.println("Going To Stove");
	}
	
	// Go to the table
	public void DoGoToTable() {
		destination = Dest.Table;
		xDest = xTable;
		yDest = yTable;
		System.out.println("Going To Table");
	}

	// Go to the bed and sleep
	public void DoGoToBed() {
		destination = Dest.Bed;
		xDest = xBed;
		yDest = yBed;
		System.out.println("Going To Bed");
	}

}
