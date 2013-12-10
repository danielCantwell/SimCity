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
	
	public abstract void msgLeaveRestaurant();	

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
