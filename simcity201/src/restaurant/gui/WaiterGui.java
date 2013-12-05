package restaurant.gui;

import restaurant.DannyWaiter;

import java.awt.*;

import SimCity.gui.Gui;

public class WaiterGui implements Gui {

	private int numWaiter;

	private boolean pause = false;

	private DannyWaiter agent = null;

	private int xPos = -20, yPos = -20;// default waiter position
	private int xDestination = -20, yDestination = -20;// default start position

	private int xDestinationDefault = 5;
	private int yDestinationDefault;

	private final int xCookDestination = 200;
	private final int yCookDestination = 340;

	private final int xCashierDestination = 335;
	private final int yCashierDestination = 30;

	private boolean isAvailable = true;
	private String foodString;

	private enum State {
		none, processingFood, deliveringFood, gettingBill, deliveringBill
	};

	private enum Action {
		none, gettingCustomer, seatingCustomer, customerReady, deliveringFood, gettingBill, deliveringBill, walkingToCook
	};

	private State state = State.none;
	private Action action = Action.none;

	public int xTable = 100;
	public int yTable = 100;

	static final int xWaiterSize = 20;
	static final int yWaiterSize = 20;

	public WaiterGui(DannyWaiter agent, int num) {
		numWaiter = num;
		this.agent = agent;
		yDestinationDefault = 60 + 20 * numWaiter;
		xDestination = xDestinationDefault;
		yDestination = yDestinationDefault;
	}

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

			/*
			 * If the waiter is at the table and his destination is the table Do
			 * something based on why he is at the table
			 */
			if (xPos == xDestination && yPos == yDestination
					&& (xDestination == xTable + 20)
					&& (yDestination == yTable - 20)) {
				// agent.getHost().msgAtTable();

				if (action == Action.deliveringBill) {
					System.out.println("Waiter Gui : action deliveringBill");
					agent.msgAtTable();
					action = Action.none;
					state = State.none;
				} else if (action == Action.deliveringFood) {
					System.out.println("Waiter Gui : action deliveringFood");
					agent.msgAtTable();
					action = Action.none;
					state = State.none;
				} else if (action == Action.seatingCustomer) {
					System.out.println("Waiter Gui : action seatingCustomer");
					agent.msgCustomerSeated();
					action = Action.none;
				} else if (action == Action.customerReady) {
					System.out.println("Waiter Gui : action customerReady");
					agent.msgReadyToTakeOrder();
					action = Action.none;
				} else {
					System.out.println("Waiter Gui : Leaving Customer");
					DoLeaveCustomer();
				}
			}
			/*
			 * If the waiter is at the cook and his destination is the cook
			 */
			if (xPos == xDestination && yPos == yDestination
					&& (xDestination == xCookDestination)
					&& (yDestination == yCookDestination)) {
				if (action == Action.walkingToCook) {
					agent.msgAtCook();
					action = Action.none;
					state = State.none;
				} else {
					DoLeaveCustomer();
				}
			}
			/*
			 * If the waiter is at the cashier and his destination is the
			 * cashier
			 */
			if (xPos == xDestination && yPos == yDestination
					&& xDestination == xCashierDestination
					&& yDestination == yCashierDestination) {
				if (action == Action.gettingBill) {
					agent.msgAtCashier();
					action = Action.none;
					state = State.none;
				}
			}
			/*
			 * If the waiter is getting the customer
			 */
			if (xPos == xDestination && yPos == yDestination
					&& (xDestination == 60) && (yDestination == 25)) {
				if (action == Action.gettingCustomer) {
					agent.msgGotCustomer();
					action = Action.none;
				}
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.fillOval(xPos, yPos, xWaiterSize, yWaiterSize);
		g.setColor(Color.WHITE);
		g.drawOval(xPos, yPos, xWaiterSize, yWaiterSize);
		if (state == State.processingFood) {
			g.drawString("?" + foodString + "", xPos + 3, yPos + 14);
		} else if (state == State.deliveringFood) {
			g.drawString(foodString + "", xPos + 3, yPos + 14);
		} else if (state == State.gettingBill) {
			g.drawString("$?" + "", xPos + 4, yPos + 14);
		} else if (state == State.deliveringBill) {
			g.drawString("$$" + "", xPos + 4, yPos + 14);
		} else {
			g.drawString(numWaiter + "", xPos + 8, yPos + 14);
		}
	}

	public boolean isPresent() {
		return true;
	}

	public void DoBringToTable(int table) {
		translateTableNumber(table);
		xDestination = xTable + 20;
		yDestination = yTable - 20;
		action = Action.seatingCustomer;
	}

	public void DoGoToTable(int table, String reason) {
		translateTableNumber(table);
		if (reason == "deliveringFood") {
			action = Action.deliveringFood;
			state = State.deliveringFood;
		} else if (reason == "customerReady") {
			action = Action.customerReady;
		} else if (reason == "deliveringBill") {
			action = Action.deliveringBill;
			state = State.deliveringBill;
		}
		xDestination = xTable + 20;
		yDestination = yTable - 20;
	}

	public void DoPrintProcessingFood(String food) {
		System.out.println("Waiter Gui : Print Processing Food");
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
		state = State.processingFood;
	}

	public void DoPrintDeliveringFood(String food) {
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
	}

	public void DoGoToCook() {
		xDestination = xCookDestination;
		yDestination = yCookDestination;
		action = Action.walkingToCook;
	}

	public void DoGetBill() {
		xDestination = xCashierDestination;
		yDestination = yCashierDestination;
		action = Action.gettingBill;
		state = State.gettingBill;
	}

	public void DoGetCustomer() {
		xDestination = 60;
		yDestination = 25;
		action = Action.gettingCustomer;
	}

	public void DoLeaveCustomer() {
		xDestination = xDestinationDefault;
		yDestination = yDestinationDefault;
		state = State.none;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setAvailable() {
		isAvailable = true;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	private void translateTableNumber(int table) {
		if (table == 1) {
			xTable = 80;
			yTable = 100;
		} else if (table == 2) {
			xTable = 260;
			yTable = 100;
		} else if (table == 3) {
			xTable = 80;
			yTable = 260;
		} else if (table == 4) {
			xTable = 260;
			yTable = 260;
		}
	}

	public void pause() {
		pause = true;
	}

	public void restart() {
		pause = false;
	}
}
