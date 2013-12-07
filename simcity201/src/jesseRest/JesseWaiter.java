package jesseRest;

import SimCity.Base.Role;
import agent.Agent;
import jesseRest.Check;
import jesseRest.Menu;

import java.util.*;
import java.util.concurrent.Semaphore;

import jesseRest.JesseCustomer.AgentEvent;
import jesseRest.JesseCustomer.AgentState;
import jesseRest.JesseHost.Table;
import jesseRest.gui.WaiterGui;
import jesseRest.interfaces.Waiter;

/**
 * Restaurant Waiter Agent
 */

public class JesseWaiter extends Role implements Waiter {
	public List<MyCustomer> customers = new ArrayList<MyCustomer>();
	public enum CustomerState {DoingNothing, Waiting, Seated, Ready, AskedToOrder, AskedToReorder, Ordered, GettingFood, Eating, Done};
	public WaiterGui waiterGui = null;
	
	private JesseHost myhost;
	private JesseCook cook;
	private JesseCashier cashier;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore waitingForOrder = new Semaphore(0, true);
	private Semaphore atKitchen = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0, true);
	private Semaphore atCustomer = new Semaphore(0, true);
	private Semaphore waitingForCheck = new Semaphore (0, true);
	private boolean atFrontDesk = true;
	public boolean isOnBreak = false;
	public boolean waitingForBreak = false;
	
	public JesseWaiter(String name) {
		super();
		this.name = name;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	/**
	 * MESSAGES =====================================================
	 */
	
	public void msgSitAtTable(JesseCustomer cust, Table table) {
		// Gets message from Host to seat Customer
		customers.add(new MyCustomer(cust, table));
		System.out.println("Adding customer. List: " + customers);
		stateChanged();
	}

	public void msgImReadyToOrder(JesseCustomer cust) {
		for (MyCustomer mc : customers) {
			if (mc.customer == cust) {
				mc.state = CustomerState.Ready;
				stateChanged();
			}
		}
	}
	
	public void msgHereIsMyOrder(String choice, JesseCustomer cust) {
		for (MyCustomer mc : customers) {
			if (mc.customer == cust) {
				mc.choice = choice;
				mc.state = CustomerState.AskedToOrder;
				waitingForOrder.release();
				stateChanged();
			}
		}
	}
	
	public void msgOrderIsReady(String choice, int table) {
		// Handles Cook's order is ready message
		for (MyCustomer mc : customers) {
			if (mc.choice == choice && mc.table.tableNumber == table) {
				mc.state = CustomerState.GettingFood;
				stateChanged();
			}
		}
	}
	
	public void msgDoneEatingAndPaying(JesseCustomer cust) {
		// Handles customer message when it's finished eating
		for (MyCustomer mc : customers) {
			if (mc.customer == cust) {
				mc.state = CustomerState.Done;
				stateChanged();
			}
		}
	}
	
	public void msgHereIsCheck(Check c) {
		for (MyCustomer mc : customers) {
			if (mc.customer == c.customer) {
				mc.check = c;
				waitingForCheck.release();
				stateChanged();
			}
		}
	}
	
	// Waiter is notified when Cook is out of food
	public void msgOutOfFood(String choice, int table) {
		for (MyCustomer mc : customers) {
			if (mc.choice == choice && mc.table.tableNumber == table) {
				print("Customer " + mc + " is asked to reorder.");
				mc.state = CustomerState.AskedToReorder;
				stateChanged();
			}
		}
	}

	public void msgWantToGoOnBreak() {
		// Waiter asks Host if it can go on break
		print("Sending CanIGoOnBreak from Waiter to Host");
		myhost.msgCanIGoOnBreak(this);
		waitingForBreak = true;
		stateChanged();
	}
	
	public void msgGoOnBreak(boolean permission) {
		// Waiter is going on break.
		if (permission) {
			print("Waiter is on break.");
			isOnBreak = true;
			waitingForBreak = false;
//			pauseAgent();
			stateChanged();
		// Waiter can't go on break.
		} else {
			isOnBreak = false;
			waitingForBreak = false;
			stateChanged();
		}
	}
	// Messages from Animation
	public void msgAtTable() {
		if (atTable.availablePermits() == 0) {
			atTable.release();
			stateChanged();
		}
	}
	
	public void msgAtFrontDesk() {
			atFrontDesk = true;
			stateChanged();
	}
	
	public void msgAtKitchen() {
		if (atKitchen.availablePermits() == 0) {
			atKitchen.release();
			stateChanged();
		}
	}
	
	public void msgAtCashier() {
		if (atCashier.availablePermits() == 0) {
			atCashier.release();
			stateChanged();
		}
	}
	
	public void msgAtCustomer() {
		if (atCustomer.availablePermits() == 0) {
			atCustomer.release();
			stateChanged();
		}
	}

	/**
	 * SCHEDULER ====================================================
	 */
	
	public boolean pickAndExecuteAnAction() {
		try {
			for (MyCustomer mc : customers) {
				if (mc.state == CustomerState.Waiting && atFrontDesk) {
					SeatCustomer(mc);
					return true;
				}
			}
			for (MyCustomer mc : customers) {
				if (mc.state == CustomerState.GettingFood) {
					GiveFood(mc);
					return true;
				}
			}
			for (MyCustomer mc : customers) {
				if (mc.state == CustomerState.AskedToOrder && mc.choice != "") {
					GiveOrder(mc);
					return true;
				}
			}
			for (MyCustomer mc : customers) {
				if (mc.state == CustomerState.AskedToReorder) {
					GiveNewMenu(mc);
					return true;
				}
			}
			for (MyCustomer mc : customers) {
				if (mc.state == CustomerState.Done) {
					RemoveCustomer(mc);
					return true;
				}
			}
			for (MyCustomer mc : customers) {
				if (mc.state == CustomerState.Ready) {
					TakeOrder(mc);
					return true;
				}
			}
			for (MyCustomer mc : customers) {
					if (mc.check != null) {
						if (mc.check.state == Check.CheckTransit.None) {
							GiveCheck(mc.check);
							return true;
						}
					}
			}
			return false;
		} catch (ConcurrentModificationException e) {
			return false;
		}
	}

	/**
	 * ACTIONS  ====================================================
	 */
	
	private void SeatCustomer(MyCustomer cust) {
		waiterGui.DoGoToCustomer(cust.customer.position);
		try {
			atCustomer.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Seats customer at correct table and gives menu
		print("Message 3: Sending FollowMeToTable from Waiter to Customer.");
		cust.state = CustomerState.Seated;
		cust.customer.msgFollowMeToTable(new Menu(), this);
		cust.customer.setCurrentTable(cust.table.tableNumber);
		// Go from front desk to table
		DoSeatCustomer(cust.customer, cust.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cust.table.setOccupant(cust.customer);
		waiterGui.DoGoToFront();
	}
	
	private void TakeOrder(MyCustomer c) {
		// Responds when Customer is ready to order
		
		// Go from front desk to table
		waiterGui.DoGoToTable(c.customer, c.table.tableNumber);
		try {
			atTable.acquire();
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Tell Customer to give order
		print("Message 5: Sending WhatWouldYouLike from Waiter to Customer");
		c.customer.msgWhatWouldYouLike();
		c.state = CustomerState.AskedToOrder;
		try {
			waitingForOrder.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void GiveOrder(MyCustomer c) {
		// Handles customer food order
		waiterGui.setIcon(waiterGui.getIcon() + " " + c.choice.substring(0,2) + "?");
		
		// Go from table to kitchen
		waiterGui.DoGoToKitchen();
		try {
			atKitchen.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Sends correct Customer order to Cook
		print("Message 7: Sending HereIsAnOrder from Waiter to Cook");
		c.state = CustomerState.Ordered;
		cook.msgHereIsAnOrder(this, c.choice, c.table.tableNumber);
		waiterGui.DoGoToFront();
		waiterGui.setIcon("");
	}

	private void GiveFood(MyCustomer c) {
		// Go from front desk to kitchen
		waiterGui.DoGoToKitchen();
		try {
			atKitchen.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cook.msgFoodPickedUp(c.choice);
		
		waiterGui.setIcon(c.choice.substring(0,2));
		
		// Go from kitchen to table
		waiterGui.DoGoToTable(c.customer, c.table.tableNumber);
		try {
			atTable.acquire();
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Delivers finished order to correct Customer
		print("Message 9: Sending HereIsYourFood from Waiter to Customer");
		c.customer.msgHereIsYourFood(c.choice);
		c.state = CustomerState.Eating;
		waiterGui.setIcon("");
		
		// Go from table to cashier
		waiterGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Asks cashier to compute check
		print("Message: Sending ComputeCheck from Waiter to Cashier");
		cashier.msgComputeCheck(c.choice, c.customer, this);
		try {
			waitingForCheck.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void RemoveCustomer(MyCustomer c) {
		// Informs Host that a table is free once customer is done
		print("Message 11: Sending TableIsFree from Waiter to Host");
		myhost.msgTableIsFree(c.table);
		customers.remove(c);
	}
	
	// Waiter tells Customer they are out of food
	private void GiveNewMenu(MyCustomer c) {
		// Go from front desk to table
		DoSeatCustomer(c.customer, c.table);
		try {
			atTable.acquire();
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		print("Message: Sending PleaseReorder from Waiter to Customer.");
		c.state = CustomerState.Seated;
		Menu correctedMenu = new Menu();
		correctedMenu.foodChoices.remove(c.choice);
		c.choice = "";
		c.customer.msgPleaseReorder(correctedMenu);

		waiterGui.DoGoToFront();
	}
	
	private void GiveCheck(Check c) {
		// Go from Cashier to table
		for (MyCustomer mc : customers) {
			if (mc.customer == c.customer) {
				DoSeatCustomer(mc.customer, mc.table);
				
				try {
					atTable.acquire();
					atTable.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				print("Message: Sending TakeCheck from Waiter to Customer");
				c.state = Check.CheckTransit.ToCustomer;
				c.customer.msgTakeCheck(c);

				// Go from table to front
				waiterGui.DoGoToFront();
			}
		}
	}
	
	// Animations
	private void DoSeatCustomer(JesseCustomer customer, Table table) {
		waiterGui.DoGoToTable(customer, table.tableNumber); 
	}

	/**
	 * UTILITIES  ===================================================
	 */

	public boolean atFrontDesk() {
		return atFrontDesk;
	}
	
	public void leavingFrontDesk() {
		atFrontDesk = false;
	}
	
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public void setHost(JesseHost h) {
		myhost = h;
	}
	
	public void setCook(JesseCook c) {
		cook = c;
	}
	
	public void setCashier(JesseCashier c) {
		cashier = c;
	}
	public void print(String string) {
		System.out.println(string);
	}

	private class MyCustomer {
		JesseCustomer customer;
		Table table;
		String choice = "";
		Check check;
		CustomerState state = CustomerState.Waiting;
		
		public MyCustomer(JesseCustomer c, Table t) {
			customer = c;
			table = t;
		}
	}


	@Override
	protected void enterBuilding() {
		jesseRest.gui.WaiterGui w = new jesseRest.gui.WaiterGui(this);
		waiterGui = w;
		System.out.println(w);
		jesseRest.gui.AnimationPanel ap = new jesseRest.gui.AnimationPanel();
		ap.addGui(w);
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}
}

