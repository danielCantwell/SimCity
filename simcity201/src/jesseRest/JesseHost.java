package jesseRest;

import Bank.tellerRole;
import EricRestaurant.EricHost.bankstate;
import SimCity.Base.God;
import SimCity.Base.Role;
import SimCity.Buildings.B_Bank;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

import jesseRest.JesseHost.Table;
import jesseRest.gui.WaiterGui;

/**
 * Restaurant Host Agent
 */

public class JesseHost extends Role {
	public List<JesseCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<JesseCustomer>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public Collection<Table> tables;
	private String name;
	private Money JRestMoney;
	tellerRole bm;
	B_Bank bank;
	public int JAccNum;
	private int NTABLES = 3;
	private int MAXTABLES = 7;
	private int nextWaiter = 0;
	public enum WaiterState {Normal, WantsBreak};
	public enum bankstate {none, gotManager, sent, noTeller};
	private bankstate bs = bankstate.none;

	public JesseHost(String name) {
		super();
		this.name = name;
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
	}
	
	public void addWaiter(JesseAbstractWaiter w) {
		MyWaiter mw = new MyWaiter(w);
		waiters.add(mw);
		//mw.waiter.startThread();
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
	
	public void setMoney(Money m) {
		JRestMoney = m;
	}
	public void setAcc(int acc) {
		JAccNum = acc;
	}
	public Money getMoney() {
		return JRestMoney;
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
		JesseCustomer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(JesseCustomer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		JesseCustomer getOccupant() {
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
		public List<JesseCustomer> customers = new ArrayList<JesseCustomer>();
		public JesseAbstractWaiter waiter;
		public WaiterState s;
		
		MyWaiter(JesseAbstractWaiter w) {
			waiter = w;
			s = WaiterState.Normal;
		}
	}
	
	/**
	 * MESSAGES =====================================================
	 */

	public void msgIWantToEat(JesseCustomer cust) {
		// Handles new Customer and puts it onto waiting list
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgTableIsFree(Table table) {
		// Frees table when customer leaves
		synchronized(waiters){
			for (MyWaiter mw : waiters) {
				for (JesseCustomer c : mw.customers) {
					if (c == table.occupiedBy) {
						table.setUnoccupied();
						mw.customers.remove(c);
					}
				}
				
				// Waiter can go on break if it has no customers left.
				if (mw.s == WaiterState.WantsBreak && mw.customers.size() == 0)  {
					mw.s = WaiterState.Normal;
					((JesseWaiter) mw.waiter).msgGoOnBreak(true);
				}
			}
		}
		stateChanged();
	}
	
	public void msgCanIGoOnBreak(JesseAbstractWaiter w) {
		synchronized(waiters){
			for (MyWaiter mw : waiters) {
				if (mw.waiter == w) {
					// Accept the break request!
					if (waiters.size() > 1) {
						print("Waiter wants to go on break. Permission granted.");
						mw.s = WaiterState.WantsBreak;
						if (mw.customers.size() == 0) {
							mw.s = WaiterState.Normal;
							((JesseWaiter) mw.waiter).msgGoOnBreak(true);
						}
					// Deny the break request!
					} else {
						print("Waiter wants to go on break. Permission denied - only one waiter.");
						((JesseWaiter) mw.waiter).msgGoOnBreak(false);
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
		//System.out.println("The bankstate is : "+bs+" and the cust size is : "+waitingCustomers.size()+" and the time is "+God.Get().getHour());
		if(bs == bankstate.gotManager && waitingCustomers.isEmpty() && God.Get().getHour()==11) {
			msgBank();
			return true;
		}
		return false;
	}

	/**
	 * ACTIONS  ====================================================
	 */
	public void msgBank() {
		bank = (B_Bank) God.Get().getBuilding(2);
		bm = (tellerRole) bank.getOneTeller();
		if(bm == null) {
			bs = bankstate.noTeller;
		}
		else {
		bm.restMoney(JAccNum, JRestMoney, this);
		bs = bankstate.sent;
		}
	}
	
	void seatCustomer(JesseCustomer c, Table t, JesseAbstractWaiter w) {
		Do(AlertTag.JesseRest, "Message 2: Sending SitAtTable from Host to Waiter.");
		((JesseWaiter) w).msgSitAtTable(c, t);
		waitingCustomers.remove(0);
		t.setOccupant(c);
	}
	public void print(String string) {
		System.out.println(string);
	}
	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "JesseHost";
	}

	public void setBM() {
		bs = bankstate.gotManager;
		stateChanged();		
	}

	public void transDone(Money m) {
		JRestMoney = m;
		System.out.println("Host finished bank interaction and JRest has : $"+JRestMoney.getDollar());		
	}
}

