package restaurant;

import agent.Agent;
import restaurant.gui.HostGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

import java.awt.Point;
import java.util.*;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent {
	static final int NTABLES = 3;//a global for the number of tables.
	static final int CAPACITY = 9;// a global for capacity of tables + waiting room
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public Map<Integer, Table> tables = Collections.synchronizedMap(new Hashtable<Integer, Table>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public List<Integer> openWaitSeats = Collections.synchronizedList(new ArrayList<Integer>());
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;

	public HostGui hostGui = null;

	public HostAgent(String name) {
		super();

		this.name = name;
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
		return name;
	}

	public String getName() {
		return name;
	}

	public List<Customer> getWaitingCustomers() {
		ArrayList<Customer> wC = new ArrayList<Customer>();
		for (MyCustomer c : waitingCustomers)
		{
			wC.add(c.customerRef);
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

	public void msgIWantFood(Customer cust) {
		waitingCustomers.add(new MyCustomer(cust));
		stateChanged();
	}
	
	public void msgImLeaving(Customer cust)
	{
		waitingCustomers.remove(cust);
		stateChanged();
	}

	public void msgTableIsOpen(int tableNumber) 
	{
		//print(cust + " leaving " + table);
		tables.get(new Integer(tableNumber)).setUnoccupied();
		stateChanged();
	}

	public void msgIWantToGoOnBreak(Waiter waiter) 
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

	public void msgIWantToGoOnJob(Waiter waiter) 
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

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
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
							openWaitSeats.add(waitingCustomers.get(0).seat);
							assignCustomer(waitingCustomers.get(0));
							return true;
							//return true to the abstract agent to reinvoke the scheduler.
						}
					}
				}
			}
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

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
				waitingCustomers.remove(customer);
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

	//utilities

	public void setGui(HostGui gui)
	{
		hostGui = gui;
	}

	public HostGui getGui()
	{
		return hostGui;
	}

	public void addWaiter(WaiterAgent waiter)
	{
		MyWaiter myWaiter = new MyWaiter(waiter);
		waiters.add(myWaiter);
		stateChanged();
	}
	
	public class Table
	{
		Customer customer;
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
	
	public class MyCustomer
	{
		Customer customerRef;
		int seat;
		
		public MyCustomer(Customer customer)
		{
			this.customerRef = customer;
			this.seat = -1;
		}
	}
	
	private enum WaiterState {onJob, putOnJob, onBreak, putOnBreak};
	
	public class MyWaiter
	{
		WaiterAgent waiter;
		WaiterState state;
		
		public MyWaiter(WaiterAgent waiter)
		{
			this.waiter = waiter;
			this.state = WaiterState.onJob;
		}
	}
}

