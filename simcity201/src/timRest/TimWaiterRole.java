package timRest;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Semaphore;

import SimCity.Base.Role;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import agent.Agent;
import timRest.gui.TimAnimationPanel;
import timRest.gui.TimCookGui;
import timRest.gui.TimWaiterGui;
import timRest.interfaces.TimCashier;
import timRest.interfaces.TimCustomer;
import timRest.interfaces.TimWaiter;
import timRest.interfaces.TimCustomer;

public class TimWaiterRole extends Role implements TimWaiter{

	public static int count = 0;
	private int id;
	
	private Menu menu = new Menu();
	
	private TimHostRole host;
	private TimCookRole cook;
	private TimCashierRole cashier;
	
	private TimWaiterGui waiterGui = new TimWaiterGui(this);
	
	private Semaphore inTransit = new Semaphore(1, true);
	private List<MyCustomer> myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private List<MyCustomer> customersThatNeedChecks = Collections.synchronizedList(new ArrayList<MyCustomer>()); // Probably redundant. TODO: Unify lists
	private List<Integer> tablesToOpen = Collections.synchronizedList(new ArrayList<Integer>());
	
	private Hashtable<Integer, String> pendingOrders = new Hashtable<Integer, String>(); // Probably redundant. TODO: Unify lists
	private Hashtable<Integer, String> readyOrders = new Hashtable<Integer, String>();
	private AgentState state = AgentState.idle;
	private TravelState travelState = TravelState.idle;
	private int currentTable;
	
	private boolean wantsBreak = false;
	
	enum AgentState { idle, seating, takingOrder, serving, reorder, outOfFood, putOnBreak, onBreak, gettingCheck };
	enum TravelState { idle, toHost, atHost, toTable, atTable, toCook, atCook, toCashier, atCashier };
	
	public TimWaiterRole(){
		super();
		id = TimWaiterRole.count;
		TimWaiterRole.count++;
		try {
			inTransit.acquire();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		currentTable = -1;
	}
	
	public Point getPos()
	{
		return new Point(waiterGui.getXPos(), waiterGui.getYPos());
	}
	
	public void msgSeatAtTable(TimCustomer customerRef, int table, Point tablePos)
	{
		MyCustomer c = new MyCustomer(customerRef, table, tablePos);
		
		myCustomers.add(c);
		stateChanged();
	}
	
	public void msgImReadyToOrder(TimCustomer customer)
	{
		MyCustomer myCustomer = null;
		synchronized(myCustomers)
		{
			for (MyCustomer c : myCustomers)
			{
				if (c.customerRef == customer)
				{
					myCustomer = c;
					break;
				}
			}
		}
		if (myCustomer != null)
		{
			Do(AlertTag.TimRest, customer.getName() + " is ready to order.");
			myCustomer.state = CustomerState.readyToOrder;
			stateChanged();
		}
		else
		{
			Do(AlertTag.TimRest, "I couldn't find " + customer.getName() + "!");
		}
	}

	public void msgIWouldLike(TimCustomer customer, String choice)
	{
		//find customer's table
		MyCustomer myCustomer = null;
		synchronized(myCustomers)
		{
			for (MyCustomer c : myCustomers)
			{
				if (c.customerRef == customer)
				{
					myCustomer = c;
					break;
				}
			}
		}
		if (myCustomer != null)
		{
			int tableNumber = myCustomer.tableNumber;
			myCustomer.choice = choice;
			pendingOrders.put(new Integer(tableNumber), choice);
			DoSetTableIcon(tableNumber, choice, true); //GUI stuff
			stateChanged();
		}
	}
	
	// needs to know tableNumber to notify customer that they are out
	public void msgCannotCookItem(int tableNumber)
	{
		// ask customer for another order
		MyCustomer myCustomer = null;
		synchronized(myCustomers)
		{
			for (MyCustomer c : myCustomers)
			{
				if (c.tableNumber == tableNumber)
				{
					myCustomer = c;
					break;
				}
			}
		}
		if (myCustomer != null)
		{
			Do(AlertTag.TimRest,myCustomer.customerRef.getName() + " needs to reorder.");
			myCustomer.state = CustomerState.reorder;
			state = AgentState.reorder;
			stateChanged();
		}
	}

	public void msgOutOfFood(int tableNumber)
	{
		// alert customer
		MyCustomer myCustomer = null;
		synchronized(myCustomers)
		{
			for (MyCustomer c : myCustomers)
			{
				if (c.tableNumber == tableNumber)
				{
					myCustomer = c;
					break;
				}
			}
		}
		if (myCustomer != null)
		{
			myCustomer.state = CustomerState.reorder;
			state = AgentState.outOfFood;
			stateChanged();
		}
	}

	public void msgOrderIsReady(int tableNumber, String choice)
	{
		readyOrders.put(new Integer(tableNumber), choice);
		stateChanged();
	}

	public void msgCanIHaveCheck(TimCustomer customer)
	{
		MyCustomer myCustomer = null;
		synchronized(myCustomers)
		{		
			for (MyCustomer c : myCustomers)
			{
				if (c.customerRef == customer)
				{
					myCustomer = c;
					break;
				}
			}
		}
		if (myCustomer != null)
		{
			customersThatNeedChecks.add(myCustomer);
			myCustomer.state = CustomerState.waitingForCheck;
			stateChanged();
		}
	}
	
	public void msgHereIsACheck(int tableNumber, Money amount)
	{
		MyCustomer myCustomer = null;
		synchronized(myCustomers)
		{
			for (MyCustomer c : myCustomers)
			{
				if (c.tableNumber == tableNumber)
				{
					myCustomer = c;
					break;
				}
			}
		}
		if (myCustomer != null)
		{
			myCustomer.amountOwed = amount;
			Do(AlertTag.TimRest,"Got check.");
			stateChanged();
		}
	}

	public void msgLeavingTable(TimCustomer customer)
	{
		// find customer's table
		MyCustomer myCustomer = null;
		synchronized(myCustomers)
		{
			for (MyCustomer c : myCustomers)
			{
				if (c.customerRef == customer)
				{
					myCustomer = c;
					break;
				}
			}
		}
		if (myCustomer != null)
		{
			int tableNumber = myCustomer.tableNumber;
			myCustomers.remove(myCustomer);
			tablesToOpen.add(tableNumber);
			DoSetTableIcon(tableNumber, "", false);
			stateChanged();
		}
	}

	public void msgGoOnBreak()
	{
		if (state == AgentState.idle && myCustomers.isEmpty())
		{
			state = AgentState.putOnBreak;
			waiterGui.setBreak(true);
			stateChanged();
		}
	}
	
	public void msgCannotBreak()
	{
		waiterGui.setBreak(false);
		wantsBreak = false;
		myPerson.Do("Can't go on break!");
		stateChanged();
	}


	public void msgGoOffBreak()
	{
		if (state == AgentState.onBreak)
		{
			state = AgentState.idle;
			currentTable = -1;
			waiterGui.setBreak(false);
			stateChanged();
		}
		else
		{
			//error
		}
	}

	// from gui
	public void msgSetWantBreak(boolean selected)
	{
		wantsBreak = selected;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToTable()
	{
    	if (travelState == TravelState.toTable)
    	{
    		inTransit.release();// = true;
    		travelState = TravelState.atTable;
    		Do(AlertTag.TimRest,"Reached Table.");
    	}
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToHost()
	{
    	if (travelState == TravelState.toHost)
    	{
    		inTransit.release();// = true;
    		travelState = TravelState.atHost;
    	}
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToCashier()
	{
    	if (travelState == TravelState.toCashier)
    	{
    		inTransit.release();// = true;
    		travelState = TravelState.atCashier;
    	}
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToCook()
	{
    	if (travelState == TravelState.toCook)
    	{
    		inTransit.release();// = true;
    		travelState = TravelState.atCook;
    	}
		stateChanged();
	}
	
	@Override
	protected boolean pickAndExecuteAnAction()
	{
		try
		{
			Do(AlertTag.TimRest,"Looking for things to do.");
			if (!tablesToOpen.isEmpty())
			{
				openTables();
				return true;
			}
			if (state == AgentState.idle)
			{
				if (myCustomers != null)
				{
					for (MyCustomer c : myCustomers)
					{
						if (c.state == CustomerState.waiting)
						{
							travelState = TravelState.toHost;
							state = AgentState.seating;
							waiterGui.GoToHost();
							return true;
						}
					}
					for (MyCustomer c : myCustomers)
					{
						if (c.state == CustomerState.readyToOrder)
						{
							travelState = TravelState.toTable;
							state = AgentState.takingOrder;
							waiterGui.GoToTable(c.tablePos);
							currentTable = c.tableNumber;
							return true;
						}
					}
					for (MyCustomer c : myCustomers)
					{
						if (c.state == CustomerState.waitingForCheck)
						{
							travelState = TravelState.toCashier;
							state = AgentState.gettingCheck;
							waiterGui.GoToCashier();
							return true;
						}
					}
					if (!readyOrders.isEmpty() || !pendingOrders.isEmpty())
					{
						travelState = TravelState.toCook;
						state = AgentState.serving;
						waiterGui.GoToCook();
						return true;
					}
					if (wantsBreak)
					{
						// out of place msg
						host.msgIWantToGoOnBreak(this);
					}
					waiterGui.GoToIdle();
				}
			}
			if (state == AgentState.seating)
			{
				if (travelState == TravelState.atHost)
				{
					MyCustomer c = null;
					for (MyCustomer mC : myCustomers)
					{
						if (mC.state == CustomerState.waiting)
						{
							c = mC;
							break;
						}
					}
					if (c != null)
					{
						travelState = TravelState.toTable;
						c.state = CustomerState.following;
						waiterGui.GoToTable(c.tablePos);
						currentTable = c.tableNumber;
						seatCustomer(c);
					}
					else
					{
						state = AgentState.idle;
						currentTable = -1;
					}
				}
				else if (travelState == TravelState.atTable)
				{
					MyCustomer c = null;
					for (MyCustomer mC : myCustomers)
					{
						if (mC.state == CustomerState.following)
						{
							c = mC;
							break;
						}
					}
					if (c != null)
					{
						c.state = CustomerState.idle;
						reachedTableWithCustomer(c);
					}
					state = AgentState.idle;
					currentTable = -1;
					return true;
				}
			}
			if (state == AgentState.serving)
			{
				if (travelState == TravelState.atCook)
				{
					if (!pendingOrders.isEmpty())
					{
						dropOffOrder();
						return true;
					}
					if (!readyOrders.isEmpty())
					{
						pickUpFood();
						return true;
					}
					waiterGui.GoToIdle();
					state = AgentState.idle;
					currentTable = -1;
					return true;
				}
				if (travelState == TravelState.atTable)
				{
					// here is your order
					if (!readyOrders.containsKey(currentTable))
					{
						// wrong table
						// error
						Do(AlertTag.TimRest,"ERROR!!!!!!!!");
					}
					else
					{
						String choice = readyOrders.get(currentTable);
						MyCustomer c = null;
						for (MyCustomer mC : myCustomers)
						{
							if (mC.tableNumber == currentTable && mC.choice.equals(choice))
							{
								c = mC;
								break;
							}
						}
						if (c != null)
						{
							serveCustomer(c, choice);
						}
					}
				}
				if (travelState == TravelState.atCashier)
				{
					int tableNumber = readyOrders.keys().nextElement();
					String choice = readyOrders.get(tableNumber);
					MyCustomer c = null;
					for (MyCustomer mC : myCustomers)
					{
						if (mC.tableNumber == tableNumber && mC.choice.equals(choice))
						{
							c = mC;
							break;
						}
					}
					if (c != null)
					{
						giveOrderToCashier(c);
					}
				}
			}
			if (state == AgentState.takingOrder)
			{
				if (travelState == TravelState.atTable)
				{
					MyCustomer c = null;
					for (MyCustomer mC : myCustomers)
					{
						if (mC.tableNumber == currentTable)
						{
							c = mC;
							break;
						}
					}
					if (c != null)
					{
						// take order
						takeOrder(c);
					}
					return true;
				}
			}
			if (state == AgentState.reorder)
			{
				if (travelState == TravelState.atTable)
				{
					MyCustomer c = null;
					for (MyCustomer mC : myCustomers)
					{
						if (mC.tableNumber == currentTable)
						{
							c = mC;
							break;
						}
					}
					if (c != null)
					{
						tellCustomerWeAreOut(c);
					}
					return true;
				}
				else if (travelState == TravelState.atCook)
				{
					MyCustomer c = null;
					for (MyCustomer mC : myCustomers)
					{
						if (mC.state == CustomerState.reorder)
						{
							c = mC;
							break;
						}
					}
					if (c != null)
					{
						travelState = TravelState.toTable;
						waiterGui.GoToTable(c.tablePos);
						currentTable = c.tableNumber;
					}
					return true;
				}
			}
			if (state == AgentState.outOfFood)
			{
				if (travelState == TravelState.atTable)
				{
					MyCustomer c = null;
					for (MyCustomer mC : myCustomers)
					{
						if (mC.tableNumber == currentTable)
						{
							c = mC;
							break;
						}
					}
					if (c != null)
					{
						// out of place msg
						Do(AlertTag.TimRest,"Sorry, we are out of food.");
						HashMap<String, Money> newChoices = menu.getChoices();
						newChoices.remove(c.choice);
						c.customerRef.msgNoMoreFood();
						DoSetTableIcon(c.tableNumber, "", false); //GUI stuff
						waiterGui.GoToIdle();
						state = AgentState.idle;
						currentTable = -1;
					}
					return true;
				}
				else if (travelState == TravelState.atCook)
				{
					MyCustomer c = null;
					for (MyCustomer mC : myCustomers)
					{
						if (mC.state == CustomerState.reorder)
						{
							c = mC;
							break;
						}
					}
					if (c != null)
					{
						travelState = TravelState.toTable;
						waiterGui.GoToTable(c.tablePos);
						currentTable = c.tableNumber;
					}
					return true;
				}
			}
			if (state == AgentState.gettingCheck)
			{
				if (travelState == TravelState.atCashier)
				{
					// pick up check
					MyCustomer c = customersThatNeedChecks.get(0);
					askForCheckFromCashier(c);
					return true;
				}
				if (travelState == TravelState.atTable)
				{
					MyCustomer c = null;
					for (MyCustomer mC : myCustomers)
					{
						if (mC.tableNumber == currentTable)
						{
							c = mC;
							break;
						}
					}
					if (c != null)
					{
						// give check
						giveCheckToCustomer(c);
					}
					return true;
				}
			}
			if (state == AgentState.onBreak)
			{
				if (!wantsBreak)
				{
					goOnJob();
					return true;
				}
			}
			if (state == AgentState.putOnBreak)
			{
				goOnBreak();
				return true;
			}
			return false;
		}
		catch (ConcurrentModificationException e)
		{
		    System.out.println("Waiter CME!");
            e.printStackTrace();
			return true;
		}
		catch (Exception e)
		{
            e.printStackTrace();
			return true;
		}
	}

	// actions
	
	private void seatCustomer(MyCustomer customer) {
		customer.customerRef.msgFollowMeToTable(this);
		DoSeatCustomer(customer);
		try {
			inTransit.acquire();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private void reachedTableWithCustomer(MyCustomer customer)
	{
		//table.setOccupant(customer);
		//myCustomers.remove(customer);
		HashMap<String, Money> availableItems = menu.getChoices();
		customer.customerRef.msgSitAtTable(customer.tablePos, availableItems);
	}
	
	private void takeOrder(MyCustomer customer)
	{
		customer.customerRef.msgWhatWouldYouLike();
		state = AgentState.idle;
		currentTable = -1;
		customer.state = CustomerState.idle;
	}
	
	private void serveCustomer(MyCustomer customer, String choice)
	{
		customer.customerRef.msgHereIsYourOrder(choice);
		DoSetTableIcon(customer.tableNumber, choice, false);
		DoSetWaiterIcon(customer.tableNumber, "");
		waiterGui.GoToCashier();
		travelState = TravelState.toCashier;
	}
	
	private void giveOrderToCashier(MyCustomer customer)
	{
		cashier.msgHereIsACheck(this, customer.choice, customer.tableNumber);
		readyOrders.remove(customer.tableNumber);
		waiterGui.GoToIdle();
		state = AgentState.idle;
		currentTable = -1;
	}
	
	private void askForCheckFromCashier(MyCustomer customer)
	{
		cashier.msgWantCheck(customer.tableNumber);
		waiterGui.GoToTable(customer.tablePos);
		currentTable = customer.tableNumber;
		travelState = TravelState.toTable;
	}
	
	private void giveCheckToCustomer(MyCustomer customer)
	{
		Do(AlertTag.TimRest,"Here is your check.");
		customer.customerRef.msgHereIsTheCheck(cashier, customer.amountOwed);
		customersThatNeedChecks.remove(customer);
		customer.state = CustomerState.idle;
		waiterGui.GoToIdle();
		state = AgentState.idle;
		currentTable = -1;
	}
	
	private void tellCustomerWeAreOut(MyCustomer c)
	{
		HashMap<String, Money> newChoices = menu.getChoices();
		newChoices.remove(c.choice);
		c.customerRef.msgWeAreOut(newChoices);
		DoSetTableIcon(c.tableNumber, "", false); //GUI stuff
		waiterGui.GoToIdle();
		state = AgentState.idle;
		currentTable = -1;
	}
	
	private void dropOffOrder()
	{
		int tableNumber = pendingOrders.keys().nextElement();
		cook.msgHereIsAnOrder(this, pendingOrders.get(tableNumber), tableNumber);
		pendingOrders.remove(tableNumber);
	}
	
	private void pickUpFood()
	{
		int tableNumber = readyOrders.keys().nextElement();
		cook.msgPickedUpOrder(tableNumber);
		travelState = TravelState.toTable;
		waiterGui.GoToTable(host.getTablePositions().get(tableNumber));
		currentTable = tableNumber;
		MyCustomer c = null;
		synchronized(myCustomers)
		{
			for (MyCustomer mC : myCustomers)
			{
				if (mC.tableNumber == tableNumber)
				{
					c = mC;
				}
			}
		}
		if (c != null)
		{
			DoSetWaiterIcon(tableNumber, c.choice);
		}
	}
	
	private void openTables()
	{
		synchronized(tablesToOpen)
		{
			for (Integer tableNumber : tablesToOpen)
			{
				host.msgTableIsOpen(tableNumber);
			}
		}
		tablesToOpen.clear();
	}
	
	private void goOnJob()
	{
		host.msgIWantToGoOnJob(this);
	}
	
	private void goOnBreak()
	{
		Do(AlertTag.TimRest,"Going on break.");
		waiterGui.GoToBreak();
		state = AgentState.onBreak;
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(MyCustomer customer) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		myPerson.Do("Seating " + customer.customerRef + " at " + customer.tableNumber);
		waiterGui.GoToTable(customer.tablePos); 
	}
	
	private void DoSetTableIcon(int tableNumber, String choice, boolean questionMark)
	{
		String text = new String();
		if (choice.equals("Steak"))
		{
			text = "ST";
		}
		else if (choice.equals("Chicken"))
		{
			text = "CH";
		}
		else if (choice.equals("Salad"))
		{
			text = "SA";
		}
		else if (choice.equals("Pizza"))
		{
			text = "PZ";
		}
		else
		{
			text = "";
		}
		if (questionMark)
		{
			text = text.concat("?");
		}
		//panel.setTableIcon(tableNumber, text);
	}
	
	private void DoSetWaiterIcon(int tableNumber, String choice)
	{
		String text = new String();
		if (choice.equals("Steak"))
		{
			text = "ST";
		}
		else if (choice.equals("Chicken"))
		{
			text = "CH";
		}
		else if (choice.equals("Salad"))
		{
			text = "SA";
		}
		else if (choice.equals("Pizza"))
		{
			text = "PZ";
		}
		else
		{
			text = "";
		}
		waiterGui.setIcon(text);
	}

	public String getName() {
		return myPerson.name;
	}

	public void setHost(TimHostRole host)
	{
		this.host = host;
	}

	public void setCashier(TimCashierRole cashier)
	{
		this.cashier = cashier;
	}

	public void setCook(TimCookRole cook)
	{
		this.cook = cook;
	}
	
//	public void setPanel(RestaurantPanel panel)
//	{
//		this.panel = panel;
//	}
	
	public void setGui(TimWaiterGui g) {
		waiterGui = g;
		waiterGui.setHome(100 + id * 50, 20);
	}

	public TimWaiterGui getGui() {
		return waiterGui;
	}
	
	public void addItemToMenu(String choice, Money price)
	{
		menu.addItem(choice, price);
	}

	public int getCustomerSize() {
		return myCustomers.size();
	}
	
	enum CustomerState { waiting, following, readyToOrder, reorder, idle, waitingForCheck };
	private class MyCustomer
	{
		TimCustomer customerRef;
		int tableNumber;
		Point tablePos;
		String choice;
		CustomerState state;
		Money amountOwed;
		
		MyCustomer(TimCustomer c, int t, Point tablePos)
		{
			customerRef = c;
			tableNumber = t;
			this.tablePos = tablePos;
			state = CustomerState.waiting;
			amountOwed = new Money(0, 0);
		}
	}
	
	public class Menu
	{
		private ArrayList<Item> items;
		public Menu()
		{
			items = new ArrayList<Item>();
		}
		
		public void addItem(String choice, Money price)
		{
			items.add(new Item(choice, price));
		}
		
		public HashMap<String, Money> getChoices()
		{
			HashMap<String, Money> choices = new HashMap<String, Money>();
			for (Item item : items)
			{
				choices.put(item.choice, item.price);
			}
			return choices;
		}
		
		public class Item
		{
			String choice;
			Money price;
			
			public Item(String choice, Money price)
			{
				this.choice = choice;
				this.price = price;
			}
		}
	}

    @Override
    protected void enterBuilding()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void workOver()
    {
        myPerson.Do("Closing time.");
        exitBuilding(myPerson);
    }

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "TimWaiterRole";
	}

}
