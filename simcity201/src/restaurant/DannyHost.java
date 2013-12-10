package restaurant;

import Bank.tellerRole;
import EricRestaurant.EricHost.bankstate;
import EricRestaurant.interfaces.Host;
import SimCity.Base.God;
import SimCity.Base.Role;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_DannyRestaurant;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import restaurant.DannyAbstractWaiter.WaiterEvent;
import restaurant.DannyAbstractWaiter.WaiterState;
import restaurant.gui.DannyRestaurantAnimationPanel;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Waiter;

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
public class DannyHost extends Role {
	public enum bankstate {none, gotManager, sent, noTeller};
	private bankstate bs = bankstate.none;
	Money DRestMoney;
	public int DAccNum;
	B_Bank bank;
	tellerRole bm;
	public static final int NTABLES = 4;// a global for the number of tables.
	// Notice that we implement waitingCustomers using ArrayList, but type it
	// with List semantics.
	public List<DannyCustomer> waitingCustomers = Collections
			.synchronizedList(new ArrayList<DannyCustomer>());
	public List<MyWaiter> waiters = Collections
			.synchronizedList(new ArrayList<MyWaiter>());
	public Collection<Table> tables = new ArrayList<Table>();
	// note that tables is typed with Collection semantics.
	// Later we will see how it is implemented

	private String name;

	private boolean workOver = false;

	public WaiterGui waiterGui = null;

	public DannyHost() {
		super();
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

	public void setName(String name) {
		this.name = name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}

	// Messages

	public void msgIWantFood(DannyCustomer cust) {
		print("MESSAGE 1 : Customer -> Host : IWantFood");
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgWantToGoOnBreak(DannyAbstractWaiter waiter) {
		print("Waiter " + ((Host) waiter).getName() + " is requesting to go on break");
		synchronized (waiters) {
			for (MyWaiter myWaiter : waiters) {
				if (myWaiter.waiter == waiter) {
					myWaiter.event = WaiterEvent.RequestingBreak;
					stateChanged();
				}
			}
		}
	}

	public void msgGoingOnBreak(DannyAbstractWaiter waiter) {
		print("Waiter " + ((Host) waiter).getName() + " is going on break");
		synchronized (waiters) {
			for (MyWaiter myWaiter : waiters) {
				if (myWaiter.waiter == waiter) {
					myWaiter.state = WaiterState.OnBreak;
					stateChanged();
				}
			}
		}
	}

	public void msgGoingOffBreak(DannyAbstractWaiter waiter) {
		print("Waiter " + ((Host) waiter).getName() + " is going off break");
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
	
	public void msgCustomerLeaving() {
		stateChanged();
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		
		if (workOver) {
			leaveRestaurant();
			return true;
		}
		
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
		if(bs == bankstate.gotManager && waitingCustomers.isEmpty() && God.Get().getHour()==11) {
			msgBank();
			return true;
		}
		if (workOver) {
			leaveRestaurant();
			return true;
		}

		return false;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

	// Actions
	public void msgBank() {
		bank = (B_Bank) God.Get().getBuilding(2);
		bm = (tellerRole) bank.getOneTeller();
		if(bm == null) {
			bs = bankstate.noTeller;
		}
		else {
		bm.restMoney(DAccNum, DRestMoney, this);
		bs = bankstate.sent;
		}
	}
	
	public void transDone(Money m) {
		DRestMoney = m;
		System.out.println("Host finished bank interaction and DRest has : $"+DRestMoney.getDollar());
	}
	
	private void seatCustomer(DannyCustomer customer, Table table,
			DannyAbstractWaiter waiter) {
		System.out.println("Danny Host -- Seat Customer");
		customer.setWaiter(waiter);
		table.setOccupant(customer);
		waiter.msgPleaseSeatCustomer(customer, table.tableNumber);
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

	private void leaveRestaurant() {
		B_DannyRestaurant rest = (B_DannyRestaurant) myPerson.getBuilding();
		if (rest.numCustomers == 0) {
			Do(AlertTag.DannyRest, "Leaving Restaurant");
			// msg all waiters that they are allowed to leave
			for (MyWaiter w : waiters) {
				w.waiter.msgLeaveRestaurant();
			}
			DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) myPerson.building
					.getPanel();
			ap.hostPresent = false;
			
			rest.cashierRole.msgLeaveRestaurant();
			rest.cookRole.msgLeaveRestaurant();
			myPerson.msgGoHome();
			exitBuilding(myPerson);
			workOver = false;
		}
	}

	// utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public void addWaiter(DannyAbstractWaiter w) {
		waiters.add(new MyWaiter(w));
		// w.startThread();
		stateChanged();
	}

	public DannyCashier getCashier() {
		B_DannyRestaurant dr = (B_DannyRestaurant) myPerson.getBuilding();
		return dr.getCashier();
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
		DannyAbstractWaiter waiter;
		WaiterState state = WaiterState.Available;
		WaiterEvent event = WaiterEvent.None;

		MyWaiter(DannyAbstractWaiter waiter) {
			this.waiter = waiter;
		}
	}

	public class Table {
		DannyCustomer occupiedBy;
		int tableNumber;

		public int xCoord = 0;
		public int yCoord = 0;

		public Table(int tableNumber) {
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

		void setOccupant(DannyCustomer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		DannyCustomer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}

	public void print(String string) {
		System.out.println(string);
	}

	@Override
	protected void enterBuilding() {
		DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) myPerson.building
				.getPanel();
		ap.hostPresent = true;
		System.out.println("Host enterBuilding");
	}

	@Override
	public void workOver() {/*
							 * System.out.println("Host workOver");
							 * B_DannyRestaurant rest =
							 * (B_DannyRestaurant)myPerson.getBuilding();
							 * rest.hostFilled = false; rest.setOpen(false);
							 * 
							 * exitBuilding(myPerson);
							 */
		workOver = true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Danny Host";
	}

	public void setBM() {
		bs = bankstate.gotManager;
		stateChanged();		
	}

	public void setMoney(Money dRestMoney) {
		DRestMoney = dRestMoney;
	}
	
	public Money getMoney() {
		return DRestMoney;
	}
	
	public void setAcc(int acc) {
		DAccNum = acc;
		System.out.println("Host got Danny Resturant Acc Num: "+DAccNum);
	}
}
