package timRest;

import Bank.tellerRole;
import EricRestaurant.EricHost.bankstate;
import SimCity.Base.God;
import SimCity.Base.Role;
import SimCity.Buildings.B_Bank;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import timRest.gui.TimCookGui;
import timRest.gui.TimHostGui;
import timRest.interfaces.TimCustomer;
import timRest.interfaces.TimWaiter;

import java.awt.Point;
import java.util.*;

import brianRest.BrianCustomerRole.CustomerState;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class TimHostRole extends Role {
	static final int NTABLES = 3;//a global for the number of tables.
	static final int CAPACITY = 9;// a global for capacity of tables + waiting room
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public Map<Integer, Table> tables = Collections.synchronizedMap(new Hashtable<Integer, Table>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public enum bankstate {none, gotManager, sent};
	private bankstate bs = bankstate.none;
    private TimCookRole cook;
    private TimCashierRole cashier;
    public int TAccNum;
	public Money TRestMoney;
	B_Bank bank;
	tellerRole bm;
	public List<Integer> openWaitSeats = Collections.synchronizedList(new ArrayList<Integer>());
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	private boolean wantsLeave = false;

	public TimHostGui hostGui = new TimHostGui(this);

	public TimHostRole() {
		super();

		// make some tables
		for (int ix = 0; ix < NTABLES; ix++) {
			addTableToHash(ix, new Table(ix, 200 + ix*100, 250));
		}
		
		for (int i = 0; i < CAPACITY-NTABLES; i++)
		{
			openWaitSeats.add(i);
		}
	}

	public String getMaitreDName() {
		return getName();
	}

	public String getName() {
		return myPerson.name;
	}

	public List<TimCustomer> getWaitingCustomers() {
		ArrayList<TimCustomer> wC = new ArrayList<TimCustomer>();
		synchronized(waitingCustomers)
		{
    		for (MyCustomer c : waitingCustomers)
    		{
    			wC.add(c.customerRef);
    		}
		}
		return wC;
	}
	
	public void addTableToHash(int key, Table table)
	{
		tables.put(new Integer(key), table);
	}
	
	public void removeTableByKey(int key)
	{
		tables.remove(new Integer(key));
	}
	
	public Collection<Table> getTables()
	{
		return tables.values();
	}
	
	public Hashtable<Integer, Point> getTablePositions()
	{
		Hashtable<Integer, Point> points = new Hashtable<Integer, Point>();
		synchronized(tables)
		{
			for (Table table : tables.values())
			{
				points.put(table.tableNumber, new Point(table.x, table.y));
			}
		}
		return points;
	}
	
	public ArrayList<String> getTableIcons()
	{
		ArrayList<String> icons = new ArrayList<String>();
		synchronized(tables)
		{
			for (Table table : tables.values())
			{
				icons.add(table.icon);
			}
		}
		return icons;
	}
	
	public void setTableIcon(int tableNumber, String icon)
	{
		synchronized(tables)
		{
			for (Table table : tables.values())
			{
				if (table.tableNumber == tableNumber)
				{
					table.icon = icon;
				}
			}
		}
	}
	// Messages

	public void msgIWantFood(TimCustomer cust) {
	    synchronized(waitingCustomers)
	    {
	        waitingCustomers.add(new MyCustomer(cust));
	    }
	    stateChanged();
	}
	
	public void msgImLeaving(TimCustomer cust)
	{
        synchronized(waitingCustomers)
        {
            waitingCustomers.remove(cust);
        }
		stateChanged();
	}

	public void msgTableIsOpen(int tableNumber) 
	{
		//print(cust + " leaving " + table);
		tables.get(new Integer(tableNumber)).setUnoccupied();
        synchronized(waitingCustomers)
        {
            for (MyCustomer c : waitingCustomers)
            {
                if (c.seat == tableNumber)
                {
                    waitingCustomers.remove(c);
                    break;
                }
            }
        }
		stateChanged();
	}

	public void msgIWantToGoOnBreak(TimWaiter waiter) 
	{
		synchronized(waiters)
		{
			for (MyWaiter w : waiters)
			{
				if (w.waiter == waiter)
				{
					w.state = WaiterState.putOnBreak;
					stateChanged();
					return;
				}
			}
		}
	}

	public void msgIWantToGoOnJob(TimWaiter waiter) 
	{
		// find waiter
		synchronized(tables)
		{
			for (MyWaiter w : waiters)
			{
				if (w.waiter == waiter)
				{
					w.state = WaiterState.putOnJob;
					stateChanged();
					return;
				}
			}
		}
	}
	public void transDone(Money m) {
		TRestMoney = m;
		System.out.println("Host finished bank interaction and TRest has : $"+TRestMoney.getDollar());
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
	    if (wantsLeave && waitingCustomers.isEmpty())
	    {
	        leaveBuilding();
	        return false;
	    }
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		//very slow... but no redundant data
		synchronized(waiters)
		{
			for (MyWaiter waiter : waiters)
			{
				if (waiter.state == WaiterState.putOnBreak)
				{
					for (MyWaiter mW : waiters)
					{
						// if there exists another waiter on the job
						if (mW.state == WaiterState.onJob && mW != waiter)
						{
							// tell waiter can go on break	
							putOnBreak(waiter);
							return true;
						}
					}
					// waiter cannot take break
					notifyWaiterCannotBreak(waiter);
					return true;
				}
				if (waiter.state == WaiterState.putOnJob)
				{
					// tell waiter can go on break
					takeOffBreak(waiter);
					return true;
				}
			}
		}
		synchronized(waitingCustomers)
		{
			for (MyCustomer c : waitingCustomers)
			{
				if (c.seat == -1)
				{
					if (!openWaitSeats.isEmpty())
					{
						// seat customer
						synchronized(openWaitSeats)
						{
							for (Integer index : openWaitSeats)
							{
								c.seat = index;
								waitingCustomers.get(waitingCustomers.size()-1).customerRef.msgPleaseSit(new Point(25, 50 + index * 30));
								openWaitSeats.remove(index);
								return true;
							}
						}
					}
					else // restaurant is full
					{
						waitingCustomers.get(waitingCustomers.size()-1).customerRef.msgWeAreFull();
						c.seat = -2;
						return true;
					}
				}
			}
		}
		if (!waiters.isEmpty() && !waitingCustomers.isEmpty())
		{
			synchronized(tables)
			{
				for (Table table : getTables())
				{
					synchronized(waitingCustomers)
					{
						if (!table.isOccupied())
						{
						    for (MyCustomer customer : waitingCustomers)
						    {
						        if (customer.state == CustomerState.Waiting)
    							{
						            openWaitSeats.add(customer.seat);
        							assignCustomer(customer);
        							return true;
        							//return true to the abstract agent to reinvoke the scheduler.
    							}
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
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	public void msgBank() {
		bank = (B_Bank) God.Get().getBuilding(2);
		bm = (tellerRole) bank.getOneTeller();
		bm.restMoney(TAccNum, TRestMoney, this);
		bs = bankstate.sent;
	}
	
	private void assignCustomer(MyCustomer customer)
	{
		Table table = null;
		for (Table t : getTables())// find open table
		{
			if (!t.isOccupied())
			{
				table = t;
				break;
			}
		}
		if (table != null)
		{
			MyWaiter myWaiter = null;
			int min = Integer.MAX_VALUE;
			for (MyWaiter w : waiters)
			{
				int size = w.waiter.getCustomerSize();
				if (size < min && w.state == WaiterState.onJob)
				{
					min = size;
					myWaiter = w;
				}
			}
			if (myWaiter != null)
			{
				myWaiter.waiter.msgSeatAtTable(customer.customerRef, table.tableNumber, new Point(table.x, table.y));//the action
				table.customer = customer.customerRef;
				customer.state = CustomerState.Seated;
			}
		}
	}
	
	private void putOnBreak(MyWaiter waiter)
	{
		waiter.state = WaiterState.onBreak;
		waiter.waiter.msgGoOnBreak();
	}
	
	private void takeOffBreak(MyWaiter waiter)
	{
		waiter.state = WaiterState.onJob;
		waiter.waiter.msgGoOffBreak();
	}
	
	private void notifyWaiterCannotBreak(MyWaiter waiter)
	{
		waiter.state = WaiterState.onJob;
		waiter.waiter.msgCannotBreak();
	}
    
    private void leaveBuilding()
    {
        if (cashier != null)
        {
            cashier.msgLeaveWork();
        }
        if (cook != null)
        {
            cook.msgLeaveWork();
        }
        myPerson.money.add(new Money(100, 00));
        Info(AlertTag.TimRest, "I have " + myPerson.money + " and I'm leaving the building.");
        wantsLeave = false;
        exitBuilding(myPerson);
    }

	//utilities

	public void setGui(TimHostGui gui)
	{
		hostGui = gui;
	}

	public TimHostGui getGui()
	{
		return hostGui;
	}

	public void addWaiter(TimWaiterRole waiter)
	{
		MyWaiter myWaiter = new MyWaiter(waiter);
		myWaiter.waiter.setHost(this);
		if (cashier != null)
		{
		    myWaiter.waiter.setCashier(cashier);
		}
        if (cook != null)
        {
            myWaiter.waiter.setCook(cook);
        }
		waiters.add(myWaiter);
		stateChanged();
	}
	
	public TimCookRole getCook()
	{
	    return cook;
	}

    public TimCashierRole getCashier()
    {
        return cashier;
    }

    public void setCashier(TimCashierRole cashier)
    {
        this.cashier = cashier;
        synchronized(waiters)
        {
            for (MyWaiter myWaiter : waiters)
            {
                myWaiter.waiter.setCashier(cashier);
            }
        }
    }

    public void setCook(TimCookRole cook)
    {
        this.cook = cook;
        synchronized(waiters)
        {
            for (MyWaiter myWaiter : waiters)
            {
                myWaiter.waiter.setCook(cook);
            }
        }
    }
	
	public class Table
	{
		TimCustomer customer;
		int tableNumber;
		int x, y;
		String icon;

		public Table(int tableNumber, int x, int y) {
			this.tableNumber = tableNumber;
			this.x = x;
			this.y = y;
			icon = new String("");
		}
		
		public int getTableNumber()
		{
			return tableNumber;
		}

		public void setUnoccupied() {
			customer = null;
		}

		public boolean isOccupied() {
			return customer != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	private enum CustomerState {Waiting, Seated};
	
	public class MyCustomer
	{
		TimCustomer customerRef;
		int seat;
		CustomerState state;
		
		public MyCustomer(TimCustomer customer)
		{
			this.customerRef = customer;
			this.seat = -1;
			state = CustomerState.Waiting;
		}
	}
	
	private enum WaiterState {onJob, putOnJob, onBreak, putOnBreak};
	
	public class MyWaiter
	{
		TimWaiterRole waiter;
		WaiterState state;
		
		public MyWaiter(TimWaiterRole waiter)
		{
			this.waiter = waiter;
			this.state = WaiterState.onJob;
		}
	}

	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
        myPerson.Do("Closing time.");
        wantsLeave = true;
        stateChanged();
	}

    public boolean isRestaurantReady()
    {
        return !waiters.isEmpty() && cook != null && cashier != null;
    }

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "TimHostRole";
	}

	public void setBM() {
		bs = bankstate.gotManager;
		stateChanged();
	}
	
	public void setMoney(Money m) {
		TRestMoney = m;
	}
	public Money getMoney() {
		return TRestMoney;
	}
	public void setAcc(int acc) {
		TAccNum = acc;
	}
}

