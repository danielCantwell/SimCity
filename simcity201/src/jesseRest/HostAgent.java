package jesseRest;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

import jesseRest.HostAgent.Table;
import jesseRest.MarketAgent.DeliveryState;
import jesseRest.gui.WaiterGui;

/**
 * Restaurant Host Agent
 */

public class HostAgent extends Agent {
	public List<CustomerAgent> waitingCustomers = Collections.synchronizedList(new ArrayList<CustomerAgent>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public Collection<Table> tables;
	private String name;
	private int NTABLES = 3;
	private int MAXTABLES = 7;
	private int nextWaiter = 0;
	public enum WaiterState {Normal, WantsBreak};
		
	public HostAgent(String name) {
		super();
		this.name = name;
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
	}
	
	public void addWaiter(WaiterAgent w) {
		MyWaiter mw = new MyWaiter(w);
		waiters.add(mw);
		mw.waiter.startThread();
		stateChanged();
	}
	
	public void addTable() {
		if (NTABLES < MAXTABLES) {
			NTABLES++;
			tables.add(new Table(NTABLES));
			stateChanged();
			print("Adding a new table. Total tables - " + NTABLES);
		} else {
			print("Unable to add more tables.");
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
	
	public class Table {
		CustomerAgent occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
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
	
	public class MyWaiter {
		public List<CustomerAgent> customers = new ArrayList<CustomerAgent>();
		public WaiterAgent waiter;
		public WaiterState s;
		
		MyWaiter(WaiterAgent w) {
			waiter = w;
			s = WaiterState.Normal;
		}
	}
	
	/**
	 * MESSAGES =====================================================
	 */

	public void msgIWantToEat(CustomerAgent cust) {
		// Handles new Customer and puts it onto waiting list
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgTableIsFree(Table table) {
		// Frees table when customer leaves
		synchronized(waiters){
			for (MyWaiter mw : waiters) {
				for (CustomerAgent c : mw.customers) {
					if (c == table.occupiedBy) {
						table.setUnoccupied();
						mw.customers.remove(c);
					}
				}
				
				// Waiter can go on break if it has no customers left.
				if (mw.s == WaiterState.WantsBreak && mw.customers.size() == 0)  {
					mw.s = WaiterState.Normal;
					mw.waiter.msgGoOnBreak(true);
				}
			}
		}
		stateChanged();
	}
	
	public void msgCanIGoOnBreak(WaiterAgent w) {
		synchronized(waiters){
			for (MyWaiter mw : waiters) {
				if (mw.waiter == w) {
					// Accept the break request!
					if (waiters.size() > 1) {
						print("Waiter wants to go on break. Permission granted.");
						mw.s = WaiterState.WantsBreak;
						if (mw.customers.size() == 0) {
							mw.s = WaiterState.Normal;
							mw.waiter.msgGoOnBreak(true);
						}
					// Deny the break request!
					} else {
						print("Waiter wants to go on break. Permission denied - only one waiter.");
						mw.waiter.msgGoOnBreak(false);
					}
				}
			}
		}
	}

	/**
	 * SCHEDULER ====================================================
	 */
	
	public boolean pickAndExecuteAnAction() {
		// Properly assigns Customers when a table becomes free and a waiter is known
		for (Table table : tables) {
			if (!table.isOccupied()) {
				// Load balancing waiters
				if (!waitingCustomers.isEmpty() && !waiters.isEmpty()) {
					waiters.get(nextWaiter).customers.add(waitingCustomers.get(0));
					seatCustomer(waitingCustomers.get(0), table, waiters.get(nextWaiter).waiter);
					nextWaiter++;
					if (nextWaiter == waiters.size()) { 
						nextWaiter = 0;
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * ACTIONS  ====================================================
	 */
	
	void seatCustomer(CustomerAgent c, Table t, WaiterAgent w) {
		Do("Message 2: Sending SitAtTable from Host to Waiter.");
		w.msgSitAtTable(c, t);
		waitingCustomers.remove(0);
		t.setOccupant(c);
	}
}

