/**
 * 
 */
package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import SimCity.Base.Role;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * @author Daniel
 * 
 */
public abstract class DannyAbstractWaiter extends Role implements Waiter {

	public List<MyCustomer> myCustomers = Collections
			.synchronizedList(new ArrayList<MyCustomer>());

	protected Semaphore waitingForOrder = new Semaphore(0, true);
	protected Semaphore walkingToCook = new Semaphore(0, true);
	protected Semaphore atTable = new Semaphore(0, true);
	protected Semaphore atCashier = new Semaphore(0, true);
	protected Semaphore getBill = new Semaphore(0, true);
	protected Semaphore customerSeated = new Semaphore(0, true);
	protected Semaphore readyToTakeOrder = new Semaphore(0, true);
	protected Semaphore seatingCustomer = new Semaphore(0, true);
	protected Semaphore leftRestaurant = new Semaphore(0, true);

	public WaiterState state = WaiterState.Available;
	public WaiterEvent event = WaiterEvent.None;

	public DannyHost host;
	public WaiterGui waiterGui = null;
	public DannyCook cook;
	public DannyCashier cashier;
	public int numWaiter;

	protected String name;
	protected boolean workOver = false;

	public void msgWorkOver() {
		System.out.println("Waiter workOver");
		workOver = true;
	}


	public void msgPleaseSeatCustomer(Customer customer, int table) {
		//print("MESSAGE 2 : Host -> Waiter : PleaseSeatCustomer");
		myCustomers.add(new MyCustomer(customer, table));
		stateChanged();
	}

	public void msgReadyToOrder(Customer customer) {
		synchronized (myCustomers) {
			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.customer == customer) {
					myCustomer.state = CustomerState.ReadyToOder;
					//print("MESSAGE 4 : Customer -> Waiter : ReadyToOrder");
					stateChanged();
				}
			}
		}
	}

	public void msgHereIsMyOrder(String choice, Customer customer) {
		synchronized (myCustomers) {
			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.customer == customer) {
					waitingForOrder.release();
					//print("MESSAGE 6 : Customer -> Waiter : HereIsMyOrder");
					myCustomer.choice = choice;
					myCustomer.state = CustomerState.WaitingForOrder;
					myCustomer.event = CookEvent.DeliveringOrder;
					stateChanged();
				}
			}
		}
	}

	public void msgOrderReady(String choice, int table) {
		synchronized (myCustomers) {
			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.choice == choice) {
					if (myCustomer.table == table) {
						//print("MESSAGE 8 : Cook -> Waiter : OrderReady");
						myCustomer.event = CookEvent.OrderReady;
						stateChanged();
					}
				}
			}
		}
	}

	public void msgOutOf(String choice, int table) {
		synchronized (myCustomers) {
			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.choice == choice) {
					if (myCustomer.table == table) {
						//print("MESSAGE 8 Non-Normative : Cook -> Waiter : OutOf");
						myCustomer.event = CookEvent.OutOfStock;
						stateChanged();
					}
				}
			}
		}
	}

	public void msgHereIsBill(Customer customer, double cost) {
		synchronized (myCustomers) {
			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.customer == customer) {
					getBill.release();
					//print("MESSAGE 11 : Cashier -> Waiter : HereIsBill");
					myCustomer.cashierEvent = CashierEvent.BillReady;
					myCustomer.dues = cost;
					stateChanged();
				}
			}
		}
	}

	public void msgDoneAndPaying(Customer customer) {
		synchronized (myCustomers) {
			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.customer == customer) {
					//print("MESSAGE 13B : Customer -> Waiter : DoneAndPaying");
					host.msgTableFree(myCustomer.table);
					myCustomers.remove(myCustomer);
					stateChanged();
					break;
				}
			}
		}
	}

	public void msgCantAffordAnythingElse(Customer customer) {
		waitingForOrder.release();
		synchronized (myCustomers) {
			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.customer == customer) {
					//print("MESSAGE 6 Non-Normative : Customer -> Waiter : CantAffordAnythingElse");
					host.msgTableFree(myCustomer.table);
					myCustomers.remove(myCustomer);
					stateChanged();
					break;
				}
			}
		}
	}

	// Go on/off break messages

	public void msgWantToGoOnBreak() {
		event = WaiterEvent.RequestingBreak;
		// TODO getGui().gui.restPanel.disableOnBreak(this);
		stateChanged();
	}

	public void msgBreakOkay() {
		//print("Break Request : ACCEPTED");
		state = WaiterState.WaitingForBreak;
		stateChanged();
	}

	public void msgBreakNotOkay() {
		//print("Break Request : DENIED");
		state = WaiterState.Available;
		event = WaiterEvent.BreakDenied;
		stateChanged();
	}

	public void msgWantToGoOffBreak() {
		event = WaiterEvent.BreakOver;
		stateChanged();
	}

	public void msgBreakOver() {
		state = WaiterState.Available;
		stateChanged();
	}

	public void msgAtTable() {
		atTable.release();
		stateChanged();
	}

	public void msgAtCook() {
		walkingToCook.release();
		stateChanged();
	}

	public void msgAtCashier() {
		atCashier.release();
		stateChanged();
	}

	public void msgGotCustomer() {
		seatingCustomer.release();
		stateChanged();
	}

	public void msgCustomerSeated() {
		customerSeated.release();
		stateChanged();
	}

	public void msgReadyToTakeOrder() {
		readyToTakeOrder.release();
		stateChanged();
	}

	public void msgLeaveRestaurant() {
		leftRestaurant.release();
		stateChanged();
	}

	protected abstract void processOrder(MyCustomer myCustomer);

	enum CustomerState {
		Waiting, Seated, ReadyToOder, AskedToOrder, WaitingForOrder, Paying
	};

	enum CookEvent {
		None, DeliveringOrder, OrderReady, OutOfStock
	};

	enum CashierEvent {
		None, GetBill, BillReady
	};

	enum WaiterState {
		Available, WaitingForBreak, OnBreak
	};

	enum WaiterEvent {
		None, RequestingBreak, BreakDenied, BreakOver
	};

	/*
	 * Helper Class
	 */
	protected class MyCustomer {

		Customer customer;
		int table;
		String choice;
		double dues;

		CustomerState state = CustomerState.Waiting;
		CookEvent event = CookEvent.None;
		CashierEvent cashierEvent = CashierEvent.None;

		MyCustomer(Customer customer, int table) {
			this.customer = customer;
			this.table = table;
		}
	}
}
