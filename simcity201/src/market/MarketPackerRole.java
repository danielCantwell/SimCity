package market;

import java.awt.Point;
import java.util.*;

import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import market.MarketManagerRole.Order;
import market.MarketManagerRole.OrderState;
import market.MarketManagerRole.OrderType;
import market.gui.MarketManagerGui;
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

    public MarketManagerRole manager;
    public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
    public enum AgentState { Idle, Packing };
    public AgentState state;
    public enum AgentLocation { Counter, Item, Transit };
    public AgentLocation location;
    
    /**
     * Constructors
     */
    
	public MarketPackerRole() {
		super();
		
		state = AgentState.Idle;
		location = AgentLocation.Counter;
	}

	/** 
	 * Messages
	 */
	
	public void msgPackage(String name, String choice, int amount, int location)
	{
	    System.out.println("Recieved msg to pack order");
	    orders.add(new Order(name, choice, amount, location));
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
	
    public void workOver()
    {
        // TODO Auto-generated method stub
        
    }

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction()
	{
	    if (location == AgentLocation.Counter)
	    {
	        if (state == AgentState.Packing)
	        {
	            for (Order o : orders)
                {
                    if (o.state == OrderState.Ready)
                    {
                        giveOrder(o);
                    }
                }
	        }
	        else if (state == AgentState.Idle)
	        {
	            for (Order o : orders)
	            {
	                if (o.state == OrderState.Pending)
	                {
	                    pack(o);
	                }
	            }
	        }
	    }
	    else
	    {
    	    if (state == AgentState.Packing)
    	    {
    	        for (Order o : orders)
                {
                    if (o.state == OrderState.Ready)
                    {
                        returnToCounter();
                    }
                }
    	    }
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
	    manager.msgOrderPacked(order.name, order.choice, order.amount);
	    state = AgentState.Idle;
	}
	
	private void pack(Order order)
	{
	    state = AgentState.Packing;
	    order.state = OrderState.Processing;
	    
	    location = AgentLocation.Transit;
	    gui.DoGoToItem(order.location);
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
	
	public void setManager(MarketManagerRole manager)
	{
	    this.manager = manager;
	}
    
    public void setPerson(Person person)
    {
        super.setPerson(person);
        person.gui = gui;
    }
	
	/**
	 * Inner Classes
	 */
	public enum OrderState { Pending, Processing, Ready };
    
    public class Order
    {
        String name;
        String choice;
        int amount;
        int location;
        OrderState state;
        
        Order(String name, String choice, int amount, int location)
        {
            this.name = name;
            this.choice = choice;
            this.amount = amount;
            this.location = location;
            state = OrderState.Pending;
        }
        
        public boolean equals(Order other)
        {
            return other.name.equals(name) && other.choice.equals(choice) && other.amount == amount && other.location == location;
        }
    }
}
