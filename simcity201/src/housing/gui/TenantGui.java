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

	private int xSize, ySize;

	private int xPos, yPos;
	private int xDest, yDest;

	private final int xBedOne = 40;
	private final int xBedTwo = 70;
	private final int yBed = 40;

	private final int xDoor = 600;
	private final int yDoor = 315;

	private final int xFridge = 30;
	private final int yFridge = 600;

	private final int xStove = 340;
	private final int yStove = 550;

	private final int xTable = 100;
	private final int yTable = 300;

	private final int xMail = 580;
	private final int yMail = 160;

	private enum Dest {
		None, Bed, Table, Fridge, Stove, Mail, Door
	};

	private Dest destination = Dest.None;

	public TenantGui(TenantRole tenant) {
		this.tenant = tenant;
		xPos = xDoor;
		yPos = yDoor;
		xDest = xPos;
		yDest = yPos;
		isPresent = true;
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
			} else if (destination == Dest.Table) {
				tenant.msgAtTable();
			} else if (destination == Dest.Bed) {
				tenant.msgAtBed();
			} else if (destination == Dest.Door) {
				tenant.msgAtDoor();
				isPresent = false;
			} else if (destination == Dest.Fridge) {
				tenant.msgAtFridge();
			} else if (destination == Dest.Mail) {
				tenant.msgAtMail();
			} else if (destination == Dest.Stove) {
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

	public void setPresent(boolean value) {
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
		System.out.println(tenant.myPerson.getName() + " Going To Mailbox");
	}

	// Go to the door and exit
	public void DoLeaveHouse() {
		destination = Dest.Door;
		xDest = xDoor;
		yDest = yDoor;
		System.out.println(tenant.myPerson.getName() + " Leaving House");

	}

	// Go to the fridge
	public void DoGoToFridge() {
		destination = Dest.Fridge;
		xDest = xFridge;
		yDest = yFridge;
		System.out.println(tenant.myPerson.getName() + " Going To Fridge");
	}

	// Go to the stove
	public void DoGoToStove() {
		destination = Dest.Stove;
		xDest = xStove;
		yDest = yStove;
		System.out.println(tenant.myPerson.getName() + " Going To Stove");
	}

	// Go to the table
	public void DoGoToTable(int num) {
		destination = Dest.Table;
		xDest = xTable;
		yDest = yTable;
		switch (num) {
		case 1:
			xDest = xTable;
			yDest = yTable;
			break;
		case 2:
			xDest = xTable + 80;
			yDest = yTable;
			break;
		case 3:
			xDest = xTable;
			yDest = yTable + 80;
			break;
		case 4:
			xDest = xTable + 80;
			yDest = yTable + 80;
			break;
		case 5:
			xDest = xTable + 200;
			yDest = yTable;
			break;
		case 6:
			xDest = xTable + 280;
			yDest = yTable;
			break;
		case 7:
			xDest = xTable + 200;
			yDest = yTable + 80;
			break;
		case 8:
			xDest = xTable + 280;
			yDest = yTable + 80;
			break;
		case 9:
			xDest = xTable;
			yDest = yTable + 130;
			break;
		case 10:
			xDest = xTable + 80;
			yDest = yTable + 130;
			break;
		case 11:
			xDest = xTable;
			yDest = yTable + 210;
			break;
		case 12:
			xDest = xTable + 80;
			yDest = yTable + 210;
			break;
		case 13:
			xDest = xTable + 200;
			yDest = yTable + 130;
			break;
		case 14:
			xDest = xTable + 280;
			yDest = yTable + 130;
			break;
		case 15:
			xDest = xTable + 200;
			yDest = yTable + 210;
			break;
		case 16:
			xDest = xTable + 280;
			yDest = yTable + 210;
			break;
		default:
			System.out.println("More than 16 tenants in the house.");
			System.out.println(num + " tenants");
			xDest = xTable - 40;
			yDest = yTable;
			break;
		}
		System.out.println(tenant.myPerson.getName() + " Going To Table");
	}

	// Go to the bed and sleep
	public void DoGoToBed(int num) {
		destination = Dest.Bed;
		switch (num) {
		case 1:
			xDest = xBedOne;
			yDest = yBed;
			break;
		case 2:
			xDest = xBedTwo;
			yDest = yBed;
			break;
		case 3:
			xDest = xBedOne + 120;
			yDest = yBed;
			break;
		case 4:
			xDest = xBedTwo + 120;
			yDest = yBed;
			break;
		case 5:
			xDest = xBedOne + 240;
			yDest = yBed;
			break;
		case 6:
			xDest = xBedTwo + 240;
			yDest = yBed;
			break;
		case 7:
			xDest = xBedOne + 360;
			yDest = yBed;
			break;
		case 8:
			xDest = xBedTwo + 360;
			yDest = yBed;
			break;
		case 9:
			xDest = xBedOne;
			yDest = yBed + 140;
			break;
		case 10:
			xDest = xBedTwo;
			yDest = yBed + 140;
			break;
		case 11:
			xDest = xBedOne + 120;
			yDest = yBed + 140;
			break;
		case 12:
			xDest = xBedTwo + 120;
			yDest = yBed + 140;
			break;
		case 13:
			xDest = xBedOne + 240;
			yDest = yBed + 140;
			break;
		case 14:
			xDest = xBedTwo + 240;
			yDest = yBed + 140;
			break;
		case 15:
			xDest = xBedOne + 360;
			yDest = yBed + 140;
			break;
		case 16:
			xDest = xBedTwo + 360;
			yDest = yBed + 140;
			break;
		default:
			System.out.println("More than 16 tenants in the house.");
			xDest = xBedOne - 30;
			yDest = yBed;
			break;
		}
		System.out.println(tenant.myPerson.getName() + " Going To Bed");
	}

}
