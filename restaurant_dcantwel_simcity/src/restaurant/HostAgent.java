package restaurant;

import agent.Agent;
import restaurant.WaiterAgent.WaiterEvent;
import restaurant.WaiterAgent.WaiterState;
import restaurant.gui.RestaurantPanel;
import restaurant.gui.WaiterGui;

import java.util.*;

/**
 * Restaurant Host Agent
 */
// We only have 2 types of agents in this prototype. A customer and an agent
// that
// does all the rest. Rather than calling the other agent a waiter, we called
// him
// the HostAgent. A Host is the manager of a restaurant who sees that all
// is proceeded as he wishes.
public class HostAgent extends Agent {
	public static final int NTABLES = 4;// a global for the number of tables.
	// Notice that we implement waitingCustomers using ArrayList, but type it
	// with List semantics.
	public List<CustomerAgent> waitingCustomers = Collections
			.synchronizedList(new ArrayList<CustomerAgent>());
	public List<MyWaiter> waiters = Collections
			.synchronizedList(new ArrayList<MyWaiter>());
	public Collection<Table> tables;
	// note that tables is typed with Collection semantics.
	// Later we will see how it is implemented

	private CookAgent cook = new CookAgent("Chef", this);
	private RestaurantPanel restPanel;

	private String name;

	public WaiterGui waiterGui = null;

	public HostAgent(String name, RestaurantPanel rest) {
		super();

		this.name = name;
		this.restPanel = rest;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		synchronized (tables) {
			for (int ix = 1; ix <= NTABLES; ix++) {
				tables.add(new Table(ix));// how you add to a collections
			}
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}

	// Messages

	public void msgIWantFood(CustomerAgent cust) {
		print("MESSAGE 1 : Customer -> Host : IWantFood");
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgWantToGoOnBreak(WaiterAgent waiter) {
		print("Waiter " + waiter.getName() + " is requesting to go on break");
		synchronized (waiters) {
			for (MyWaiter myWaiter : waiters) {
				if (myWaiter.waiter == waiter) {
					myWaiter.event = WaiterEvent.RequestingBreak;
					stateChanged();
				}
			}
		}
	}

	public void msgGoingOnBreak(WaiterAgent waiter) {
		print("Waiter " + waiter.getName() + " is going on break");
		synchronized (waiters) {
			for (MyWaiter myWaiter : waiters) {
				if (myWaiter.waiter == waiter) {
					myWaiter.state = WaiterState.OnBreak;
					stateChanged();
				}
			}
		}
	}

	public void msgGoingOffBreak(WaiterAgent waiter) {
		print("Waiter " + waiter.getName() + " is going off break");
		synchronized (waiters) {
			for (MyWaiter myWaiter : waiters) {
				if (myWaiter.waiter == waiter) {
					myWaiter.event = WaiterEvent.BreakOver;
					stateChanged();
				}
			}
		}
	}

	public void msgTableFree(int t) {
		synchronized (tables) {
			for (Table table : tables) {
				if (table.tableNumber == t) {
					print("MESSAGE 14B : Waiter -> Host : TableFree");
					table.setUnoccupied();
					stateChanged();
				}
			}
		}
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/*
		 * Think of this next rule as: Does there exist a table, customer and
		 * waiter, so that table is unoccupied, the customer is waiting, and
		 * there is an available waiter. If so, tell the waiter to seat that
		 * customer
		 */
		synchronized (tables) {
			for (Table table : tables) {
				if (!table.isOccupied()) {
					if (!waitingCustomers.isEmpty()) {
						synchronized (waiters) {
							for (MyWaiter myWaiter : waiters) {
								if (myWaiter.state == WaiterState.Available) {
									seatCustomer(waitingCustomers.get(0),
											table, myWaiter.waiter);
									switchOrderOfWaiters();
									return true;
								}
							}
						}
					}
				}
			}
		}

		synchronized (waiters) {
			for (MyWaiter myWaiter : waiters) {
				if (myWaiter.state == WaiterState.Available
						&& myWaiter.event == WaiterEvent.RequestingBreak) {
					replyToBreakRequest(myWaiter);
					return true;
				}
			}
		}

		synchronized (waiters) {
			for (MyWaiter myWaiter : waiters) {
				if (myWaiter.state == WaiterState.OnBreak
						&& myWaiter.event == WaiterEvent.BreakOver) {
					waiterBreakIsOver(myWaiter);
					return true;
				}
			}
		}

		return false;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

	// Actions

	private void seatCustomer(CustomerAgent customer, Table table,
			WaiterAgent waiter) {
		customer.setWaiter(waiter);
		table.setOccupant(customer);
		waiter.msgPleaseSeatCustomer(this, customer, table.tableNumber);
		waitingCustomers.remove(customer);
	}

	private void replyToBreakRequest(MyWaiter myWaiter) {
		boolean waiterAvailable = false;
		synchronized (waiters) {
			for (MyWaiter waiter : waiters) {
				if (waiter != myWaiter) {
					if (waiter.state == WaiterState.Available) {
						waiterAvailable = true;
					}
				}
			}
		}
		
		if (waiters.size() > 1 && waiterAvailable) {
			myWaiter.waiter.msgBreakOkay();
			myWaiter.state = WaiterState.WaitingForBreak;
			myWaiter.event = WaiterEvent.None;
		} else {
			myWaiter.waiter.msgBreakNotOkay();
			myWaiter.state = WaiterState.Available;
			myWaiter.event = WaiterEvent.None;
		}
		stateChanged();
	}

	private void waiterBreakIsOver(MyWaiter myWaiter) {
		myWaiter.state = WaiterState.Available;
		myWaiter.event = WaiterEvent.None;
		myWaiter.waiter.msgBreakOver();
		stateChanged();
	}

	// utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public void addWaiter(WaiterAgent w) {
		waiters.add(new MyWaiter(w));
		w.startThread();
		print("Added waiter " + w.getName());
		stateChanged();
	}

	public CookAgent getCook() {
		return cook;
	}
	
	public CashierAgent getCashier() {
		return restPanel.getCashier();
	}

	private void switchOrderOfWaiters() {

		MyWaiter first = waiters.get(0);
		waiters.remove(0);
		waiters.add(first);
	}

	/*
	 * public HostGui getGui() { return hostGui; }
	 */
	public class MyWaiter {
		WaiterAgent waiter;
		WaiterState state = WaiterState.Available;
		WaiterEvent event = WaiterEvent.None;

		MyWaiter(WaiterAgent waiter) {
			this.waiter = waiter;
		}
	}

	public class Table {
		CustomerAgent occupiedBy;
		int tableNumber;

		public int xCoord = 0;
		public int yCoord = 0;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;

			if (tableNumber == 1) {
				xCoord = 80;
				yCoord = 100;
			} else if (tableNumber == 2) {
				xCoord = 260;
				yCoord = 100;
			} else if (tableNumber == 3) {
				xCoord = 80;
				yCoord = 260;
			} else if (tableNumber == 4) {
				xCoord = 260;
				yCoord = 260;
			}
		}

		void setOccupant(CustomerAgent cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerAgent getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
}
