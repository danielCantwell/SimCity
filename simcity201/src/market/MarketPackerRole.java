package market;

import java.util.*;

import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import market.gui.MarketPackerGui;
import market.interfaces.MarketManager;
import market.interfaces.MarketPacker;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketPackerRole extends Role implements MarketPacker {
	
	private MarketPackerGui gui = new MarketPackerGui(this); 
	
	/**
	 * Data
	 */

    public MarketManager manager;
    public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
    public enum AgentState { Idle, Packing };
    public AgentState state;
    public enum AgentLocation { Counter, Item, Transit };
    public AgentLocation location;
    public int destination;

    private boolean canLeave = false;
    
    /**
     * Constructors
     */
    
	public MarketPackerRole() {
		super();
		
		state = AgentState.Idle;
		location = AgentLocation.Counter;
		destination = -1;
	}

	/** 
	 * Messages
	 */
	
	public void msgPackage(int id, String choice, int amount, int location)
	{
	    System.out.println("Recieved msg to pack order");
	    orders.add(new Order(id, choice, amount, location));
	    stateChanged();
	}
	
	public void msgGuiArrivedAtCounter()
	{
	    location = AgentLocation.Counter;
        stateChanged();
	}
	
	public void msgGuiArrivedAtItem()
	{
        location = AgentLocation.Item;
        stateChanged();
	}

    @Override
    protected void enterBuilding() {
        // TODO Auto-generated method stub
        
    }
    
    public void msgLeaveMarket()
    {
        canLeave  = true;
        stateChanged();
    }
	
    public void workOver()
    {
        myPerson.Do("Closing time.");
        //exitBuilding(myPerson);
    }

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction()
	{
	    if (location == AgentLocation.Counter)
	    {
	        if (state == AgentState.Packing)
	        {
	            Order order = null;
                synchronized(orders)
                {
    	            for (Order o : orders)
                    {
                        if (o.state == OrderState.Ready)
                        {
                            order = o;
                        }
                    }
                }
                if (order != null)
                {
                    giveOrder(order);
                    return true;
                }
	        }
	        else if (state == AgentState.Idle)
	        {
	            Order order = null;
                synchronized(orders)
                {
    	            for (Order o : orders)
    	            {
    	                if (o.state == OrderState.Pending)
    	                {
    	                    order = o;
    	                }
    	            }
                }
                if (order != null)
                {
                    pack(order);
                    return false;
                    // wait until arrival
	            }
	        }
	    }
	    else if (location == AgentLocation.Item)
	    {
    	    if (state == AgentState.Packing)
    	    {
                Order order = null;
                synchronized(orders)
                {
        	        for (Order o : orders)
                    {
                        if (o.state == OrderState.Processing && o.location == destination)
                        {
                            order = o;
                        }
                    }
                }
                if (order != null)
                {
                    grabItem(order);
                    returnToCounter();
                    return false;
                    // wait until arrival
                }
    	    }
	    }
	    if (canLeave)
	    {
	        leaveBuilding();
	    }
	    
		return false;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

	/**
	 * Actions
	 */

	private void giveOrder(Order order)
	{
        Do(AlertTag.Market, "Here is the " + order.choice + ".");
	    manager.msgOrderPacked(order.id, order.choice, order.amount);
	    orders.remove(order);
	    state = AgentState.Idle;
	}
	
	private void pack(Order order)
	{
	    state = AgentState.Packing;
	    order.state = OrderState.Processing;
	    
	    location = AgentLocation.Transit;
	    destination = order.location;
	    gui.DoGoToItem(order.location, order.choice);
	}
    
    private void leaveBuilding()
    {
        myPerson.money.add(new Money(75, 00));
        Info(AlertTag.Market, "I have " + myPerson.money + " and I'm leaving the building.");
        canLeave = false;
        exitBuilding(myPerson);
    }
	
	private void grabItem(Order order)
	{
	    Do(AlertTag.Market, "Grabbing " + order.choice + " from shelf.");
	    manager.msgGrabbingItem(order.choice, order.amount);
	    order.state = OrderState.Ready;
	}
	
	private void returnToCounter()
	{
        location = AgentLocation.Transit;
	    gui.DoGoToCounter();
	}
	
    /**
     * Utilities
     */

	public void setGui(MarketPackerGui gui) {
		this.gui = gui;
	}
	
	public MarketPackerGui getGui()
	{
	    return gui;
	}
	
	public void setManager(MarketManager manager)
	{
	    this.manager = manager;
	}
    
    public void setPerson(Person person)
    {
        super.setPerson(person);
    }
	
	/**
	 * Inner Classes
	 */
	public enum OrderState { Pending, Processing, Ready };
    
    public class Order
    {
    		int id;
        String choice;
        int amount;
        int location;
        public OrderState state;
        
        Order(int id, String choice, int amount, int location)
        {
            this.id = id;
            this.choice = choice;
            this.amount = amount;
            this.location = location;
            state = OrderState.Pending;
        }
        
        public boolean equals(Order other)
        {
            return other.id == id && other.choice.equals(choice) && other.amount == amount && other.location == location;
        }
    }

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "MPkr";
	}
}
