package EricRestaurant;

import Bank.bankManagerRole;
import Bank.tellerRole;
import EricRestaurant.gui.HostGui;
import EricRestaurant.interfaces.*;
import SimCity.Base.God;
import SimCity.Base.Role;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_EricRestaurant;
import SimCity.Globals.Money;

import java.util.*;
import java.util.concurrent.Semaphore;

import timRest.TimHostRole.bankstate;

/**
 * Restaurant Host Agent
 */

public class EricHost extends Role implements Host {
	static final int NTABLES = 3;//a global for the number of tables.
	public int ERestNum;
	public tellerRole bm;
	public List<EricCustomer> waitingCustomers
	= new ArrayList<EricCustomer>();
	public List<Waiter> waiters = new ArrayList<Waiter>();
	public Collection<Table> tables;
	public int check = 0;
	static int fullcheck = 0;
	int wCount = 1;
	public Money ERestMoney;
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
	B_Bank bank;
	public enum state {none, free, busy};
	private state s = state.none;//The start state
	public enum breakstate {onbreak, offbreak};
	public enum bankstate {none, gotManager, sent, noTeller};
	private bankstate bs = bankstate.none;
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

	public void setBM() {
		bs = bankstate.gotManager;
		stateChanged();
	}
	
	public void setMoney(Money m) {
		ERestMoney = m;
	}
	
	public Money getMoney(){
		return ERestMoney;
	}
	@Override
	public void setWaiter(EricAbstractWaiter w) {
		waiter = w;
	}

	@Override
	public void setCook(EricCook ck) {
		cook = ck;
	}

	@Override
	public void setCashier(EricCashier cash) {
		cashier = cash;
	}

	@Override
	public String getMaitreDName() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	@Override
	public Collection getTables() {
		return tables;
	}

	@Override
	public void msgIWantFood(EricCustomer cust, Money c) {
		Random generator = new Random(); 
		if (fullcheck > 2) {
			int i = generator.nextInt(3);
			if(i >= 1) {
				cust.leaveTable();
				print("-------------------Customer decided to leave because restaurant was full-------------------");
			}
		}
		if (!(c.isZero())) {
			waitingCustomers.add(cust);
			stateChanged();
		}
		else {
			print("You do not have enough money to buy anything");
			cust.leaveTable();
		}
	}

	@Override
	public void msgLeavingTable(Customer cust, EricAbstractWaiter w) {
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

	public void newWaiter(EricAbstractWaiter wait) {
		Waiter mywaiter = new Waiter();
		mywaiter.w = wait;
		mywaiter.s = state.free;
		mywaiter.b = breakstate.offbreak;
		waiters.add(mywaiter);
		wCount++;
		//wait.getGui().waiting(wCount);
		stateChanged();
	}

	@Override
	public void canIBreak(EricAbstractWaiter w) {
		checkBreak(w);	
	}

	@Override
	public void backFromBreak(EricAbstractWaiter w) {
		waiterBack(w);
	}

	public void transDone(Money m) {
		ERestMoney = m;
		System.out.println("Host finished bank interaction and ERest has : $"+ERestMoney.getDollar());
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		int count = 0;
		//System.out.println("waiter size " + waiters.size()+" and myPerson is this: "+myPerson.toString());

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
		if(bs == bankstate.gotManager && waitingCustomers.isEmpty() && God.Get().getHour()==11) {
			msgBank();
			return true;
		}
		return false;

	}

	// Actions

	public void msgBank() {
		bank = (B_Bank) God.Get().getBuilding(2);
		bm = (tellerRole) bank.getOneTeller();
		if(bm == null) {
			bs = bankstate.noTeller;
		}
		else {
		bm.restMoney(ERestNum, ERestMoney, this);
		bs = bankstate.sent;
		}
	}
	
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


	//utilities

	@Override
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	@Override
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

	public void setAccNum(int num) {
		ERestNum = num;
		System.out.println("Host got Eric Resturant Acc Num: "+ERestNum);
	}
	@Override
	protected void enterBuilding() {
		
	}

	@Override
	public void workOver() {
		myPerson.getMoney().add(70, 0);
		bs = bankstate.none;
		exitBuilding(myPerson);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "EHst";
	}


}

