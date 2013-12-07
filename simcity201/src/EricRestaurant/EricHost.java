package EricRestaurant;

import EricRestaurant.EricCustomer.AgentState;
import EricRestaurant.gui.HostGui;
import EricRestaurant.interfaces.Customer;
import SimCity.Base.Role;
import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class EricHost extends Role {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<EricCustomer> waitingCustomers
	= new ArrayList<EricCustomer>();
	public List<Waiter> waiters = new ArrayList<Waiter>();
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public int check = 0;
	static int fullcheck = 0;
	int wCount = 1;
	private EricRestaurant.interfaces.Waiter waiter;
	public EricCook cook;
	public EricCashier cashier;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	public class Waiter {
		EricRestaurant.interfaces.Waiter w;
		state s;
		int num;
		breakstate b;
	}
	public enum state {none, free, busy};
	private state s = state.none;//The start state
	public enum breakstate {onbreak, offbreak};
	private breakstate b = breakstate.offbreak;//The start state
	public HostGui hostGui = null;

	public EricHost(String name) {
		super();
		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}
	public void setWaiter(EricRestaurant.interfaces.Waiter w) {
		waiter = w;
	}

	public void setCook(EricCook ck) {
		cook = ck;
	}
	public void setCashier(EricCashier cash) {
		cashier = cash;
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

	public void msgIWantFood(EricCustomer cust, double c) {
		Random generator = new Random(); 
		if (fullcheck > 2) {
			int i = generator.nextInt(3);
			if(i >= 1) {
				cust.leaveTable();
				print("-------------------Customer decided to leave because restaurant was full-------------------");
			}
		}

		if (c > 0) {
			waitingCustomers.add(cust);
			stateChanged();
		}
		else {
			print("You do not have enough money to buy anything");
			cust.leaveTable();
		}
	}

	public void msgLeavingTable(Customer cust, EricRestaurant.interfaces.Waiter w) {
		for (Waiter mw : waiters) {
			if(mw.w == w) {
				mw.s = state.free;
			}
		}
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				fullcheck--;
			}
		}

		stateChanged();
	}

	public void newWaiter(EricRestaurant.interfaces.Waiter wait) {
		Waiter mywaiter = new Waiter();
		mywaiter.w = wait;
		mywaiter.s = state.free;
		mywaiter.b = breakstate.offbreak;
		waiters.add(mywaiter);
		wCount++;
		wait.getGui().waiting(wCount);
		stateChanged();
	}

	public void canIBreak(EricRestaurant.interfaces.Waiter w) {
		checkBreak(w);	
	}

	public void backFromBreak(EricRestaurant.interfaces.Waiter w) {
		waiterBack(w);
	}

	//	public void msgAtTable() {//from animation
	//		//print("msgAtTable() called");
	//		atTable.release();// = true;
	//		stateChanged();
	//	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		int count = 0;
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		for(Waiter w : waiters) {
			if(w.b == breakstate.offbreak) {
				count++;
			}
		}
		if(waiters.size() > 1) {
			for(Waiter w : waiters) {
				if (w.s == state.free && w.b == breakstate.offbreak) {
					for (Table table : tables) {
						if (!table.isOccupied()) {
							if (!waitingCustomers.isEmpty()) {
								callWaiter(waitingCustomers.get(0), table, w);
								return true;//return true to the abstract agent to reinvoke the scheduler.
							}
						}
					}
				}
			}
		}
		if(waiters.size() == 1 || count == 1) {
			for(Waiter w : waiters) {
				if (w.b == breakstate.offbreak) {
					for (Table table : tables) {
						if (!table.isOccupied()) {
							if (!waitingCustomers.isEmpty()) {
								//seatCustomer(waitingCustomers.get(0), table);//the action
								callWaiter(waitingCustomers.get(0), table, w);
								return true;//return true to the abstract agent to reinvoke the scheduler.
							}
						}
					}
				}
			}
		}
		//		else System.out.println("There is no Waiter available yet, please add one!");
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	//	private void seatCustomer(CustomerAgent customer, Table table) {
	//		customer.msgSitAtTable();
	//		DoSeatCustomer(customer, table);
	//		try {
	//			atTable.acquire();
	//		} catch (InterruptedException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		table.setOccupant(customer);
	//		waitingCustomers.remove(customer);
	//		hostGui.DoLeaveCustomer();
	//	}

	private void callWaiter(EricCustomer cust, Table table, Waiter w) {
		table.setOccupant(cust);
		w.w.HostCalled(cust, table.tableNumber, wCount);
		waitingCustomers.remove(cust);
		fullcheck++;
		w.s = state.busy;
	}

	private void checkBreak(EricRestaurant.interfaces.Waiter w) {
		check = 0;
		for(Waiter myw : waiters) {
			if(myw.b == breakstate.offbreak) {
				check++;
			}
		}
		if(waiters.size() > 1 && check > 1) {
			for (Waiter mw : waiters) {
				if(mw.w == w){
					mw.b = breakstate.onbreak;
					print(w.getName()+": can take a break.");
				}
			}
		}
		else print("Only 1 waiter, can't go on break");
	}

	private void print(String string) {
		System.out.println(string);
	}
	private void waiterBack(EricRestaurant.interfaces.Waiter w) {
		for(Waiter mw : waiters) {
			if(mw.w == w && mw.b == breakstate.onbreak) {
				mw.b = breakstate.offbreak;
				print(w.getName()+": is back from break.");
			}
		}
	}
	// The animation DoXYZ() routines
	//	private void DoSeatCustomer(CustomerAgent customer, Table table) {
	//		//Notice how we print "customer" directly. It's toString method will do it.
	//		//Same with "table"
	//		print("Seating " + customer + " at " + table);
	//		hostGui.DoBringToTable(customer); 
	//
	//	}

	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	private class Table {
		Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
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

