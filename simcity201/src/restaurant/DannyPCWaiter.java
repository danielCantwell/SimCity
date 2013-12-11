/**
 * 
 */
package restaurant;

import java.util.ConcurrentModificationException;

import SimCity.Base.Person.Intent;
import SimCity.Buildings.B_DannyRestaurant;
import SimCity.trace.AlertTag;
import restaurant.gui.DannyRestaurantAnimationPanel;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * @author Daniel
 * 
 */
public class DannyPCWaiter extends DannyAbstractWaiter implements Waiter {

	public DannyPCWaiter() {

	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * MESSAGES
	 */


	// Messages from Animation

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {

		if (workOver) {
			leaveRestaurant();
			return true;
		}

		try {

			if (state == WaiterState.Available
					&& event == WaiterEvent.RequestingBreak) {
				askHostForBreak();
				return true;
			}

			if (state == WaiterState.Available
					&& event == WaiterEvent.BreakDenied) {
				DoBreakDenied();
				return true;
			}

			if (state == WaiterState.WaitingForBreak && myCustomers.isEmpty()) {
				goOnBreak();
				return true;
			}

			if (state == WaiterState.OnBreak && event == WaiterEvent.BreakOver) {
				goOffBreak();
				return true;
			}

			for (MyCustomer myCustomer : myCustomers) {
				System.out.println("Waiter: " + hashCode() + " myCustomer");
				if (myCustomer.state == CustomerState.Waiting) {
					print("seatCustomer");
					seatCustomer(myCustomer);
					return true;
				}
			}

			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.state == CustomerState.ReadyToOder) {
					takeOrder(myCustomer);
					return true;
				}
			}

			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.state == CustomerState.WaitingForOrder
						&& myCustomer.event == CookEvent.DeliveringOrder) {
					print("Scheduler : ProcessOrder");
					processOrder(myCustomer);
					return true;
				}
			}

			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.state == CustomerState.WaitingForOrder
						&& myCustomer.event == CookEvent.OrderReady) {
					deliverFood(myCustomer);
					return true;
				}
			}

			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.state == CustomerState.WaitingForOrder
						&& myCustomer.event == CookEvent.OutOfStock) {
					deliverNewMenu(myCustomer);
					return true;
				}
			}

			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.cashierEvent == CashierEvent.GetBill) {
					getBill(myCustomer);
					return true;
				}
			}

			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.cashierEvent == CashierEvent.BillReady) {
					giveCustomerBill(myCustomer);
					return true;
				}
			}

		} catch (ConcurrentModificationException e) {
			print("Concurrent Modifcation Exception has been caught.");
			return false;
		}

		return false;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

	// Actions

	private void seatCustomer(MyCustomer myCustomer) {
		DoSeatCustomer(myCustomer);
		myCustomer.customer.msgFollowMe(this, new Menu(), myCustomer.table);
		myCustomer.state = CustomerState.Seated;
		try {
			customerSeated.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void takeOrder(MyCustomer myCustomer) {
		DoGoToTable(myCustomer);
		try {
			readyToTakeOrder.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		myCustomer.customer.msgWhatDoYouWant();
		myCustomer.state = CustomerState.AskedToOrder;
		try {
			waitingForOrder.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void deliverNewMenu(MyCustomer myCustomer) {
		DoGoToTable(myCustomer);
		try {
			readyToTakeOrder.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myCustomer.customer.msgOutOfChoice(new Menu(myCustomer.choice));
		myCustomer.state = CustomerState.AskedToOrder;
		try {
			waitingForOrder.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void processOrder(MyCustomer myCustomer) {
		DoTellCookOrder(myCustomer);
		try {
			walkingToCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myCustomer.event = CookEvent.None;
		B_DannyRestaurant dr = (B_DannyRestaurant)myPerson.getBuilding();
		dr.getOrderStand().addOrder(myCustomer.choice, this, myCustomer.table);
	}

	private void deliverFood(MyCustomer myCustomer) {
		myCustomer.event = CookEvent.None;
		DoDeliverFood(myCustomer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myCustomer.customer.msgHereIsYourFood(myCustomer.choice);
		myCustomer.cashierEvent = CashierEvent.GetBill;
	}

	private void getBill(MyCustomer myCustomer) {
		myCustomer.cashierEvent = CashierEvent.None;
		waiterGui.DoGetBill();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cashier.msgGetBill(this, myCustomer.customer, myCustomer.choice);
		try {
			getBill.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void giveCustomerBill(MyCustomer myCustomer) {
		waiterGui.DoGoToTable(myCustomer.table, "deliveringBill");
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myCustomer.cashierEvent = CashierEvent.None;
		myCustomer.customer.msgHereIsYourBill(myCustomer.dues);
	}

	private void leaveRestaurant() {
		/*
		waiterGui.DoLeaveRestaurant();

		try {
			leftRestaurant.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
*/
		waiterGui.setPresent(false);
		DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) myPerson.building
				.getPanel();
		ap.removeGui(waiterGui);

		B_DannyRestaurant rest = (B_DannyRestaurant) myPerson.getBuilding();
		rest.numWaiters--;

		myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
		exitBuilding(myPerson);
		workOver = false;
	}

	private void askHostForBreak() {
		if (host != null) {
			//host.msgWantToGoOnBreak(this);
			event = WaiterEvent.None;
		} else {
			print("No host has been assigned. Cannot request to go off break.");
			state = WaiterState.Available;
			event = WaiterEvent.BreakDenied;
			// TODO getGui().gui.restPanel.enableOnBreak(this);
		}
	}

	private void DoBreakDenied() {
		event = WaiterEvent.None;
		// TODO getGui().gui.restPanel.removeWaiterOnBreakSelection(this);
	}

	private void goOnBreak() {
		Do(AlertTag.DannyRest, "Going On Break");
		state = WaiterState.OnBreak;
		//host.msgGoingOnBreak(this);
		// TODO getGui().gui.restPanel.enableOnBreak(this);
	}

	private void goOffBreak() {
		Do(AlertTag.DannyRest, "Going Off Break");
		//host.msgGoingOffBreak(this);
		event = WaiterEvent.None;
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(MyCustomer myCustomer) {
		print("DoSeatCustomer");
		waiterGui.DoGetCustomer();
		try {
			seatingCustomer.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		print("Seating " + myCustomer.customer + " at " + myCustomer.table);
		waiterGui.DoBringToTable(myCustomer.table);
	}

	private void DoGoToTable(MyCustomer myCustomer) {
		print("Going to table " + myCustomer.table);
		waiterGui.DoGoToTable(myCustomer.table, "customerReady");
	}

	private void DoTellCookOrder(MyCustomer myCustomer) {
		waiterGui.DoGoToCook();
		waiterGui.DoPrintProcessingFood(myCustomer.choice);
	}

	private void DoDeliverFood(MyCustomer myCustomer) {
		print("Retrieving food from cook");
		waiterGui.DoGoToCook();
		try {
			walkingToCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cook.msgGotFood(myCustomer.choice, myCustomer.table);
		print("Delivering food to table " + myCustomer.table);
		waiterGui.DoGoToTable(myCustomer.table, "deliveringFood");
		waiterGui.DoPrintDeliveringFood(myCustomer.choice);
	}

	// utilities
	public void print(String string) {
		System.out.println(string);
	}

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public void setHost(DannyHost host) {
		this.host = host;
	}

	public DannyHost getHost() {
		return host;
	}

	public void setNum(int num) {
		numWaiter = num;
	}

	public void setCashier(DannyCashier c) {
		cashier = c;
	}

	public DannyCashier getCashier() {
		return cashier;
	}

	public void setCook(DannyCook c) {
		cook = c;
	}

	public DannyCook getCook() {
		return cook;
	}

	public String getName() {
		return name;
	}

	@Override
	protected void enterBuilding() {
		WaiterGui wg = new WaiterGui(this, numWaiter);
		waiterGui = wg;
		// add gui
		DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) myPerson.building
				.getPanel();
		ap.addGui(waiterGui);
		System.out.println("Waiter: " + hashCode() + " enterBuilding");
		System.out.println("WaiterGUI " + waiterGui.hashCode()
				+ " added to Danny Rest Panel " + ap.hashCode());
	}

	@Override
	public void workOver() {
		// System.out.println("Waiter workOver");
		// B_DannyRestaurant rest = (B_DannyRestaurant) myPerson.getBuilding();
		// rest.numWaiters--;

		// waiterGui.DoLeaveRestaurant();
		/*
		 * try { leftRestaurant.acquire(); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */
		// workOver = true;
		/*
		 * waiterGui.setPresent(false); DannyRestaurantAnimationPanel ap =
		 * (DannyRestaurantAnimationPanel) myPerson.building .getPanel();
		 * ap.removeGui(waiterGui);
		 * 
		 * myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
		 * exitBuilding(myPerson);
		 */
	}

	@Override
	public String toString() {
		return "DPCW";
	}
}
