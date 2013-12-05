package restaurant.gui;

import restaurant.DannyCustomer;

import java.awt.*;

import SimCity.gui.Gui;

public class CustomerGui implements Gui {
	
	private static int numCustomer = 0;

	private DannyCustomer agent = null;

	private boolean pause = false;

	private boolean isPresent = false;
	private boolean isHungry = false;

	private boolean goingToSeat = false;
	private boolean waitingForOrder = false;
	private boolean eating = false;
	private boolean doneEating = false;
	private boolean goingToCashier = false;

	private String foodString;
	private String waitingString;

	private int xPos, yPos;
	private int xDestination, yDestination;

	private enum Command {
		noCommand, GoToSeat, GoToCashier, LeaveRestaurant
	};

	private Command command = Command.noCommand;
	
	private int xDefault = 60 + 23 * numCustomer;
	private int yDefault = 5;

	private int xCashierDestination = 335;
	private int yCashierDestination = 30;

	public int xTable = 100;
	public int yTable = 100;

	static final int xCustomerSize = 20;
	static final int yCustomerSize = 20;

	public CustomerGui(DannyCustomer c) { // HostAgent m) {
		agent = c;
		numCustomer++;
		xPos = xDefault;
		yPos = yDefault;
		xDestination = xDefault;
		yDestination = yDefault;
	}

	/*
	 * Animation to Update Customer Position
	 */
	public void updatePosition() {
		if (!pause) {

			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;

			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;

			if (xPos == xDestination && yPos == yDestination) {
				if (command == Command.GoToSeat)
					agent.msgAnimationFinishedGoToSeat();
				else if (command == Command.GoToCashier) {
					agent.msgAnimationFinishedGoToCashier();
					DoPrintDoneEating();
				} else if (command == Command.LeaveRestaurant) {
					agent.msgAnimationFinishedLeaveRestaurant();
					isHungry = false;
					agent.exitRestaurant();
				}
				command = Command.noCommand;
			}

			if (xPos == xDestination && yPos == yDestination
					&& xDestination == -40 && yDestination == 2) {
				goingToSeat = false;
				waitingForOrder = false;
				eating = false;
				doneEating = false;
				goingToCashier = false;
			}
		}
	}

	/*
	 * Animation to Draw the Customer
	 */
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillOval(xPos, yPos, xCustomerSize, yCustomerSize);
		g.setColor(Color.WHITE);
		g.drawOval(xPos, yPos, xCustomerSize, yCustomerSize);
		g.setColor(Color.BLACK);
		if (waitingForOrder) {
			g.drawString(waitingString, xPos + 2, yPos + 14);
		} else if (eating) {
			g.drawString(foodString, xPos + 3, yPos + 14);
		} else if (doneEating) {
			g.drawString(":)", xPos + 6, yPos + 14);
		} else if (goingToCashier) {
			g.drawString("$$", xPos + 4, yPos + 14);
		} else {
			g.drawString("C", xPos + 7, yPos + 14);
		}

	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
		xPos = xDefault;
		yPos = yDefault;
		xDestination = xDefault;
		yPos = yDefault;
	}

	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	/*
	 * Animation to Go to Table
	 */
	public void DoGoToSeat(int seatnumber, int tableNumber) {// later you will
																// map
																// seatnumber to
																// table
																// coordinates.
		if (tableNumber == 1) {
			xTable = 80;
			yTable = 100;
		} else if (tableNumber == 2) {
			xTable = 260;
			yTable = 100;
		} else if (tableNumber == 3) {
			xTable = 80;
			yTable = 260;
		} else if (tableNumber == 4) {
			xTable = 260;
			yTable = 260;
		}
		xDestination = xTable;
		yDestination = yTable;
		command = Command.GoToSeat;
	}

	public void DoGoToCashier() {
		xDestination = xCashierDestination;
		yDestination = yCashierDestination;
		command = Command.GoToCashier;
	}

	public void DoPrintGoingToSeat() {
		goingToSeat = true;
	}

	public void DoPrintWaitingForOrder(String food) {
		String foodLetters = "";
		switch (food) {
		case "Steak": {
			foodLetters = "ST";
			break;
		}
		case "Chicken": {
			foodLetters = "CH";
			break;
		}
		case "Pizza": {
			foodLetters = "PZ";
			break;
		}
		case "Salad": {
			foodLetters = "SA";
			break;
		}
		default: {
			foodLetters = "NA";
			break;
		}
		}
		foodString = foodLetters;
		waitingString = "?" + foodString;
		goingToSeat = false;
		waitingForOrder = true;
	}

	public void DoPrintEatingFood(String food) {
		String foodLetters = "";
		switch (food) {
		case "Steak": {
			foodLetters = "ST";
			break;
		}
		case "Chicken": {
			foodLetters = "CH";
			break;
		}
		case "Pizza": {
			foodLetters = "PZ";
			break;
		}
		case "Salad": {
			foodLetters = "SA";
			break;
		}
		default: {
			foodLetters = "NA";
			break;
		}
		}
		foodString = foodLetters;
		waitingForOrder = false;
		eating = true;
	}

	public void DoPrintBillDelivered() {
		foodString += "$";
	}

	public void DoPrintGoingToCashier() {
		eating = false;
		goingToCashier = true;
	}

	public void DoPrintDoneEating() {
		goingToCashier = false;
		doneEating = true;
	}

	/*
	 * Animation to Exit Restaurant
	 */
	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = 2;
		command = Command.LeaveRestaurant;
	}

	public void pause() {
		pause = true;
	}

	public void restart() {
		pause = false;
	}
}
