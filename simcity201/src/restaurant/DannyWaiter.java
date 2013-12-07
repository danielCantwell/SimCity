package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import restaurant.gui.WaiterGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import SimCity.Base.Role;
import agent.Agent;

/**
 * Restaurant Waiter Agent
 */
// We only have 2 types of agents in this prototype. A customer and an agent
// that
// does all the rest. Rather than calling the other agent a waiter, we called
// him
// the HostAgent. A Host is the manager of a restaurant who sees that all
// is proceeded as he wishes.
public class DannyWaiter extends Role implements Waiter {

	public List<MyCustomer> myCustomers = Collections
			.synchronizedList(new ArrayList<MyCustomer>());

	private Semaphore waitingForOrder = new Semaphore(0, true);
	private Semaphore walkingToCook = new Semaphore(0, true);
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atCashier = new Semaphore(0, true);
	private Semaphore getBill = new Semaphore(0, true);
	private Semaphore customerSeated = new Semaphore(0, true);
	private Semaphore readyToTakeOrder = new Semaphore(0, true);
	private Semaphore seatingCustomer = new Semaphore(0, true);

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

	public WaiterState state = WaiterState.Available;
	public WaiterEvent event = WaiterEvent.None;

	public DannyHost host;
	public WaiterGui waiterGui = null;
	public DannyCook cook;

	private String name;

	public DannyWaiter(String name) {
		super();
		this.name = name;
	}

	/*
	 * MESSAGES
	 */

	public void msgPleaseSeatCustomer(DannyHost host, Customer customer,
			int table) {
		print("MESSAGE 2 : Host -> Waiter : PleaseSeatCustomer");
		this.host = host;
		cook = host.getCook();
		myCustomers.add(new MyCustomer(customer, table));
		stateChanged();
	}

	public void msgReadyToOrder(Customer customer) {
		synchronized (myCustomers) {
			for (MyCustomer myCustomer : myCustomers) {
				if (myCustomer.customer == customer) {
					myCustomer.state = CustomerState.ReadyToOder;
					print("MESSAGE 4 : Customer -> Waiter : ReadyToOrder");
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
					print("MESSAGE 6 : Customer -> Waiter : HereIsMyOrder");
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
						print("MESSAGE 8 : Cook -> Waiter : OrderReady");
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
						print("MESSAGE 8 Non-Normative : Cook -> Waiter : OutOf");
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
					print("MESSAGE 11 : Cashier -> Waiter : HereIsBill");
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
					print("MESSAGE 13B : Customer -> Waiter : DoneAndPaying");
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
					print("MESSAGE 6 Non-Normative : Customer -> Waiter : CantAffordAnythingElse");
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
		getGui().gui.restPanel.disableOnBreak(this);
		stateChanged();
	}

	public void msgBreakOkay() {
		print("Break Request : ACCEPTED");
		state = WaiterState.WaitingForBreak;
		stateChanged();
	}

	public void msgBreakNotOkay() {
		print("Break Request : DENIED");
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

	// Messages from Animation
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

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {

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

	private void processOrder(MyCustomer myCustomer) {
		print("Going to tell cook order");
		DoTellCookOrder(myCustomer);
		try {
			walkingToCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myCustomer.event = CookEvent.None;
		print("Processing Order");
		//cook.startThread();
		cook.msgHereIsAnOrder(this, myCustomer.choice, myCustomer.table);
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
		waiterGui.gui.restPanel.getCashier().msgGetBill(this,
				myCustomer.customer, myCustomer.choice);
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

	private void askHostForBreak() {
		if (host != null) {
			host.msgWantToGoOnBreak(this);
			event = WaiterEvent.None;
		} else {
			print("No host has been assigned. Cannot request to go off break.");
			state = WaiterState.Available;
			event = WaiterEvent.BreakDenied;
			getGui().gui.restPanel.enableOnBreak(this);
		}
	}

	private void DoBreakDenied() {
		event = WaiterEvent.None;
		getGui().gui.restPanel.removeWaiterOnBreakSelection(this);
	}

	private void goOnBreak() {
		Do("Going On Break");
		state = WaiterState.OnBreak;
		host.msgGoingOnBreak(this);
		getGui().gui.restPanel.enableOnBreak(this);
	}

	private void goOffBreak() {
		Do("Going Off Break");
		host.msgGoingOffBreak(this);
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

	/*
	 * Helper Class
	 */
	public class MyCustomer {

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

	// utilities
	public void print(String string) {
		System.out.println(string);
	}
	
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}

	public void setHost(DannyHost host) {
		this.host = host;
	}

	public DannyHost getHost() {
		return host;
	}

	public String getName() {
		return name;
	}

	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}
}
